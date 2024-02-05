import zio.http.Server.Config
import zio.{ZIO, ZIOAppDefault, ZLayer}
import zio.http.{Handler, Method, Response, Routes, Server}
import zio.config._
import zio.config.magnolia._
import zio.config.typesafe._
import zio.json._

import java.io.File

object Main extends ZIOAppDefault {

  case class MyConfig(url: String, urlFromConfigMap: String, secret: String)
  val myConfig = deriveConfig[MyConfig]
  implicit val myConfigEncoder: JsonEncoder[MyConfig] = DeriveJsonEncoder.gen[MyConfig]

  val routes = Routes(
    Method.GET / "" -> Handler.fromZIO(ZIO.serviceWith[MyConfig](c => Response.text(c.toJsonPretty))),
    Method.GET / "host-file" -> Handler.fromFile(new File("/etc-on-host/hosts")).orDie,
  )

  val serverConfig = Config.default.binding("0.0.0.0", 8080)

  override def run: zio.ZIO[zio.ZIOAppArgs, Throwable, Any] =
    Server
      .serve(routes.toHttpApp)
      .provide(
        ZLayer.fromZIO(TypesafeConfigProvider.fromResourcePath(false).load(myConfig).orDie),
          ZLayer.succeed(serverConfig) >>> Server.live,
      )
}