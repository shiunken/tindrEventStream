package events

import akka.actor._
import akka.pattern.ask
import akka.util.Timeout
import scala.concurrent.duration._

object StreamingStats extends App {

	val actorSystem = ActorSystem("streaming-system")
  val supervisor = actorSystem.actorOf(SupervisorActor.props, "supervisor")
}
