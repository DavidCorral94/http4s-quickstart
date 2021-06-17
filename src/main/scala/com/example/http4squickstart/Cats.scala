package com.example.http4squickstart

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

final case class Cat(id: String, url: String, width: Int, height: Int)
object Cat {
  implicit val catDecoder: Decoder[Cat] = deriveDecoder[Cat]
  implicit val catEncoder: Encoder[Cat] = deriveEncoder[Cat]
}
