import akka.actor._

class Supervisor extends Actor {
	
	def receive : Receive = {
	}
}

object Supervisor {
	def props = 
	Props(new Supervisor())
}