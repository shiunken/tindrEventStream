package events

import akka.actor._

class RequestHandler extends Actor {
	
	import Messages._
	
	val actorMap = scala.collection.mutable.Map[Session,ActorRef]()
	
	def receive : Receive = {
		case msg @ Request(session, timestamp, url) => {
			if(!actorMap.contains(session)) {
				val newSessionActor = context.actorOf(SessionActor.props(session))
				actorMap(session) = newSessionActor
			}
			actorMap(session) ! msg
		}
	}
}

object RequestHandler {
	def props = Props(new RequestHandler())
}