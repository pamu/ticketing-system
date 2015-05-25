# ticketing-system
Ticketing System

looks like heroku is down

To run the app offline git clone the app and change the username, password of the MySQL to some available user and password

Also create a database called as ```ticksys``` in the corresponding user.

models.DB.scala

```scala
    package models

    import java.net.URI

    //import scala.slick.driver.PostgresDriver.simple._
    import scala.slick.driver.MySQLDriver.simple._

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

       //change the credentials here
      lazy val db = Database.forURL(
        url = "jdbc:mysql://localhost/ticksys",
        driver = "com.mysql.jdbc.Driver",
        user="root",
        password="root"
      )

    }

```

Go to the project root directory and run

'''
    sbt run
'''

if sbt is not installed download it from http://www.scala-sbt.org/download.html

### Login

![UI](https://raw.githubusercontent.com/pamu/ticketing-system/master/images/login.png)

### Home

![UI](https://raw.githubusercontent.com/pamu/ticketing-system/master/images/home.png)

### New Ticket

![UI](https://raw.githubusercontent.com/pamu/ticketing-system/master/images/newticket.png)

### Comments

![UI](https://raw.githubusercontent.com/pamu/ticketing-system/master/images/comments.png)
