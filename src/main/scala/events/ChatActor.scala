package events

import akka.actor.{Props, ActorLogging, Actor}
import events.Messages.StartChat

/**
 * User: ken
 * Date: 2013-11-28 10:15 AM
 */
class ChatActor extends Actor with ActorLogging {

  override def receive: Receive = {
    case StartChat => {
      log.info("Starting a chat")
    }
  }

}

object ChatActor {
  def props = Props(new ChatActor())
}
