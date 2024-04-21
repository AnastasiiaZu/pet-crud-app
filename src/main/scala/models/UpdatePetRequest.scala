package anastasiia.application
package models

import spray.json.DefaultJsonProtocol.{StringJsonFormat, jsonFormat1}
import spray.json.RootJsonFormat

final case class UpdatePetRequest(name: String) {

  require(name.nonEmpty && name.length <= 16, "Name cannot be empty or longer than 16 characters")
}

object UpdatePetRequest {
  implicit val fmt: RootJsonFormat[UpdatePetRequest] = jsonFormat1 {
    UpdatePetRequest.apply
  }
}
