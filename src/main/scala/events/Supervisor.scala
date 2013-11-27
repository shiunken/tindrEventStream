package events

import akka.actor._

class Supervisor extends Actor {
	
	import Messages._
	
	def receive : Receive = {
		case CreateUser(sessionId:String) => {
		
		}
	}
}

object Supervisor {
	def props = 
	Props(new Supervisor())
}