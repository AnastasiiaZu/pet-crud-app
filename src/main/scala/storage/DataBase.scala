package anastasiia.application

import java.time.ZonedDateTime
import java.util.UUID

object DataBase {

  val pets: Map[UUID, (String, ZonedDateTime)] =
    Map(
      UUID.fromString("d2e9ba66-a805-4743-9222-f3522a1ebc4b") ->
        ("Kapybara", ZonedDateTime.parse("2024-04-21T10:29:49.843961+02:00[Europe/Berlin]")),
      UUID.fromString("f934f4ae-0ba5-44c2-b190-5177b7ad7094") ->
        ("Python", ZonedDateTime.parse("2024-04-21T11:29:49.843961+02:00[Europe/Berlin]"))
    )
}
