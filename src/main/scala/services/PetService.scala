package services

import akka.Done
import storage.DataBase.pets
import models.PetResponse
import java.time.ZonedDateTime
import java.util.UUID
import scala.concurrent.{ExecutionContext, Future}

class PetService(dbConnectorString: String, var pets: Map[UUID, (String, ZonedDateTime)] = pets)(
  implicit val ec: ExecutionContext
) {
  // todo: add real connection once using an external DB
  println(dbConnectorString)

  // Future wrapper imitates an async call to a DB

  def createPet(name: String): Future[PetResponse] = {
    def TimeGenerator: ZonedDateTime = ZonedDateTime.now()
    def IdGenerator: UUID = UUID.randomUUID()
    val createdAt = TimeGenerator
    val newUUID = IdGenerator
    val newPet = newUUID -> (name, createdAt)
    pets = pets + newPet

    Future.successful(PetResponse(newUUID, name, createdAt))
  }

  def getPet(id: UUID): Future[Option[PetResponse]] =
    Future.successful(pets.get(id).map { pet =>
      PetResponse(id, pet._1, pet._2)
    })

  def updatePet(id: UUID, newName: String): Future[Option[PetResponse]] =
    Future.successful(pets.get(id).map { pet =>
      val updatedPet = id -> (newName, pet._2)
      pets = pets.removed(id) + updatedPet
      PetResponse(id, newName, pet._2)
  })


  def deletePet(id: UUID): Future[Done] =
    pets.get(id) match {
      case Some(_) =>
        pets = pets.removed(id)
        Future.successful(Done)
      case None => Future.successful(Done)
    }
  }
