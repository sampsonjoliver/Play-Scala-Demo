package controllers

import play.api.libs.ws.WS
import play.api.mvc.{Controller, Action}

import scala.concurrent.Future
import play.api.Play.current
import scala.concurrent.ExecutionContext.Implicits.global

/**
 * Created by sampsonjoliver on 21/04/15.
 * Illustrates asynchronous workflows in the Play framework using WebServices as an
 * example data source.
 */
object Async extends Controller {

  // Async
  def asyncAction = Action.async { request =>
    val futureJsUserArray = WS.url("http://www.json-generator.com/api/json/get/cfLxEnRoAy?indent=2").get()

    futureJsUserArray.map{ jsResponse =>
      Ok(jsResponse.body).as("application/json")
    }
  }

  def flatMapSample = {
    val stringArr = "I am a string".split(" ") // Array(I, am, a, string)
    val nestedCharArr = stringArr.map { stringItem =>
        stringItem.toCharArray.toList // maps "string" to [s, t, r, i, n, g]
      } // Array(List(I), List(a, m), List(a), List(s, t, r, i, n, g))
    val flattenedCharArr = stringArr.flatMap { stringItem =>
        stringItem.toCharArray.toList
      } // Array(I, a, m, a, s, t, r, i, n, g)
  }

  /* Doesn't work, as expected
  def asyncAction2 = Action.async { request =>
    val futureResponse = WS.url("www.example.com").get()
    val futureResponse2 = futureResponse.map { response =>
      WS.url("www.foobar.com").get()
    } // returns a Future[Future[WSResponse]]

    futureResponse2.map{ stillAFutureResponse =>
      stillAFutureResponse.map { notAFutureResponse =>
        Ok(notAFutureResponse.body).as("text/plain")
      }
    }
  }
  */

  def asyncAction2 = Action.async { request =>
    val futureResponse = WS.url("http://www.example.com").get()

    val futureResponse2 = futureResponse.flatMap { response =>
      WS.url("http://www.foo.com").get()
    } // returns a Future[Response]

    futureResponse2.map{ notAFutureResponse =>
      Ok(notAFutureResponse.body).as("text/html")
    }
  }

  def asyncAction3 = Action.async { request =>
    val futureA = WS.url("http://www.example.com").get()
    val futureB = WS.url("http://www.foo.com").get()
    val futureC = WS.url("http://www.bar.com").get()

    Future.sequence(Seq(futureA, futureB, futureC)).map{ futureSequence =>
      // Concatenate the response bodies
      Ok(futureSequence.map(_.body).reduce(_ + _)).as("text/html")
    }
  }

  def asyncAction4 = Action.async { request =>
    val allMyFutures = for {
      a <- WS.url("http://www.json-generator.com/api/json/get/bUSnjMfGWa?indent=2").get()
      b <- WS.url(a.body.substring(1, a.body.length - 2)).get() // returns "http://www.example.com"
      c <- WS.url(b.body.substring(1, a.body.length - 2)).get() // returns content of example.com
      d <- WS.url("http://www.example.com").get()
    } yield a.body + b.body + c.body + d.body

    allMyFutures.map(Ok(_).as("text/html"))
  }
}
