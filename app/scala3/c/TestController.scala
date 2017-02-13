package scala3.c

import scala.concurrent.{ ExecutionContext, Future }
import scala.concurrent.duration.DurationInt

import akka.actor.ActorSystem
import akka.pattern.ask
import akka.util.Timeout
import akka.util.Timeout.durationToTimeout
import javax.inject.{ Inject, Singleton }
import play.api.libs.json.Json
import play.api.mvc.{ Action, Controller }
import scala3.c.PalindromeActor.{ Check, CheckResult, checkResultFormat }

@Singleton
class TestController @Inject() (actorSystem: ActorSystem)(implicit exec: ExecutionContext) extends Controller {
  implicit val timeout: Timeout = 5.seconds
  val pille = actorSystem.actorOf(PalindromeActor.props)

  def test(toCheck: String) = Action.async {
    val res: Future[Any] = pille ? Check(toCheck)
    res.mapTo[CheckResult]
      .map(Json.toJson(_))
      .map(Ok(_).as(JSON))
  }

}
