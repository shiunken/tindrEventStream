package events

import akka.actor._
import events.Messages.GetRequestHandler
import akka.actor.Terminated
import akka.actor.SupervisorStrategy._

/**
 * User: ken
 * Date: 2013-11-28 9:15 AM
 */
class SupervisorActor extends Actor with ActorLogging  {

  val statsCollector = context.actorOf(StatsCollector.props, "StatsCollector")
  val requestHandler = context.actorOf(RequestHandler.props(statsCollector), "RequestHandler")

  val actorRestarts = scala.collection.mutable.Map[ActorRef, Int]()

  context.watch(statsCollector)

  override def receive: Receive = {
    case Terminated(actor) => log.info(s"${actor.path.name} died! send an email")
    case _ => {

    }
  }


  override val supervisorStrategy =
    OneForOneStrategy(maxNrOfRetries = 5) {
      case _: Exception ⇒ {
        Restart
      }
    }

}

object SupervisorActor {
  def props = Props(new SupervisorActor())
}
