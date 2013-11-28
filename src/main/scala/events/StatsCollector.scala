package events

import akka.actor._

class StatsCollector extends Actor with ActorLogging {
	
	import Messages._
	
	var urlVisits = scala.collection.mutable.Map[String,Int]()
	var pageViews = scala.collection.mutable.Seq[Int]()
	var visitDuration = scala.collection.mutable.Seq[Long]()
	var statMap = scala.collection.mutable.Map[String,scala.collection.mutable.Map[String,Int]]()
	
	def receive : Receive = {
		case AddStats(newStats, duration, lp, sp, b, r) => {
			
			// basic counts
			increment("landingPage", lp)
			increment("sinkPage", sp)
			increment("browser", b)
			increment("referrer", r)
			
			//avg page views
			pageViews = pageViews :+ newStats.keys.size
			
			//average visit time
			visitDuration = visitDuration :+ duration
			
			//merge stats together
			urlVisits = urlVisits ++ newStats.map{ case (k,v) => k -> (v + urlVisits.getOrElse(k,0)) }
			log.info(s"URL visits: ${urlVisits} map:${statMap} pageViews:${averagePageViews}")
      log.info(s"topFive landing pages: " + topFiveStats("landingPage").toString())
		}
	}
	
	def averagePageViews : Int = {
		pageViews.sum / pageViews.size
	}

  def averageDuration : Long = {
    visitDuration.sum / visitDuration.size
  }

  def topFiveStats(key: String): Seq[(String, Int)] = {
    statMap.getOrElse(key, Map.empty).toSeq.sortWith( (a, b) => a._2 > b._2 ).take(5)
  }

	
	def increment(stat:String, key:String) = {
		val innerMap = statMap.getOrElse(stat, scala.collection.mutable.Map[String,Int]())
		innerMap.get(key) match {
			case Some(value) => innerMap(key) = value + 1
			case None => innerMap(key) = 1
		}
		statMap(stat) = innerMap
	}
}

object StatsCollector {
	def props = Props(new StatsCollector())
}