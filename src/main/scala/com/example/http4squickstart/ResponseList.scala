package com.example.http4squickstart

import io.circe.syntax.KeyOps
import io.circe.{Decoder, Encoder, Json}

final case class ResponseList[A](value: List[A]) extends AnyVal
object ResponseList {
  implicit def responseListDecoder[A: Decoder]: Decoder[ResponseList[A]] =
    Decoder.decodeList[A].map(ResponseList(_))
  implicit def responseListEncoder[A: Encoder]: Encoder[ResponseList[A]] =
    Encoder.instance(rl =>
      Json.obj(
        "response" := rl.value
      )
    )
}
