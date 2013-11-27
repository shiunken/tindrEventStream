package events

import akka.actor.ActorSystem
import akka.testkit.{TestActorRef, TestKit}
import org.scalatest.{ShouldMatchers, WordSpecLike}
import events.Messages.AddStats

/**
 * User: ken
 * Date: 2013-11-27 1:46 PM
 */
class StatsCollectorSpec extends TestKit(ActorSystem("testing")) with WordSpecLike with ShouldMatchers {

  "When StatsCollector receives an AddStats it " should {
    "aggregate the stats correctly" in {

      val statsCollector = TestActorRef(new StatsCollector())
      val underlyingActor = statsCollector.underlyingActor

      statsCollector ! AddStats(Map.empty, 10, "landing", "sink", "ie", "other")
      statsCollector ! AddStats(Map.empty, 5, "landing2", "sink2", "google", "other")
      statsCollector ! AddStats(Map.empty, 6, "landing", "sink2", "google", "blah");

      val expectedStatMap =
        Map( "landingPage" -> Map("landing" -> 2, "landing2" -> 1),
          "browser" -> Map("google" -> 2, "ie" -> 1),
          "referrer" -> Map("blah" -> 1, "other" -> 2),
          "sinkPage" -> Map("sink2" -> 2, "sink" -> 1)
        )

      assertResult( (10 + 5 + 6) /3 ){ underlyingActor.averageDuration }
      assertResult( expectedStatMap) { underlyingActor.statMap }

    }
  }


}
