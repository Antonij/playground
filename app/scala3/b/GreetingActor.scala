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

  val upper = context.actorOf(CapitalizationActor.props, "capitalizer")

  def receive = {
    case HiMyNameIs(name) => upper forward Capitalize(name)
    case msg => log.info(s"greeting actor got unknown message: $msg")
  }

  override def postStop(): Unit = log.info("GreetingActor was terminated")
}
object GreetingActor {
  val props = Props[GreetingActor]
  case class HiMyNameIs(name: String)
}


class CapitalizationActor extends Actor with ActorLogging {
  import CapitalizationActor._

  val picker = context.actorOf(AddressPickActor.props, name = "addressPicker")

  def receive = {
    case Capitalize(str) =>
      picker forward Name(capitalize(str))
    case CapitalizeAsk(str) =>
      sender ! capitalize(str)
    case CapitalizeAskFailHandle(str) =>
      // functional try to catch an exception and avoid ask timeout so the sender can see what went wrong where
      val response = Try(capitalize(str)) match {
        case Success(capitalized) => capitalized
        case Failure(exception) => Status.Failure(exception)
      }
      sender ! response
  }

  // What happens if something goes wrong?
  // Ask timeout happens. The sender only gets an answer if you reply.
  private def capitalize(str: String): String = if (str.startsWith("?")) throwUp() else str.capitalize

  private def throwUp(): Nothing = throw new RuntimeException("HUÄRGH")

//  override def preStart(): Unit = log.info("CapitalizationActor was started")
  override def postStop(): Unit = log.info("CapitalizationActor was stopped")
}
object CapitalizationActor {
  val props = Props[CapitalizationActor]

  case class Capitalize(str: String)
  case class CapitalizeAsk(str: String)
  case class CapitalizeAskFailHandle(str: String)
}


class AddressPickActor extends Actor with ActorLogging {
  import AddressPickActor._

  def receive = {
    case Name(name) =>
      val address = addresses(rnd.nextInt(addresses.length))
      sender ! s"Hi $name, new $address."
  }

//  override def preStart(): Unit = log.info(s"AddressPickActor was started")
  override def postStop(): Unit = log.info("AddressPickActor was stopped")
}
object AddressPickActor {
  case class Name(firstname: String)
  val props = Props[AddressPickActor]

  val rnd = new Random()
  val addresses = List("friend", "buddy", "pal", "guy", "dude")
}