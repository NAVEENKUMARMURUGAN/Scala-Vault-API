package com.bitwiselearning.idp

import akka.actor.ActorSystem
import akka.http.javadsl.model.headers.HttpCredentials
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.model.headers.HttpCredentials
import akka.stream.Materializer
import akka.util.ByteString
import com.bitwiselearning.config.ConfigLoader
import com.bitwiselearning.vault.VaultServiceImpl
import io.circe.generic.auto._
import io.circe.parser._
import sttp.client3._

import scala.concurrent.{ExecutionContext, Future}

case class TokenResponse(access_token: String)

class IdpService(env: String)(implicit system: ActorSystem, mat: Materializer, ec: ExecutionContext) {
  private val config = ConfigLoader.loadConfig(env)
  private val idpUrl = config.getString("idp.url")

  private val vaultService = new VaultServiceImpl(env)
  private val secrets = vaultService.fetchSecrets

  private val clientId = secrets.getOrElse("client_id", throw new RuntimeException("client_id not found in Vault"))
  private val clientSecret = secrets.getOrElse("client_secret", throw new RuntimeException("client_secret not found in Vault"))
  private val clientCert = secrets.getOrElse("client_cert", throw new RuntimeException("client_cert not found in Vault"))
  private val clientKey = secrets.getOrElse("client_key", throw new RuntimeException("client_key not found in Vault"))

  def getToken: Future[String] = {
    val requestEntity = FormData(Map("client_id" -> clientId, "client_secret" -> clientSecret)).toEntity

    val httpRequest = HttpRequest(
      method = HttpMethods.POST,
      uri = s"$idpUrl/token",
      entity = requestEntity
    ).addCredentials(HttpCredentials.createMutual("customRealm", ByteString(clientCert), ByteString(clientKey)))

    for {
      response <- Http().singleRequest(httpRequest)
      entity <- response.entity.dataBytes.runFold(ByteString(""))(_ ++ _)
      tokenResponse <- Future.fromTry(decode[TokenResponse](entity.utf8String))
    } yield tokenResponse.access_token
  }
}
