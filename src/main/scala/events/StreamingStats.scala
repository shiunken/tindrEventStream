package events

import akka.actor._

object StreamingStats extends App {

  val stream = new EventStream(5)

	val actorSystem = ActorSystem("streaming-system")
	val requestHandler = actorSystem.actorOf(RequestHandler.props)

  for ( i <- 1 to 20; request <- stream.tick ) {
		requestHandler ! request
	}
}
