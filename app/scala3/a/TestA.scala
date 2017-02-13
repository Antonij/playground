package scala3.a

import akka.actor.{ ActorSystem, Kill, PoisonPill, Props, actorRef2Scala }
import scala3.a.HelloActor.Greet
import scala3.a.HelloActor2.Greeting

object TestA {

  def main(args: Array[String]): Unit = {
    /* 1 *
     * get an actor
     */
    val actorSystem = ActorSystem("test-system")
    val anonSimpleActor = actorSystem.actorOf(Props[SimpleActor])
    
    val name = "simple"
    val simpleActor = actorSystem.actorOf(SimpleActor.props, name)

    simpleActor ! "stuff"
    simpleActor ! 1
    simpleActor ! true
    simpleActor ! (1, "1")

    /* 2 *
     * actors are unique
     */
    try {
      val otherSimpleActor = actorSystem.actorOf(SimpleActor.props, name)
      otherSimpleActor ! "Whut?!"
    } catch {
      case e: Exception => println(e.toString())
    }

    /* 3 *
     * do not forget to cleanup
     * note the difference
     */
    simpleActor ! Kill
//    simpleActor ! PoisonPill

    /* 4 *
     * giving your actors something one the way
     */
    val helloActor = actorSystem.actorOf(HelloActor.props("Hi there"), name = "helloCtor")
    helloActor ! Greet("John")
    helloActor ! "Bob"
    
    val helloActor2 = actorSystem.actorOf(HelloActor2.props, name = "helloBecome")
    // what happens to an unhandled message?
    helloActor2 ! Greet("Bill")
    helloActor2 ! Greeting("Greetings")
    helloActor2 ! Greet("Jim")
    helloActor2 ! PoisonPill
    
    while (true) { Thread.sleep(10000) }
  }

}