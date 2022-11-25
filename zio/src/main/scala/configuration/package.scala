import zio.TaskLayer

import zio.config.magnolia.descriptor
import zio.config.typesafe.TypesafeConfig
import zio.config.ConfigDescriptor._
import zio.config.syntax._

package object configuration {
  private type AllConfig = AppConfig with FlywayConfig with ServerConfig

  private final val Root = "todo"

  private final val Descriptor = descriptor[AppConfig]

  private val appConfig = TypesafeConfig.fromResourcePath(nested(Root)(Descriptor))

  val live: TaskLayer[AllConfig] =
    appConfig >+>
      appConfig.narrow(_.flyway) >+> appConfig.narrow(_.server)

}
