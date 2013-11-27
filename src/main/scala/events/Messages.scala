package events

object Messages {
	case class CreateUser(sessionId:String)
	case class InactiveSession(timestamp:Long)
	case class AddStats(urlCount:Map[String,Int])
}