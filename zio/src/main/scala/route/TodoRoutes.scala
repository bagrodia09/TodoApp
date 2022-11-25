package route

import service.TodoService
import zhttp.http._
import zio._
import zio.json._
import domain.AppError._
import domain._
/** TodoRoutes is a service that provides the routes for the TodoService API. The routes
  * serve the "todo" endpoint.
  */
final case class TodoRoutes(todoService: TodoService){

val routes: Http[Any, Throwable, Request, Response] = Http.collectZIO[Request] {

    // Gets all of the todos in the database and returns them as JSON.
    case Method.GET -> !! / "todos" => 
       todoService.getAll.map(todos => Response.json(todos.toJson))
       

    // Create a todo and return as JSON.
        case req@Method.POST -> !! / "todos" => 
    for{
            body <- req.bodyAsString.orElseFail(MissingBodyError)
            createTodo<- ZIO
                     .fromEither(body.fromJson[CreateTodo])
                     .mapError(_ => InvalidJsonBody)
              todo<-todoService.save(createTodo.todoId,createTodo.description)       
        } yield Response.json(todo.toJson).setStatus(Status.Created)


        //get a todo
        case Method.GET -> !! / "todos"/todoId => 
            todoService.get(todoId).map {
        case None        => Response.status(Status.NotFound)
        case Some(todo) => Response.json(todo.toJson)
    
                  } 
                  


        //update a todo
        case req@Method.PUT -> !! / "todos"/todoId => 
        for{
            body <- req.bodyAsString.orElseFail(MissingBodyError)
            updateTodo<- ZIO
                     .fromEither(body.fromJson[UpdateTodo])
                     .mapError(_ => InvalidJsonBody)
              todo<-todoService.update(updateTodo.todoId,updateTodo.description) 
        } yield Response.ok
    

        //delete a todo
    case Method.DELETE -> !! / "todos"/todoId => 
       for {
        _ <- todoService.delete(todoId)
      } yield Response.ok
      


}

}





/** Here in the companion object we define the layer that will be used to provide the routes for the
  * TodoService API.
  */
object TodoRoutes{
val live: ZLayer[TodoService,Nothing,TodoRoutes]=ZLayer.fromFunction(TodoRoutes.apply _)
}
