package com.bitwiselearning.vault

import com.bitwiselearning.config.ConfigLoader
import sttp.client3._
import sttp.client3.circe._
import io.circe.parser._
//
//class VaultServiceImpl(env: String) extends VaultService {
//  private val config = ConfigLoader.loadConfig(env)
//  private val vaultUrl = config.getString("vault.url")
//  private val roleId = config.getString("vault.role_id")
//  private val secretId = config.getString("vault.secret_id")
//  private val secretsPath = config.getString("vault.secrets_path")
////  private val secrets_to_fetch = config.getString("vault.secrets_to_fetch")
//
//  override def fetchClientCert: String = {
//    val vaultToken = authenticateWithAppRole(roleId, secretId)
//    val backend = HttpURLConnectionBackend()
//    val request = basicRequest
//      .header("X-Vault-Token", vaultToken)
//      .get(uri"$vaultUrl/v1/$secretsPath")
//
//    val response = request.send(backend)
//    response.body match {
//      case Right(json) =>
//        val cert = parse(json).flatMap(_.hcursor.downField("data").downField("data").get[String]("cert")).getOrElse("")
//        println(s"my cert is $cert")
//        cert
//      case Left(error) =>
//        throw new RuntimeException(s"Failed to fetch client cert: $error")
//    }
//  }
//
//  private def authenticateWithAppRole(roleId: String, secretId: String): String = {
//    val backend = HttpURLConnectionBackend()
//    val request = basicRequest
//      .post(uri"$vaultUrl/v1/auth/approle/login")
//      .body(Map("role_id" -> roleId, "secret_id" -> secretId))
//
//    val response = request.send(backend)
//    response.body match {
//      case Right(json) =>
//        parse(json).flatMap(_.hcursor.downField("auth").get[String]("client_token")).getOrElse {
//          throw new RuntimeException("Failed to retrieve client token")
//        }
//      case Left(error) =>
//        throw new RuntimeException(s"Failed to authenticate with AppRole: $error")
//    }
//  }
//}

class VaultServiceImpl(env: String) extends VaultService {
  private val config = ConfigLoader.loadConfig(env)
  private val vaultUrl = config.getString("vault.url")
  private val roleId = config.getString("vault.role_id")
  private val secretId = config.getString("vault.secret_id")
  private val secretsPath = config.getString("vault.secrets_path")

  override def fetchSecrets: Map[String, String] = {
    val vaultToken = authenticateWithAppRole(roleId, secretId)
    println(vaultToken)
    val backend = HttpURLConnectionBackend()
    val request = basicRequest
      .header("X-Vault-Token", vaultToken)
      .get(uri"$vaultUrl/v1/$secretsPath")

    val response = request.send(backend)
    response.body match {
      case Right(json) =>
        val parsedJson = parse(json)
        println(s"parsed json $parsedJson")
        parsedJson match {
          case Right(jsonObj) =>
            val secretsMap = jsonObj.hcursor.downField("data").downField("data").as[Map[String, String]].getOrElse(Map.empty)
            println(secretsMap)
            secretsMap
          case Left(error) =>
            throw new RuntimeException(s"Failed to parse JSON response from Vault: $error")
        }
      case Left(error) =>
        throw new RuntimeException(s"Failed to fetch secrets from Vault: $error")
    }
  }

  private def authenticateWithAppRole(roleId: String, secretId: String): String = {
    val backend = HttpURLConnectionBackend()
    val request = basicRequest
      .post(uri"$vaultUrl/v1/auth/approle/login")
      .body(Map("role_id" -> roleId, "secret_id" -> secretId))

    val response = request.send(backend)
    response.body match {
      case Right(json) =>
        parse(json).flatMap(_.hcursor.downField("auth").get[String]("client_token")).getOrElse {
          throw new RuntimeException("Failed to retrieve client token")
        }
      case Left(error) =>
        throw new RuntimeException(s"Failed to authenticate with AppRole: $error")
    }
  }
}
