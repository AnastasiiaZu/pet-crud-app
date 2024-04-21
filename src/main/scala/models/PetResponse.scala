package models

import utils.JsonFormatters.{UUIDFormat, ZonedDateTimeFormat}
import spray.json.DefaultJsonProtocol.{StringJsonFormat, jsonFormat3}
import spray.json.RootJsonFormat

import java.time.ZonedDateTime
import java.util.UUID

final case class PetResponse(id: UUID, name: String, createdAt: ZonedDateTime) {
}

object PetResponse {
  implicit val fmt: RootJsonFormat[PetResponse] = jsonFormat3 {
    PetResponse.apply
  }
}