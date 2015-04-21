package controllers

import play.api.Logger
import play.api.mvc._
import scala.concurrent.ExecutionContext.Implicits.global

import scala.concurrent.Future

/**
 * Created by sampsonjoliver on 21/04/15.
 * Illustrates basic action composition using server logging as example.
 */
object Logging extends Controller {
  // Logging //
  object LoggingAction extends ActionBuilder[Request] {
    def invokeBlock[A](request: Request[A], block: (Request[A]) => Future[Result]) = {
      Logger.info("Logging some things")
      block(request)
    }
  }

  def loggingAction = LoggingAction { request =>
    Ok("Hooray")
  }

  case class Logging[A](action: Action[A]) extends Action[A] {
    def apply(request: Request[A]): Future[Result] = {
      Logger.info("Calling action")
      action(request)
    }
    lazy val parser = action.parser
  }

  def loggingComposableAction = Logging {
    Action { request =>
      Ok("Hooray")
    }
  }
}
