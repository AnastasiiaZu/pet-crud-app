package anastasiia.application

import services.PetService
import api.Api
import akka.actor.ActorSystem
import com.typesafe.config.{Config, ConfigFactory}

import scala.concurrent.ExecutionContext

object Main extends App {

  implicit val system: ActorSystem = ActorSystem("pet-crud-app")
  implicit val executionContext: ExecutionContext = system.dispatcher

  val config: Config = ConfigFactory.load

  private val petService = new PetService(config.getString("dbConnectorString"))
  val api = new Api(petService)

  WebServer(api.routes).start().stopOnReturn()
}
