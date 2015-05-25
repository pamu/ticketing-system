# ticketing-system
Ticketing System

looks like heroku is down

Added H2 (in memory SQL database)

You can run the project on your local machine.

To run the project

In the project root directory run

```
    sbt run
```

now open
```
    http://localhost:9000/
```

if sbt is not installed download it from http://www.scala-sbt.org/download.html

# Screen Shots of Ticketing system

### Login

![UI](https://raw.githubusercontent.com/pamu/ticketing-system/master/images/login.png)

### Home

![UI](https://raw.githubusercontent.com/pamu/ticketing-system/master/images/home.png)

### New Ticket

![UI](https://raw.githubusercontent.com/pamu/ticketing-system/master/images/newticket.png)

### Comments

![UI](https://raw.githubusercontent.com/pamu/ticketing-system/master/images/comments.png)

### Notifications

![UI](https://raw.githubusercontent.com/pamu/ticketing-system/master/images/notifications.png)

# Interesting code snippets

The code that powers notifications

### Notifications Action

```scala
    def notifications() = withFUser {user => implicit request => {
        implicit val timeout = Timeout(5 seconds)
        val f = (Global.nhandler ? NHandler.Stream).mapTo[Enumerator[JsValue]]
        f.map(en => {
          Ok.chunked(en &> NHandler.filter(user.id.get) &> EventSource()).as(EVENT_STREAM)
        }).recover{case throwable: Throwable => BadRequest}
      }}
```

### Notifications Actor

```scala
    package actors

    import akka.actor.{ActorLogging, Actor}
    import play.api.libs.iteratee.{Enumeratee, Concurrent}
    import play.api.libs.json.{Json, JsValue}

    import play.api.libs.concurrent.Execution.Implicits.defaultContext
    /**
     * Created by pnagarjuna on 24/05/15.
     */
    object NHandler {
      case class Message(to: Long, msg: String)
      case object Stream
      case object Stop
      def filter(userId: Long) = Enumeratee.filter[JsValue]{  json: JsValue =>
        userId == (json\ "to").as[Long]
      }
    }

    class NHandler extends Actor with ActorLogging {
      import NHandler._
      val (en, out) = Concurrent.broadcast[JsValue]
      override def receive = {
        case Message(to, msg) =>
          log.info("{}", Message(to, msg))
          out.push(Json.obj("to" -> to, "msg" -> msg))
        case Stream => sender ! en
        case Stop =>
          out end()
          context stop self
      }
    }
```
