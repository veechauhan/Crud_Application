package daos

import java.time.LocalDateTime
import java.util.UUID
import javax.inject.{Inject, Singleton}
import play.api.db.slick._
import scala.concurrent.Future
import slick.jdbc.{JdbcProfile, GetResult}

import helpers.slick.dbioaction._
import helpers.slick.jdbc._

trait UsersDao {
  def findAll(): Future[Seq[models.User]]
  def findById(id: UUID): Future[Option[models.User]]
  def insert(user: models.User): Future[Unit]
  def update(user: models.User): Future[Unit]
  def delete(user: models.User): Future[Unit]
}

@Singleton
class UsersDaoMap extends UsersDao {
  val map = collection.mutable.Map.empty[UUID, models.User]

  def findAll(): Future[Seq[models.User]] = Future.successful(map.values.toSeq)
  def findById(id: UUID): Future[Option[models.User]] = Future.successful(map.get(id))
  def insert(user: models.User): Future[Unit] = Future.successful(map.update(user.id, user))
  def update(user: models.User): Future[Unit] = Future.successful(map.update(user.id, user))
  def delete(user: models.User): Future[Unit] = Future.successful(map.remove(user.id))
}

class UserDaoSlick @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) extends UsersDao with HasDatabaseConfigProvider[JdbcProfile] {
  import profile.api._

  private val table = TableQuery[UsersTable]
  private class UsersTable(tag: Tag) extends Table[models.User](tag, "users") {
    def id = column[UUID]("id", O.PrimaryKey)
    def name = column[String]("name")
    def password = column[String]("password")

    def * = (id, name, password).mapTo[models.User]
  }

  def findAll(): Future[Seq[models.User]] = db.run(table.result)
  def findById(id: UUID): Future[Option[models.User]] = db.run(table.filter(_.id === id).result.headOption)
  def insert(user: models.User): Future[Unit] = db.run((table += user).void)
  def update(user: models.User): Future[Unit] = db.run(table.filter(_.id === user.id).update(user).void)
  def delete(user: models.User): Future[Unit] = db.run(table.filter(_.id === user.id).delete.void)
}

class UserDaoSlickPlainSql @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) extends UsersDao with HasDatabaseConfigProvider[JdbcProfile] {
  import profile.api._

  implicit val getUserResult: GetResult[models.User] = GetResult(r => models.User(r.<<, r.<<, r.<<))

  def findAll(): Future[Seq[models.User]] = db.run(sql"""
    select id, name, password
    from users
  """.as[models.User])

  def findById(id: UUID): Future[Option[models.User]] = db.run(sql"""
    select id, name, password
    from users
    where id = ${id}
  """.as[models.User].headOption)

  def insert(user: models.User): Future[Unit] = db.run(sqlu"""
    insert into users(id, name, password)
    values (${user.id}, ${user.name}, ${user.password})
  """.void)

  def update(user: models.User): Future[Unit] = db.run(sqlu"""
    update users
    set name = ${user.name},
      password = ${user.password}
    where id = ${user.id}
  """.void)

  def delete(user: models.User): Future[Unit] = db.run(sqlu"""
    delete from users
    where id = ${user.id}
  """.void)
}
