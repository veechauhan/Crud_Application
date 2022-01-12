import org.specs2.mutable.Specification


class UsersControllerTestingWithDatabases extends Specification {
  class NotTested {
    {
      import play.api.db.Databases

      val database = Databases(
        driver = "com.mysql.jdbc.Driver",
        url = "jdbc:mysql://localhost/test",
        name = "postgres",
        config = Map(
          "name" -> "postgres",
          "password" -> "12345"
        )
      )
      database.shutdown()
    }
  }

  import play.api.db.evolutions._
  import play.api.db.Database
  import play.api.db.Databases

  def withMyDatabase[T](block: Database => T) = {
    Databases.withInMemory(
      urlOptions = Map(
        "MODE" -> "MYSQL"
      ),
      config = Map(
        "logStatements" -> true
      )
    ) { database =>
      Evolutions.withEvolutions(
        database,
        SimpleEvolutionsReader.forDefault(
          Evolution(
            1,
            "create table postgres (id bigint not null, name varchar(255), password varchar(255);",
            "drop table postgres;"
          )
        )
      ) {
        block(database)
      }
    }
  }
}
