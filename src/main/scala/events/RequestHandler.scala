package events

import akka.actor._

class RequestHandler(statsCollector:ActorRef) extends Actor with ActorLogging {
	
	import Messages._
	import concurrent.duration._
	import context.dispatcher
	
	val sessionMap = scala.collection.mutable.Map[Session,ActorRef]()
	
	def receive : Receive = {
		
		case msg @ Request(session, timestamp, url) => {
			if(!sessionMap.contains(session)) {
				log.info(s"Created SessionActor for ${session}")
				val newSessionActor = context.actorOf(SessionActor.props(session, statsCollector))
				sessionMap(session) = newSessionActor
			}
			val actor = sessionMap(session)
			actor ! msg
			context.system.scheduler.scheduleOnce(10 seconds, actor, InactiveSession(timestamp))
		}
	}
}

object RequestHandler {
	def props(statsCollector:ActorRef) = Props(new RequestHandler(statsCollector))
}