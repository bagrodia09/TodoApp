package domain

sealed trait AppError extends Exception

object AppError {
  case object MissingBodyError extends AppError
  case object InvalidJsonBody extends AppError
}