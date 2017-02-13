package scala3.d

import scala.concurrent.ExecutionContext
import scala.concurrent.duration.DurationInt

import akka.actor.ActorSystem
import akka.pattern.ask
import akka.util.Timeout
import akka.util.Timeout.durationToTimeout
import javax.inject.{ Inject, Singleton }
import play.api.libs.json.Json
import play.api.mvc.{ Action, Controller }
import scala3.c.PalindromeActor._
import scala.concurrent.Future
import akka.actor.Props

@Singleton
class IoExampleController @Inject() (actorSystem: ActorSystem) extends Controller {
  implicit val exampleExecutionContext: ExecutionContext = actorSystem.dispatchers.lookup("blocking-io-dispatcher")
  //val myActor = context.actorOf(Props[MyActor].withDispatcher("blocking-io-dispatcher"), "example")
  //http://doc.akka.io/docs/akka/current/scala/dispatchers.html#Types_of_dispatchers
  
  implicit val timeout: Timeout = 5.seconds
  
  val pille = actorSystem.actorOf(Props.empty)

  def test(toCheck: String) = Action.async {
    val res: Future[Any] = pille ? Check(toCheck)
    res.mapTo[CheckResult]
      .map(Json.toJson(_))
      .map(Ok(_).as(JSON))
      
  }

  private def stuff() {
//    val f = Status.Failure
  }
  
}
