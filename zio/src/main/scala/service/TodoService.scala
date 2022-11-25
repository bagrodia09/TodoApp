package service

import zio._
import domain._

import javax.sql.DataSource

trait TodoService {
  def getAll: Task[List[Todo]]
  def save(todoId: String, description: String): Task[Todo]
  def update(todoId: String, description: String): Task[Todo]
  def get(todoId: String): Task[Option[Todo]]
  def delete(todoId: String): Task[Unit]
}

/** TodoServiceLive is a service which provides the "live" implementation of the TodoService . This
  * implementation uses a DataSource, which will concretely be a connection pool.
  */
case class TodoServiceLive(dataSource: DataSource) extends TodoService {
  import db.QuillContext._
  override def getAll: Task[List[Todo]] = ???

  override def save(todoId: String, description: String): Task[Todo] = ???

  override def update(todoId: String, description: String): Task[Todo] = ???

  override def get(todoId: String): Task[Option[Todo]] = ???

  override def delete(todoId: String): Task[Unit] = ???
}

object TodoService {

  val live: ZLayer[DataSource, Nothing, TodoServiceLive] = ZLayer.fromFunction(
    TodoServiceLive.apply _
  )

}
