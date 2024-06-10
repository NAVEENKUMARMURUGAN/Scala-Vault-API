package com.bitwiselearning.vault

trait VaultService {
  def fetchSecrets : Map[String, String]
}