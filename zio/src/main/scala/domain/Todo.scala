package domain
import zio.json._
final case class Todo(todoId:String,description:String)

object Todo{
    implicit val todoCodec: JsonCodec[Todo] = DeriveJsonCodec.gen[Todo]
}
