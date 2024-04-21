package models

import spray.json.DefaultJsonProtocol.{StringJsonFormat, jsonFormat1}
import spray.json.RootJsonFormat

final case class CreatePetRequest(name: String) {

  require(name.nonEmpty && name.length <= 16, "Name cannot be empty or longer than 16 characters")
}

object CreatePetRequest {
  implicit val fmt: RootJsonFormat[CreatePetRequest] = jsonFormat1 {
    CreatePetRequest.apply
  }
}


