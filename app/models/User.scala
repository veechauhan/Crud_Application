package models

import java.util.UUID
import play.api.libs.json.{Json, OWrites}

case class User(
  id: UUID,
  name: String,
  password: String,
)

object User {
  implicit def userPlayJsonWrites: OWrites[User] = Json.writes[User]

  def fromForm(form: UserForm): User =
    User(
      id = UUID.randomUUID,
      name = form.name,
      password = form.password,
    )

  def updated(self: User)(form: UserForm): User =
    self.copy(
      name = form.name,
      password = form.password,
    )

  def tupled(tuple: (UUID, String, String)): User =
    (apply _).tupled(tuple)
}

case class UserForm(
  name: String,
  password: String,
)

object UserForm {
  implicit def userFormPlayJsonReads = Json.reads[UserForm]

  def fromModel(model: User): UserForm =
    UserForm(
      name = model.name,
      password = model.password,
    )
}
