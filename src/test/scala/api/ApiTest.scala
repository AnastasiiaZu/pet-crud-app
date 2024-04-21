package api

import akka.Done
import akka.actor.ActorSystem
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model._
import akka.http.scaladsl.testkit.ScalatestRouteTest
import models.{PetResponse, UpdatePetRequest}
import models.CreatePetRequest
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AsyncWordSpec
import services.PetService

import java.time.ZonedDateTime
import java.util.UUID
import scala.concurrent.Future

class ApiTest extends AsyncWordSpec with Matchers with ScalatestRouteTest with SprayJsonSupport {
  val testPetId: UUID = UUID.randomUUID()
  val testPetName: String = "Test Pet"
  val testPetNameTooLong: String = "Test Pet Test Pet Test Pet Test Pet Test Pet"
  val now: ZonedDateTime = ZonedDateTime.now()
  val nonExistingPetId = UUID.randomUUID()

  // Mock PetService for testing
  // todo: improvement use Mockito for mocking
  val mockPetService = new PetService("string") {
    override def createPet(name: String): Future[PetResponse] = Future.successful(PetResponse(testPetId, name, now))

    override def getPet(id: UUID): Future[Option[PetResponse]] =
      if (id == testPetId) Future.successful(Some(PetResponse(testPetId, testPetName, now)))
      else Future.successful(None)

    override def updatePet(id: UUID, name: String): Future[Option[PetResponse]] =
      if (id == testPetId) Future.successful(Some(PetResponse(testPetId, name, now)))
      else Future.successful(None)

    override def deletePet(id: UUID): Future[Done] =
      if (id == testPetId) Future.successful((Done))
      else Future.failed(new NoSuchElementException)
  }

  val api = new Api(mockPetService)(ActorSystem())

  "Api" should {
    "return 201 Created when creating a new pet" in {
      val createPetRequest = CreatePetRequest(testPetName)
      Post("/pets", createPetRequest) ~> api.routes ~> check {
        status shouldBe StatusCodes.Created
        responseAs[PetResponse].id shouldBe testPetId
        responseAs[PetResponse].name shouldBe testPetName
      }
    }

    // todo: add test for BadRequest for create and patch
    // todo: add test for InternalServerError response

    "return 200 OK with the correct pet when getting an existing pet" in {
      Get(s"/pets/$testPetId") ~> api.routes ~> check {
        status shouldBe StatusCodes.OK
        responseAs[PetResponse].id shouldBe testPetId
        responseAs[PetResponse].name shouldBe testPetName
      }
    }

    "return 404 Not Found when getting a non-existing pet" in {
      Get(s"/pets/$nonExistingPetId") ~> api.routes ~> check {
        status shouldBe StatusCodes.NotFound
        responseAs[String] shouldBe s"No pet with such id $nonExistingPetId"
      }
    }

    "return 200 OK with the updated pet when updating an existing pet" in {
      val updatedPetName = "Updated Test Pet"
      val updatePetRequest = UpdatePetRequest(updatedPetName)
      Patch(s"/pets/$testPetId", updatePetRequest) ~> api.routes ~> check {
        status shouldBe StatusCodes.OK
        responseAs[PetResponse].id shouldBe testPetId
        responseAs[PetResponse].name shouldBe updatedPetName
      }
    }

    "return 404 Not Found when updating a non-existing pet" in {
      val updatedPetName = "Updated Test Pet"
      val updatePetRequest = UpdatePetRequest(updatedPetName)
      Patch(s"/pets/$nonExistingPetId", updatePetRequest) ~> api.routes ~> check {
        status shouldBe StatusCodes.NotFound
      }
    }

    "return 204 No Content when deleting an existing pet" in {
      Delete(s"/pets/$testPetId") ~> api.routes ~> check {
        status shouldBe StatusCodes.NoContent
      }
    }
  }
}

