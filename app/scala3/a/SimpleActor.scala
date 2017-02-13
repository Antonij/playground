package scala3.a

import akka.actor.ActorLogging
import akka.actor.Actor
import akka.actor.Props

//http://doc.akka.io/docs/akka/current/java/logging.html
class SimpleActor extends Actor with ActorLogging {

  /*
   * get a message, do a thing
   */
  def receive = {
    case str: String => log.info(s"Got String: $str")
    case x => log.info(s"Got: $x") //actor path in log message
  }

  /*
   * can be used for initialisation
   */
  override def preStart(): Unit = {
    log.info("pre start..")
  }

  /*
   * can be used for cleanup
   */
  override def postStop(): Unit = {
    log.info("post stop..")
  }

}

object SimpleActor {
  val props = Props[SimpleActor]
}
