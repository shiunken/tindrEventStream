package events

import akka.actor._

object StreamingStats extends App {

  val stream = new EventStream(5)

	val actorSystem = ActorSystem("streaming_system")
	val supervisor = actorSystem.actorOf(Supervisor.props)

  for {
    i <- 1 to 20
    requests = stream.tick
  } println(requests)
}
