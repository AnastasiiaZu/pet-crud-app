package services

import akka.Done
import models.PetResponse
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import java.time.ZonedDateTime
import java.util.UUID
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class PetServiceTest extends AnyWordSpec with Matchers {

  val petName = "Test Pet"
  val createdAt = ZonedDateTime.now()
  val petId = UUID.randomUUID()
  val nonExistingPetId = UUID.randomUUID()
  val petService = new PetService("mockDbConnector")

  "PetService" should {
    "create a new pet" in {
      val futureResult: Future[PetResponse] = petService.createPet(petName)

      futureResult.map { petResponse =>
        petResponse.id shouldBe petId
        petResponse.name shouldBe petName
        petResponse.createdAt shouldBe createdAt
      }
    }

    "get an existing pet" in {
      val petService = new PetService("mockDbConnector")
      val futureResult: Future[Option[PetResponse]] = petService.getPet(petId)

      futureResult.map { maybePetResponse =>
        maybePetResponse shouldBe Some(PetResponse(petId, petName, createdAt))
      }
    }

    "return None for a non-existing pet" in {
      val futureResult: Future[Option[PetResponse]] = petService.getPet(nonExistingPetId)

      futureResult.map { maybePetResponse =>
        maybePetResponse shouldBe None
      }
    }

    "update an existing pet" in {
      val updatedPetName = "Updated Pet"
      val futureResult: Future[Option[PetResponse]] = petService.updatePet(petId, updatedPetName)

      futureResult.map { maybePetResponse =>
        maybePetResponse shouldBe Some(PetResponse(petId, updatedPetName, createdAt))
      }
    }

    "delete an existing pet" in {
      val futureResult: Future[Done] = petService.deletePet(petId)

      futureResult.map { _ =>
        // Deletion should succeed, no need to assert anything
        succeed
      }
    }

    "delete a non-existing pet" in {
      val futureResult: Future[Done] = petService.deletePet(nonExistingPetId)

      futureResult.map { _ =>
        // Deletion should succeed even for non-existing pets, no need to assert anything
        succeed
      }
    }
  }
}
