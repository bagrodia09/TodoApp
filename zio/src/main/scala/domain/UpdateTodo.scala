package domain
import zio.json._
final case class UpdateTodo(todoId:String,description:String)

object  UpdateTodo{
    implicit val updateTodoCodec: JsonCodec[UpdateTodo] = DeriveJsonCodec.gen[UpdateTodo]
}
