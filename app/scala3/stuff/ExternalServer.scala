package scala3.stuff

import javax.inject.Inject
import javax.inject.Singleton
import scala.util.Random
import spark.Spark._
import spark.Request
import spark.Response
import spark.Route
import scala.language.implicitConversions
import spark.route.RouteOverview
import play.Logger
import org.slf4j.LoggerFactory

//@Singleton
class ExternalServer {
  val log = LoggerFactory.getLogger(classOf[ExternalServer])

  type SparkRoute = (Request, Response) => Object
  implicit def toSparkRoute(route: SparkRoute): Route = new Route {
    def handle(req: Request, res: Response): Object = route(req, res)
  }

  val rnd = new Random()

  head("/", (req: Request, res: Response) => {
    res.status(204)
    ""
  })

  get("/random", (req: Request, res: Response) => {
    val n = rnd.nextInt(100) + 1
    res.`type`("text/plain")

    if (n > 50) {
      log.info(s"We crash with: $n")
      res.status(503)
      ""
    } else {
      log.info(s"Delivering $n")
      s"$n"
    }
  })

  RouteOverview.enableRouteOverview()
}