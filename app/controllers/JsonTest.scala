package controllers

import play.api.libs.json.{Json, JsObject}
import play.api.libs.ws.{WS, WSResponse}
import play.api.mvc.{Action, Controller}
import util.AuthTools
import play.api.Play.current
import scala.concurrent.ExecutionContext.Implicits.global


/**
 * Created by sampsonjoliver on 21/04/15.
 * Illustrates complex tasks using json data pipelines, authorisation/authentication/permission
 * systems, and asynchronous action composition workflows
 */
object JsonTest extends Controller with AuthTools {
  
  def parsingAction = Action(parse.json) { request =>
    Ok(request.body)
  }

  // Authentication //

  def authAction = AuthenticatedAction { request =>
    Ok("Hello " + request.username)
  }

  def asyncAuthAction = AuthenticatedAction.async { request =>
    val credentials = (request.username, request.password)
    val futureJsUserArray = WS.url("http://www.json-generator.com/api/json/get/cfLxEnRoAy?indent=2").get()

    futureJsUserArray.map{ jsResponse =>
      val userEntry = getUserFromJs(jsResponse, credentials) getOrElse Json.obj("" -> "")

      Ok(userEntry).as("application/json")
    }
  }

  def permittedAction =
    (AuthenticatedAction andThen PermissionAction(Some(List("ADMIN_ACCESS", "READ_ACCESS"))) andThen AuthorisedAction)
    { request =>
      Ok("This should only succeed if you were successfully authorised")
    }
}
