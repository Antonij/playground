package controllers

import akka.actor.ActorSystem
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import scala.concurrent.duration._
import akka.pattern.ask
import PalindromeActor._
import akka.util._

object PlayAround {
	implicit val timeout: Timeout = 5.seconds //> timeout  : akka.util.Timeout = Timeout(5 seconds)
  val sys = ActorSystem()                         //> sys  : akka.actor.ActorSystem = akka://default
  
  val pa = sys.actorOf(PalindromeActor.props, "pillepalle")
                                                  //> pa  : akka.actor.ActorRef = Actor[akka://default/user/pillepalle#1540283470]
                                                  //| /
  //val stuff = pa ? Check("stuff")


}