package anastasiia.application
package api

import services.PetService

import akka.http.scaladsl.model.StatusCodes.{Created, NoContent, NotFound, OK}
import akka.http.scaladsl.server.Directives.{complete, path, pathPrefix, post}
import akka.http.scaladsl.server.{PathMatcher1, Route}
import akka.http.scaladsl.server.Directives._
import models.{CreatePetRequest, UpdatePetRequest}
import akka.actor.ActorSystem
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import models.CreatePetRequest.fmt
import akka.http.scaladsl.model.StatusCodes

import java.util.UUID
import scala.util.{Failure, Success}

class Api(petService: PetService) (
  implicit val system: ActorSystem
 ) extends SprayJsonSupport {


  private val petIdMatcher: PathMatcher1[UUID] = JavaUUID

  def routes: Route = pathPrefix("pets") {
    post {
      entity(as[CreatePetRequest]) { req =>
        onComplete(petService.createPet(req.name)) {
          case Failure(_) => complete(StatusCodes.InternalServerError)
          case Success(pet) => complete(Created, pet)
        }
      }
    } ~ path(petIdMatcher) { id =>
        get {
          onComplete(petService.getPet(id)) {
            case Failure(_) => complete(StatusCodes.InternalServerError)
            case Success(None) => complete(404, s"No pet with such id $id")
            case Success(Some(pet)) => complete(OK, pet)
          }
        complete(petService.getPet(id))
      } ~ patch {
          entity(as[UpdatePetRequest]) { req =>
            onComplete(petService.updatePet(id, req.name)) {
              case Failure(_) => complete(StatusCodes.InternalServerError)
              case Success(None) => complete(404, s"No pet with such id $id")
              case Success(Some(pet)) => complete(OK, pet)
            }
          }
        } ~ delete {
          onComplete(petService.deletePet(id)) {
            case Failure(_) => complete(StatusCodes.InternalServerError)
            case Success(_) => complete(StatusCodes.NoContent)
          }
        }
    }
  }
}
