import route.TodoRoutes

import service._
import zhttp.http._
import zhttp.http.middleware.HttpMiddleware
import zhttp.service.Server
import zio._
import zio.Console._
import configuration._

final case class TodoAppServer(todoRoutes: TodoRoutes) {

  /** Composes the routes together, returning a single HttpApp.
    */
  val allRoutes: HttpApp[Any, Throwable] = todoRoutes.routes

  /** Logs the requests made to the server.
    *
    * It also adds a request ID to the logging context, so any further logging that occurs in the
    * handler can be associated with the same request.
    *
    * For more information on the logging, see: https://zio.github.io/zio-logging/
    */
  val loggingMiddleware: HttpMiddleware[Any, Nothing] =
    new HttpMiddleware[Any, Nothing] {

      override def apply[R1 <: Any, E1 >: Nothing](
        http: Http[R1, E1, Request, Response]
      ): Http[R1, E1, Request, Response] = Http.fromOptionFunction[Request] { request =>
        Random.nextUUID.flatMap { requestId =>
          ZIO.logAnnotate("REQUEST-ID", requestId.toString) {
            for {
              _ <- ZIO.logInfo(s"Request: $request")
              result <- http(request)
            } yield result
          }
        }
      }

    }

  def startServer: ZIO[FlywayConfig, Throwable, Unit] =
    for {
      port <- System.envOrElse("PORT", "8080").map(_.toInt)
      _ <- MigrationService.migrate
      fiber <- Server.start(port, allRoutes @@ Middleware.cors() @@ loggingMiddleware).forkDaemon
      _ <-
        printLine("Press Any Key to stop the rendezvous server") *> readLine.catchAll(e =>
          printLine(s"There was an error! ${e.getMessage}")
        ) *> fiber.interrupt &> MigrationService.reset
    } yield ()

  def startServer2: ZIO[FlywayConfig with ServerConfig, Throwable, Unit] =
    for {
      serverConfig <- ZIO.service[ServerConfig]
      fiber <-
        Server
          .start(
            serverConfig.port,
            allRoutes @@ Middleware.cors() @@ loggingMiddleware,
          )
          .forkDaemon
      _ <-
        printLine("Press Any Key to stop the rendezvous server") *> readLine.catchAll(e =>
          printLine(s"There was an error! ${e.getMessage}")
        ) *> fiber.interrupt &> MigrationService.reset
    } yield ()

}

/** Here in the companion object, we define the layer that will be used to create the server.
  */
object TodoAppServer {

  val serverLayer: ZLayer[TodoRoutes, Nothing, TodoAppServer] = ZLayer.fromFunction(
    TodoAppServer.apply _
  )

}
