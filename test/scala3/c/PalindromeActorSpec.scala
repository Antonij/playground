package scala3.c

import scala.concurrent.duration.DurationInt
import scala.language.postfixOps

import org.scalatest.{ BeforeAndAfterAll, Finders, Matchers, WordSpecLike }

import com.typesafe.config.ConfigFactory

import akka.actor.{ ActorSystem, actorRef2Scala }
import akka.testkit.{ DefaultTimeout, ImplicitSender, TestKit }
import scala3.c.PalindromeActor.{ Check, CheckResult }

//http://doc.akka.io/docs/akka/2.4/scala/testing.html#akka-testkit
class PalindromeActorSpec
    extends TestKit(ActorSystem("test-system", ConfigFactory.parseString(PalindromeActorSpec.cfgString)))
    with DefaultTimeout
    with ImplicitSender
    with WordSpecLike
    with Matchers
    with BeforeAndAfterAll {

  val actorUnderTest = system.actorOf(PalindromeActor.props, "palindromeTest")

  override def afterAll {
    shutdown()
  }

  "An PalindromeActor" should {
    "respond with a positive check result" in {
      within(500 millis) {
        actorUnderTest ! Check("otto")

        expectMsg(CheckResult("otto : otto", true))
      }
    }

    "respond with a negative check result" in {
      within(500 millis) {
        actorUnderTest ! Check("foo")

        val answers = receiveWhile(500 millis) {
          case CheckResult(mirror, result) => (mirror, result)
        }

        answers.head should equal(("foo : oof", false))
      }
    }

    "do nothing on unknown message" in {
      within(500 millis) {
        actorUnderTest ! "stuff"
        expectNoMsg()
      }
    }

  }

}

object PalindromeActorSpec {
  val cfgString =
    """
    akka.loggers = ["akka.testkit.TestEventListener"]
    """
}