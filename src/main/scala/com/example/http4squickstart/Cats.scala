package com.example.http4squickstart

import cats.effect.Sync
import cats.implicits._
import io.circe.{Decoder, Encoder, JsonObject}
import io.circe.generic.semiauto._
import org.http4s._
import org.http4s.implicits._
import org.http4s.client.Client
import org.http4s.client.dsl.Http4sClientDsl
import org.http4s.circe._
import org.http4s.Method._

trait Cats[F[_]] {
  def get: F[Cats.Cat]
  def dummy : F[Cats.Cat]
}

object Cats {
  final case class Cat(breeds: List[JsonObject], id: String, url: String, width: Int, height: Int)

  object Cat {
    implicit val catDecoder: Decoder[Cat] = deriveDecoder[Cat]
    implicit def catEntityDecoder[F[_]: Sync]: EntityDecoder[F, Cat] = jsonOf
    implicit val catEncoder: Encoder[Cat] = deriveEncoder[Cat]
    implicit def catEntityEncoder[F[_]]: EntityEncoder[F, Cat] =
      jsonEncoderOf
  }

  final case class CatError(e: Throwable) extends RuntimeException

  def apply[F[_]](implicit ev: Cats[F]): Cats[F] = ev

  def impl[F[_]: Sync](C: Client[F]): Cats[F] =
    new Cats[F] {
      val dsl = new Http4sClientDsl[F] {}
      import dsl._
      def get: F[Cats.Cat] = {
        C.expect[Cat](GET(uri"http://localhost:8080/cat/dummy"))
          .adaptError { case t => {println(t);CatError(t)} }
      }

      override def dummy: F[Cat] = {
        Sync[F].pure(new Cat(List(), "1", "http://google.com", 1,1))
      }
    }
}
