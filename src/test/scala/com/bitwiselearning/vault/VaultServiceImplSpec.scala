//package com.bitwiselearning.vault
//
//import org.scalatest.flatspec.AnyFlatSpec
//import org.scalatest.matchers.should.Matchers
//
//class VaultServiceImplSpec extends AnyFlatSpec with Matchers {
//  "VaultServiceImpl" should "fetch the client certificate from Vault" in {
//    val vaultService = new VaultServiceImpl("dev")
//    val clientCert = vaultService.fetchClientCert
//    // Assert the behavior, such as verifying the returned client certificate
//    clientCert should not be empty
//  }
//}

package com.bitwiselearning.vault

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class VaultServiceImplSpec extends AnyFlatSpec with Matchers {
  "VaultServiceImpl" should "fetch secrets from Vault" in {
    val vaultService = new VaultServiceImpl("dev")
    val secrets = vaultService.fetchSecrets

    // Assert the behavior, such as verifying the returned secrets
    secrets should not be empty
    secrets should contain key "client_cert"
    secrets should contain key "client_key"

    // Add more assertions as needed for other secrets
  }
}

