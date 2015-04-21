package controllers

import play.api.libs.ws.WS
import play.api.mvc.{Action, Controller}
import play.api.Play.current
import scala.concurrent.ExecutionContext.Implicits.global

/**
 * Created by sampsonjoliver on 21/04/15.
 * Illustrates the recover mechanism of Scala in Play
 */
object Recover extends Controller {
  // Recovering and error handling

  def throwableAction = Action.async {
    WS.url("http://www.example.comrade")
      .get()
      .map(result => Ok(result.body).as("text/plain"))
      .recover {
      case t: Exception =>
        InternalServerError("Uh oh, caught an exception: " + t.getLocalizedMessage)
      case _ =>
        InternalServerError("I really don't know what went wrong here")
    }
  }
}
