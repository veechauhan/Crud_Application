package controllers

import javax.inject.Inject
import java.util.UUID
import play.api.mvc.{BaseController, ControllerComponents}
import play.api.libs.json.Json
import scala.concurrent.{ExecutionContext, Future}

class UsersController @Inject()(
  dao: daos.UsersDao,
  val controllerComponents: ControllerComponents
)(implicit ec: ExecutionContext) extends BaseController {

  def get = Action.async {
    dao.findAll()
      .map(Json.toJson(_))
      .map(Ok(_))
  }

  def getById(id: UUID) = Action.async {
    dao.findById(id)
      .map { optModel =>
        optModel
          .map(Json.toJson[models.User])
          .fold(NotFound(s"User not found: ${id}"))(Ok(_))
      }
  }

  val missingContentType = Future.successful(UnprocessableEntity("Expected 'Content-Type' set to 'application/json'"))
  val missingUserForm = Future.successful(UnprocessableEntity("Expected content to contain a user form"))

  def post = Action.async { req =>
    req.body.asJson
      .toRight(missingContentType)
      .flatMap(_.asOpt[models.UserForm].toRight(missingUserForm))
      .map { form =>
        val model = models.User.fromForm(form)

        dao.insert(model)
          .map { _ =>
            val json = Json.toJson(model)
            Created(json)
          }
      }
      .merge
  }

  def putById(id: UUID) = Action.async { req =>
    req.body.asJson
      .toRight(missingContentType)
      .flatMap(_.asOpt[models.UserForm].toRight(missingUserForm))
      .map { form =>
        dao.findById(id)
          .flatMap {
            case None => Future.successful((NotFound(s"User not found: ${id}")))
            case Some(found) =>
              val model = models.User.updated(found)(form)
              dao.update(model).map(_ => NoContent)
          }
      }
      .merge
  }

  def deleteById(id: UUID) = Action.async {
    dao.findById(id)
      .flatMap {
        case None => Future.successful((NotFound(s"User not found: ${id}")))
        case Some(found) => dao.delete(found).map(_ => NoContent)
      }
  }
}
