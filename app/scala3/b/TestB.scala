package scala3.b

import scala.concurrent.Await
import scala.concurrent.duration.DurationInt

import akka.actor.ActorSystem
import akka.pattern._
import akka.util.Timeout
import scala3.b.GreetingActor.HiMyNameIs
import scala3.b.CapitalizationActor.CapitalizeAsk
import scala3.b.AddressPickActor.Name
import scala.util.Success
import scala.util.Failure
import scala3.b.GreetingActor.AlsoHiMyNameIs

object TestB {

  def main(args: Array[String]): Unit = {
    val system = ActorSystem("test-system")
    implicit val ex = system.dispatcher
    implicit val timeout: Timeout = 5.seconds

    /* Forwarding behaves kind of like a public method.
     * The actors know each other but you don't have to */
    val greeter = system.actorOf(GreetingActor.props, "gruetzieh")

    val greet = greeter ? HiMyNameIs("franz")
    greet.map(println(_))

    // Or you can just ask everything yourself and flatMap it
    val cap = system.actorOf(CapitalizationActor.props, "captain")
    val pick = system.actorOf(AddressPickActor.props, "picker")

    val otherGreet = (cap ? CapitalizeAsk("fritz"))
    .mapTo[String]
    .flatMap(pick ? Name(_))
    .mapTo[String]

    otherGreet.foreach(println)

    //What if something breaks?
    val iHopeThisWorks = (cap ? CapitalizeAsk("?franz")).mapTo[String]
    try {
      val stuff = Await.result(iHopeThisWorks, 2.seconds)
    } catch {
      case e: Exception => e.printStackTrace()
    }

   val letsSee = (greeter ? AlsoHiMyNameIs("?Carl")).mapTo[String]
   try {
      val alsoStuff = Await.result(letsSee, 2.seconds)
    } catch {
      case e: Exception => e.printStackTrace()
    }

    while (true) { Thread.sleep(1000) }
  }

}