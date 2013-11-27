package events

import akka.actor._

class StatsCollector extends Actor with ActorLogging {
	
	import Messages._
	
	var allStats = scala.collection.mutable.Map[String,Int]()
	
	def receive : Receive = {
		case AddStats(newStats) => {
			allStats = allStats ++ newStats.map{ case (k,v) => k -> (v + allStats.getOrElse(k,0)) }
			log.info(s"new state : ${allStats}")
		}
	}
}

object StatsCollector {
	def props = Props(new StatsCollector())
}