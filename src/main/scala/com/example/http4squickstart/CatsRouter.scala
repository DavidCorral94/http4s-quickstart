package com.example.http4squickstart

import cats.effect.Sync
import org.http4s.implicits._
import org.http4s.client.Client
import org.http4s.Method._
import org.http4s.{Request, Status}
import org.http4s.circe.CirceEntityCodec._

trait CatsRouter[F[_]] {
  def get: F[ResponseList[Cat]]
}

object CatsRouter {

  final case class CatError(e: Throwable) extends RuntimeException

  def apply[F[_]](implicit ev: CatsRouter[F]): CatsRouter[F] = ev

  def impl[F[_]: Sync](C: Client[F]): CatsRouter[F] =
    new CatsRouter[F] {
      def get: F[ResponseList[Cat]] = {
        val req: Request[F] =
          Request[F](GET, uri"https://api.thecatapi.com/v1/images/search")
        C.run(req).use {
          case Status.Successful(ok) => ok.as[ResponseList[Cat]]
          case _                  =>  throw new ArithmeticException("divide by 0")
        }
      }
    }
}
