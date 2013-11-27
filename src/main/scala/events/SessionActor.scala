package events

import akka.actor._

class SessionActor(session:Session, statsCollector:ActorRef) extends Actor with ActorLogging {

	import Messages._

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
		
		case InactiveSession(timestamp) => {
			if(history.last.timestamp <= timestamp){
				statsCollector ! AddStats(urlCount.toMap, duration, landingPage, sinkPage, browser, referrer)
				context.stop(self)
			}
		}
	}
	
	def duration : Long = 
		history.last.timestamp - history.head.timestamp
		
	def landingPage : String = 
		history.head.url
		
	def sinkPage : String = 
		history.last.url
		
	def browser : String = 
		history.head.session.browser
		
	def referrer : String = 
		history.head.session.referrer
}

object SessionActor {
	def props(session:Session, statsCollector:ActorRef) = Props(new SessionActor(session, statsCollector))
}