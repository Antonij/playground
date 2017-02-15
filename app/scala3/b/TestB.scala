package scala3.b

import scala.concurrent.Await
import scala.concurrent.duration.DurationInt

import akka.actor.{ ActorSystem, PoisonPill }
import akka.pattern._
import akka.util.Timeout
import scala3.b.AddressPickActor.Name
import scala3.b.CapitalizationActor.{ CapitalizeAsk, CapitalizeAskFailHandle }
import scala3.b.GreetingActor.HiMyNameIs

object TestB {

  def main(args: Array[String]): Unit = {
    val actorSystem = ActorSystem("test-system")
    implicit val ex = actorSystem.dispatcher
    implicit val timeout: Timeout = 2.seconds

    /*
     * Forwarding behaves kind of like a public method.
     * The actors know each other but you don't have to
     */
    val greeter = actorSystem.actorOf(GreetingActor.props, "gruetzieh")

    val greet = greeter ? HiMyNameIs("franz")
    greet.map(println(_))

    /*
     *  Or you can just ask everything yourself and flatMap it
     */
    val cap = actorSystem.actorOf(CapitalizationActor.props, "captain")
    val pick = actorSystem.actorOf(AddressPickActor.props, "picker")

    val otherGreet = (cap ? CapitalizeAsk("fritz"))
      .mapTo[String]
      .flatMap(pick ? Name(_))
      .mapTo[String]

    otherGreet.foreach(println)

    /*
     *  What if something breaks?
     *  *hint* timeout because no answer is given
     */
    val iHopeThisWorks = (cap ? CapitalizeAsk("?franz")).mapTo[String]
    try {
      val stuff = Await.result(iHopeThisWorks, 3.seconds)
    } catch {
      case e: Exception =>
        println("Non handled ask:")
        e.printStackTrace()
//        e.printStackTrace(System.)
        println()
    }

    Thread.sleep(4000)

    /*
     * Is this more useful?
     */
    val letsSee = (cap ? CapitalizeAskFailHandle("?Carl")).mapTo[String]
    try {
      val alsoStuff = Await.result(letsSee, 3.seconds)
    } catch {
      case e: Exception =>
        println("Handled ask:")
        e.printStackTrace()
        println()
    }

    Thread.sleep(4000)

    // cleanup of children?
    greeter ! PoisonPill

    while (true) { Thread.sleep(1000) }
  }

}