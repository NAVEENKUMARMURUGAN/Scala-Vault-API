package com.bitwiselearning.config

import com.typesafe.config.{Config, ConfigFactory}
import java.io.File

object ConfigLoader {
  def loadConfig(env: String): Config = {
    val configFile = new File(s"src/main/resources/$env/application.yaml")
    ConfigFactory.parseFile(configFile)
  }
}