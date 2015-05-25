package models

import java.net.URI

//import scala.slick.driver.PostgresDriver.simple._
//import scala.slick.driver.MySQLDriver.simple._

import scala.slick.driver.H2Driver.simple._

/**
 * Created by pnagarjuna on 22/05/15.
 */
object DB {
  val uri = new URI(s"""postgres://qoxdlyackjozvb:KChWyPOvKuLwil7eT4GWDOt7fC@ec2-107-20-178-83.compute-1.amazonaws.com:5432/d6elqbqqchafak""")

  val username = uri.getUserInfo.split(":")(0)

  val password = uri.getUserInfo.split(":")(1)

  /*
  lazy val db = Database.forURL(
    driver = "org.postgresql.Driver",
    url = "jdbc:postgresql://" + uri.getHost + ":" + uri.getPort + uri.getPath, user = username,
    password = password
  ) */

  //test database

  /*
  lazy val db = Database.forURL(
    url = "jdbc:mysql://localhost/ticksys",
    driver = "com.mysql.jdbc.Driver",
    user="root",
    password="root"
  )*/

  lazy val db = Database.forURL("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", driver = "org.h2.Driver")

}
