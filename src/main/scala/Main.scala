import zio.http.Server.Config
import zio.{ZIO, ZIOAppDefault, ZLayer}
import zio.http.{Handler, Method, Response, Routes, Server}
import zio.config._
import zio.config.magnolia._
import zio.config.typesafe._
import zio.json._

import java.io.File

object Main extends ZIOAppDefault {

  case class MyConfig(sharedValue: String, configMapValue: String, secretValue: String, anotherSecretValue: String)
  val myConfig = deriveConfig[MyConfig]
  implicit val myConfigEncoder: JsonEncoder[MyConfig] = DeriveJsonEncoder.gen[MyConfig]
  val configLayer = ZLayer.fromZIO(TypesafeConfigProvider.fromResourcePath(false).load(myConfig).orDie)

  val routes = Routes(
    Method.GET / "" -> Handler.fromZIO(ZIO.serviceWith[MyConfig](c => Response.text(c.toJsonPretty))),
//    Method.GET / "host-file" -> Handler.fromFile(new File("/etc-on-host/hosts")).orDie,
  )

  val serverConfig = Config.default.binding("0.0.0.0", 8080)

  val runServer = Server
    .serve(routes.toHttpApp)
    .provide(
      configLayer,
      ZLayer.succeed(serverConfig),
      Server.live,
    )

  val runConsole =
    ZIO.serviceWith[MyConfig](_.toJsonPretty).debug("Config: ").provide(configLayer)

  override def run: zio.ZIO[zio.ZIOAppArgs, Throwable, Any] =
    runServer

}