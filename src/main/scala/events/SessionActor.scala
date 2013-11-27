package events

import akka.actor._

class SessionActor(session:Session) extends Actor with ActorLogging {

	val urlCount = scala.collection.mutable.Map[String,Int]()
	val history = scala.collection.mutable.MutableList[Request]()

	def receive : Receive = {
		case msg @ Request(session, timestamp, url) => {
      log.info(s"${self.path.name} received request: " + msg.toString)
			history += msg
			urlCount.get(url) match {
				case Some(value) => urlCount(url) = value + 1
				case None => urlCount(url) = 1
			}
		}
	}
}

object SessionActor {
	def props(session:Session) = Props(new SessionActor(session))
}