package api.server

import api.server.config.ServerConfig
import fs2.io.net.tls.TLSContext
import io.circe.Json
import org.http4s._
import org.http4s.circe._
import org.http4s.dsl.Http4sDsl
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.implicits._
import service.order.OrderService
import zio.interop.catz._
import zio.{Task, UIO, ZIO}

import java.io.FileInputStream
import java.security.{KeyFactory, KeyStore, PrivateKey, SecureRandom}
import java.security.cert.CertificateFactory
import java.security.spec.PKCS8EncodedKeySpec
import java.util.Base64
import javax.net.ssl.{KeyManagerFactory, SSLContext}

final class ServerLive(config: ServerConfig, orderService: OrderService)
    extends Server {

  private val dsl = Http4sDsl[Task]
  import dsl._

  private val routes: HttpRoutes[Task] = HttpRoutes.of[Task] {
    case req @ POST -> Root / "notification" =>
      for {
        json <- req.as[Json]
        _    <- orderService.saveOrder(json)
        resp <- Ok()
      } yield resp
  }

  private val httpApp: HttpApp[Task] = routes.orNotFound

  private def sslContext: Task[SSLContext] =
    ZIO.attempt {
      // Initialize an empty KeyStore
      val keyStore = KeyStore.getInstance("JKS")
      keyStore.load(null, null)

      // Load the certificate
      val certStream =
        new FileInputStream("/etc/letsencrypt/live/santexserv.ru/fullchain.pem")
      val certificate =
        CertificateFactory.getInstance("X.509").generateCertificate(certStream)
      certStream.close()

      // Load the private key
      val keyStream =
        new FileInputStream("/etc/letsencrypt/live/santexserv.ru/privkey.pem")
      val keyContent = scala.io.Source.fromInputStream(keyStream).mkString
      keyStream.close()

      // Parse the private key (remove PEM headers and decode Base64)
      val keyBase64 = keyContent
        .replace("-----BEGIN PRIVATE KEY-----", "")
        .replace("-----END PRIVATE KEY-----", "")
        .replaceAll("\\s", "")
      val keyBytes               = Base64.getDecoder.decode(keyBase64)
      val keySpec                = new PKCS8EncodedKeySpec(keyBytes)
      val keyFactory             = KeyFactory.getInstance("RSA")
      val privateKey: PrivateKey = keyFactory.generatePrivate(keySpec)

      // Add certificate and private key to KeyStore
      keyStore.setKeyEntry(
        "alias",
        privateKey,
        "changeit".toCharArray,
        Array(certificate)
      )

      // Initialize KeyManagerFactory
      val kmf =
        KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm)
      kmf.init(keyStore, "changeit".toCharArray)

      // Create SSLContext
      val sslContext = SSLContext.getInstance("TLS")
      sslContext.init(kmf.getKeyManagers, null, new SecureRandom)
      sslContext
    }

  override def run(): UIO[Unit] =
    sslContext.flatMap { sslContext =>
      val tls = TLSContext.Builder.forAsync[Task].fromSSLContext(sslContext)
      EmberServerBuilder
        .default[Task]
        .withHost(config.host)
        .withPort(config.port)
        .withTLS(tls)
        .withHttpApp(httpApp)
        .build
        .useForever
        .orDie

    }.orDie

}
