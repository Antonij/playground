package scala3.a

import akka.actor.ActorLogging
import akka.actor.Actor
import akka.actor.Props
import scala3.a.HelloActor._
import scala3.a.HelloActor2._
import akka.actor.DiagnosticActorLogging
import akka.event.Logging.MDC

class HelloActor(greeting: String) extends Actor with ActorLogging {

  def receive: Receive = {
    case Greet(name) => log.info(s"$greeting $name.")
    case something => log.info(s"What am i supposed to do with $something?")
  }
  
}

object HelloActor {
  // use only outside of actors
  def props(greeting: String) = Props(new HelloActor(greeting))
  
  // messages are our interface
  case class Greet(name: String)
}

//http://doc.akka.io/docs/akka/current/scala/logging.html#logging-thread-akka-source-and-actor-system-in-mdc
class HelloActor2 extends Actor with DiagnosticActorLogging {

  private var greeting: String = _

  /*
   * initial behaviour
   */
  def receive = {
    case Greeting(greetingToUse) =>
      greeting = greetingToUse
      context.become(behavior = greeter, discardOld = true) //become someone completely different and forget your past
  }

  /*
   * greeter behaviour
   */
  def greeter: Receive = {
    case Greet(name) => log.info(s"$greeting $name.")
    case something => log.info(s"What am i supposed to do with $something?")
  }

  /*
   * 
   */
  override def mdc(currentMessage: Any): MDC = {
    currentMessage match {
      case gr: Greet => Map("greetee" -> gr.name)
      case _ => Map()
    }
  }
  
//  override def unhandled(message: Any) = {
//    log.info(s"Cannot handle message: $message")
//    // default behaviour publishes to system wide event stream
//    super.unhandled(message)
//  }
}

object HelloActor2 {
  val props = Props[HelloActor2]
  case class Greeting(greeting: String)
}
