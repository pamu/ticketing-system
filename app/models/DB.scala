package models

import java.net.URI

import scala.slick.driver.PostgresDriver.simple._
/**
 * Created by pnagarjuna on 22/05/15.
 */
object DB {
  val uri = new URI(s"""postgres://qoxdlyackjozvb:KChWyPOvKuLwil7eT4GWDOt7fC@ec2-107-20-178-83.compute-1.amazonaws.com:5432/d6elqbqqchafak""")

  val username = uri.getUserInfo.split(":")(0)

  val password = uri.getUserInfo.split(":")(1)

  lazy val db = Database.forURL(
    driver = "org.postgresql.Driver",
    url = "jdbc:postgresql://" + uri.getHost + ":" + uri.getPort + uri.getPath, user = username,
    password = password
  )
}
