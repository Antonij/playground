package scala3.b

import scala.language.postfixOps
import scala.util.Random

import akka.actor.{ Actor, ActorLogging, Props, actorRef2Scala }
import scala3.b.AddressPickActor.Name
import scala3.b.GreetingActor.HiMyNameIs
import scala.util.Try
import scala.util.Success
import scala.util.Failure
import akka.actor.Status
import akka.actor.Terminated


/**
 * Just a filler to have another forwarder in the chain.
 *
 */
class GreetingActor extends Actor with ActorLogging {
  import CapitalizationActor._
  import GreetingActor._

  implicit val executionContext = context.dispatcher
  val upper = context.actorOf(CapitalizationActor.props, "capitalizer")
  context.watch(upper)

  def receive = {
    case HiMyNameIs(name) => upper forward Capitalize(name)
    case AlsoHiMyNameIs(name) => upper forward CapitalizeAsk(name)
    case Terminated => log.info(s"watched $upper died")
    case msg => log.info(s"greeting actor got unknown message: $msg")
  }
}
object GreetingActor {
  val props = Props[GreetingActor]
  case class HiMyNameIs(name: String)
  case class AlsoHiMyNameIs(name: String)
}


class CapitalizationActor extends Actor with ActorLogging {
  import CapitalizationActor._

  val picker = context.actorOf(AddressPickActor.props, name = "addressPicker")

  def receive = {
    // deliver failure to sender to avoid ask timeout so you can see what went wrong where
//    case Capitalize(str) =>

    case Capitalize(str) =>
      picker forward Name(capitalize(str))
    case CapitalizeAsk(str) =>
      val response = capitalize(str)
      // functional try to catch an exception and avoid ask timeout
//      val response = Try(capitalize(str)) match {
//        case Success(capitalized) =>
//          capitalized
//        case Failure(exception) =>
//          Status.Failure(exception)
//      }
      sender ! response
  }

  // What happens if something goes wrong?
  // Ask timeout happens. The sender only gets an answer if you reply.
  private def capitalize(str: String): String = if (str.startsWith("?")) throwUp() else str.capitalize

  private def throwUp(): Nothing = throw new RuntimeException("HUÄRGH")
}
object CapitalizationActor {
  val props = Props[CapitalizationActor]

  case class Capitalize(str: String)
  case class CapitalizeAsk(str: String)
}


class AddressPickActor extends Actor with ActorLogging {
  import AddressPickActor._

  def receive = {
    case Name(name) =>
      val address = addresses(rnd.nextInt(addresses.length))
      sender ! s"Hi $name, new $address."
  }
}

object AddressPickActor {
  case class Name(firstname: String)
  val props = Props[AddressPickActor]

  val rnd = new Random()
  val addresses = List("friend", "buddy", "pal", "guy", "dude")
}