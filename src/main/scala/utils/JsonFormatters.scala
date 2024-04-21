package utils

import spray.json.{DeserializationException, JsString, JsValue, JsonFormat}

import java.time.ZonedDateTime
import java.util.UUID

object JsonFormatters {

  implicit object UUIDFormat extends JsonFormat[UUID] {
    def write(uuid: UUID) = JsString(uuid.toString)

    def read(value: JsValue) = {
      value match {
        case JsString(uuid) => UUID.fromString(uuid)
        case _ => throw DeserializationException("Expected hexadecimal UUID string")
      }
    }
  }

  implicit object ZonedDateTimeFormat extends JsonFormat[ZonedDateTime] {
    def write(time: ZonedDateTime) = JsString(time.toString)

    def read(value: JsValue) = {
      value match {
        case JsString(time) => ZonedDateTime.parse(time)
        case _ => throw DeserializationException("Expected ZonedDateTime string")
      }
    }
  }

}
