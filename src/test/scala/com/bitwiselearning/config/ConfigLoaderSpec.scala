package com.bitwiselearning.config

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class ConfigLoaderSpec extends AnyFlatSpec with Matchers {
  "ConfigLoader" should "load the correct configuration for the specified environment" in {
    val env = sys.props.getOrElse("env", "dev") // Default to "dev" if no property is set
    val config = ConfigLoader.loadConfig(env)
    config.getString("vault.url") shouldBe s"http://127.0.0.1:8200"
    config.getString("vault.role_id") shouldBe s"490e4607-e2bb-81fe-046c-b289713fd420"
    config.getString("vault.secret_id") shouldBe s"76cc9abd-50ba-9cec-18bf-96e4d50415a1"
    config.getString("vault.secrets_path") shouldBe s"secret/data/dev/bbu/fileupload-secrets"
    config.getString("idp.url") shouldBe s"https://idp-$env.example.com"
    config.getString("api.url") shouldBe s"https://api-$env.example.com/upload"
  }
}