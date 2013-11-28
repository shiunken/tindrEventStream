package events

import akka.actor._
import akka.remote.RemoteScope
import scala.util._

class RequestHandler(statsCollector:ActorRef) extends Actor with ActorLogging {
	
	import Messages._
	import concurrent.duration._
	import context.dispatcher
	
	val sessionMap = scala.collection.mutable.Map[Session,ActorRef]()

  //test code
  val stream = new EventStream(5)
  for ( i <- 1 to 20; request <- stream.tick ) {
    self ! request
  }

	// lookup an existing remote actor
	val selection = context.actorSelection("akka.tcp://streaming-system@172.18.137.213:2552/user/supervisor/RequestHandler/ChatActor");
	val chatActor1 = selection.resolveOne(10 seconds) onComplete {
		case Success(result) => log.info("Looked up remote actor:"+result); result ! StartChat
		case Failure(ex) => log.info("Exception:"+ex)
	}

	// create a new remote actor
	val addr = AddressFromURIString("akka.tcp://streaming-system@172.18.137.213:2552")
	val chatActor = context.actorOf(Props[ChatActor].withDeploy(Deploy(scope = RemoteScope(addr))), "Chatter")
	log.info("created remote actor:"+chatActor)

	def receive : Receive = {
		
		case msg @ Request(session, timestamp, url) => {
			if(!sessionMap.contains(session)) {
				log.info(s"Created SessionActor for ${session}")
        val newActor = context.actorOf(SessionActor.props(session, statsCollector))
				sessionMap(session) = newActor
				context.watch(newActor)
        chatActor ! StartChat
			}
			val actor = sessionMap(session)
			actor ! msg

      context.system.scheduler.scheduleOnce(10 seconds, actor, InactiveSession(timestamp))
		}
		
		case Terminated(actor) => {
			val entry = sessionMap.find(entry => entry._2 == actor)
			if(entry.nonEmpty) {
				sessionMap.remove(entry.get._1)
			}
		}
	}
}

object RequestHandler {
	def props(statsCollector:ActorRef) = Props(new RequestHandler(statsCollector))
}