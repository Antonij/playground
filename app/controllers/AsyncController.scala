package controllers

import scala.concurrent.{ ExecutionContext, Future, Promise }
import scala.concurrent.duration.{ DurationInt, FiniteDuration }

import akka.actor.ActorSystem
import akka.pattern.ask
import javax.inject.{ Inject, Singleton }
import play.api.mvc.{ Action, Controller }
import scala3.c.PalindromeActor
import scala3.c.PalindromeActor._
import akka.util.Timeout
import play.api.libs.json.Json
import akka.actor.Actor
import akka.actor.ActorLogging
import akka.actor.Props
import scala3.c.PalindromeActor.Check
import scala3.c.PalindromeActor.CheckResult
import play.api.libs.ws.WS
import play.api.Play

/**
 * This controller creates an `Action` that demonstrates how to write
 * simple asynchronous code in a controller. It uses a timer to
 * asynchronously delay sending a response for 1 second.
 *
 * @param actorSystem We need the `ActorSystem`'s `Scheduler` to
 * run code after a delay.
 * @param exec We need an `ExecutionContext` to execute our
 * asynchronous code.
 */
@Singleton
class AsyncController @Inject() (actorSystem: ActorSystem)(implicit exec: ExecutionContext) extends Controller {

  /**
   * Create an Action that returns a plain text message after a delay
   * of 1 second.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/message`.
   */
  def message = Action.async {
    getFutureMessage(1.second).map { msg => Ok(msg) }
  }

  private def getFutureMessage(delayTime: FiniteDuration): Future[String] = {
    val promise: Promise[String] = Promise[String]()
    actorSystem.scheduler.scheduleOnce(delayTime) { promise.success("Hi!") }
    promise.future
  }

  def stest = Action.async {
    implicit val timeout: Timeout = 5.seconds
    val pa = actorSystem.actorOf(PalindromeActor.props)
    val res = pa ? Check("stuff")
    res.mapTo[CheckResult]
    .map(Json.toJson(_)(PalindromeActor.checkResultFormat))
    .map(Ok(_).as(JSON))
  }

}

class Tactor(stuff: String = "") extends Actor with ActorLogging {
  def receive = {
    case s: String =>
      log.info(s"Got $s")
      sender ! s+s.reverse
    case x =>
      log.info(s"What is this? $x")
  }
}
object Tactor {
  val props = Props[Tactor]
  def props(stuff: String) = Props(new Tactor(stuff))
}
