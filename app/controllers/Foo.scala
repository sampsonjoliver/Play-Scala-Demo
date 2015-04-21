package controllers

import play.api.Logger
import play.api.libs.json.{JsObject, Json}
import play.api.libs.ws.{WSResponse, WS}
import play.api.mvc._

import play.api.Play.current
import util.AuthTools
import scala.concurrent.ExecutionContext.Implicits.global

import scala.concurrent.Future

/**
 * Created by sampsonjoliver on 8/04/15.
 * Illustrates the skeleton of a basic RESTful web service
 */
object Foo extends Controller {
  def show(id: Int) = Action { request =>
    Ok("")
  }

  def list(page: Option[Int]) = Action { request =>
    Ok("")
  }

  def update(id: Int) = Action { request =>
    Ok("")
  }

  def add() = Action { request =>
    Ok("")
  }

  def delete(id: Int) = Action { request =>
    Ok("")
  }
}
