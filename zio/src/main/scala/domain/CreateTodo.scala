package domain
import zio.json._
final case class CreateTodo(todoId:String,description:String)

object  CreateTodo{
implicit val createTodoCodec: JsonCodec[CreateTodo] = DeriveJsonCodec.gen[CreateTodo]
}
