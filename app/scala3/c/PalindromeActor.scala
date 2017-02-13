package scala3.c

import akka.actor.{ Actor, ActorLogging, Props, actorRef2Scala }
import play.api.libs.json.Json
import scala3.c.PalindromeActor.{ Check, CheckResult }
import akka.actor.Terminated
import akka.actor.Status

object PalindromeActor {
  case class Check(text: String)
  case class CheckResult(mirror: String, result: Boolean)
  
  implicit val checkResultFormat = Json.format[CheckResult]
  
  val props = Props[PalindromeActor]
}

class PalindromeActor extends Actor with ActorLogging {
  
  def receive = {
    case Check(textToCheck) =>
      log.info(s"checking: '$textToCheck'")
      val reverse: String = textToCheck.reverse
      sender ! CheckResult(s"$textToCheck : $reverse", textToCheck == reverse)
    
    case x =>
      log.info(s"got something i dont understand: $x")
  }
  
}
