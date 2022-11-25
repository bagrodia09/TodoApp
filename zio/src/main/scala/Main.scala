import db.QuillContext

import route.TodoRoutes
import service.MigrationService
import service.TodoService
import zio._

object Main extends ZIOAppDefault {

  // start server
  override def run = ZIO
    .serviceWithZIO[TodoAppServer](
      _.startServer2
    )
    .provide(
      TodoAppServer.serverLayer,
      TodoRoutes.live,
      QuillContext.dataSourceLayer,
      configuration.live,
      TodoService.live,
      // MigrationService.layer,// this layer can and should be removed because it depends on DataSource Layer which is already provided by  QuillContext.dataSourceLayer
    )

  /** override def run1 = ZIO .serviceWithZIO[TodoAppServer]( _.startServer ) //
    * ZIO.serviceWithZIO[TodoAppServer](_.startServer) .provide( TodoAppServer.serverLayer,
    * TodoRoutes.live, QuillContext.dataSourceLayer, TodoService.live ,MigrationService.layer)
    */

}
