package anastasiia.application

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route

import scala.concurrent.{ExecutionContext, Future}
import scala.io.StdIn

case class WebServer(route: Route, host: String = "localhost", port: Int = 8080)(
  implicit val system: ActorSystem,
  ec: ExecutionContext
) {

  def start(): RunningServer = {
    new RunningServer(Http().newServerAt(host, port).bindFlow(route))
  }

  class RunningServer(bindingFuture: Future[Http.ServerBinding]) {
    def stop(): Unit = {
      bindingFuture
        .flatMap(_.unbind())
        .onComplete(_ => system.terminate())
    }

    def stopOnReturn(): Unit = {
      println(s"Server online at http://localhost:$port/\nPress RETURN to stop...")
      StdIn.readLine()
      stop()
    }
  }

}

