package com.wavesplatform.dex.settings

import cats.syntax.either._
import com.typesafe.config.{Config, ConfigFactory}
import net.ceedubs.ficus.Ficus._
import org.scalatest.flatspec.AnyFlatSpec

import scala.util.Try

class BaseSettingsSpecification extends AnyFlatSpec {

  def getSettingByConfig(conf: Config): Either[String, MatcherSettings] =
    Try(conf.as[MatcherSettings]("waves.dex")).toEither.leftMap(_.getMessage)

  val correctOrderFeeStr: String =
    s"""
       |order-fee {
       |  mode = percent
       |  dynamic {
       |    base-fee = 300000
       |    zero-maker-double-taker = true
       |  }
       |  fixed {
       |    asset = WAVES
       |    min-fee = 300000
       |  }
       |  percent {
       |    asset-type = amount
       |    min-fee = 0.1
       |  }
       |}
       """.stripMargin

  val correctDeviationsStr: String =
    s"""
       |max-price-deviations {
       |  enable = yes
       |  profit = 1000000
       |  loss = 1000000
       |  fee = 1000000
       |}
     """.stripMargin

  val correctAllowedAssetPairsStr: String =
    s"""
       |allowed-asset-pairs = []
     """.stripMargin

  val correctOrderRestrictionsStr: String =
    s"""
       |order-restrictions = {}
     """.stripMargin

  val correctMatchingRulesStr: String =
    s"""
       |matching-rules = {}
     """.stripMargin

  def configWithSettings(orderFeeStr: String = correctOrderFeeStr,
                         deviationsStr: String = correctDeviationsStr,
                         allowedAssetPairsStr: String = correctAllowedAssetPairsStr,
                         orderRestrictionsStr: String = correctOrderRestrictionsStr,
                         matchingRulesStr: String = correctMatchingRulesStr): Config = {
    val configStr =
      s"""waves {
         |  directory = /waves
         |  dex {
         |    account-storage {
         |      type = "in-mem"
         |      in-mem.seed-in-base64 = "c3lrYWJsZXlhdA=="
         |    }
         |    rest-api {
         |      address = 127.1.2.3
         |      port = 6880
         |      api-key-hash = foobarhash
         |      cors = no
         |      api-key-different-host = no
         |    }
         |    waves-blockchain-client {
         |      grpc {
         |        target = "127.1.2.9:6333"
         |        max-hedged-attempts = 9
         |        max-retry-attempts = 13
         |        keep-alive-without-calls = false
         |        keep-alive-time = 8s
         |        keep-alive-timeout = 11s
         |        idle-timeout = 20s
         |        channel-options {
         |          connect-timeout = 99s
         |        }
         |      }
         |      default-caches-expiration = 101ms
         |    }
         |    exchange-tx-base-fee = 300000
         |    actor-response-timeout = 11s
         |    snapshots-interval = 999
         |    limit-events-during-recovery = 48879
         |    make-snapshots-at-start = yes
         |    snapshots-loading-timeout = 423s
         |    start-events-processing-timeout = 543s
         |    order-books-recovering-timeout = 111s
         |    rest-order-limit = 100
         |    price-assets = [
         |      WAVES
         |      8LQW8f7P5d5PZM7GtZEBgaqRPGSzS3DfPuiXrURJ4AJS
         |      DHgwrRvVyqJsepd32YbBqUeDH4GJ1N984X8QoekjgH8J
         |    ]
         |    blacklisted-assets = ["AbunLGErT5ctzVN8MVjb4Ad9YgjpubB8Hqb17VxzfAck"]
         |    blacklisted-names = ["b"]
         |    blacklisted-addresses = [
         |      3N5CBq8NYBMBU3UVS3rfMgaQEpjZrkWcBAD
         |    ]
         |    white-list-only = yes
         |    allowed-order-versions = [11, 22]
         |    order-book-snapshot-http-cache {
         |      cache-timeout = 11m
         |      depth-ranges = [1, 5, 333]
         |      default-depth = 5
         |    }
         |    events-queue {
         |      type = "kafka"
         |
         |      local {
         |        enable-storing = no
         |        polling-interval = 1d
         |        max-elements-per-poll = 99
         |        clean-before-consume = no
         |      }
         |
         |      kafka {
         |        topic = "some-events"
         |
         |        consumer {
         |          fetch-max-duration = 10s
         |          max-buffer-size = 777
         |          client.foo = 2
         |        }
         |
         |        producer {
         |          enable = no
         |          client.bar = 3
         |        }
         |      }
         |    }
         |    process-consumed-timeout = 663s
         |    $orderFeeStr
         |    $deviationsStr
         |    $allowedAssetPairsStr
         |    $orderRestrictionsStr
         |    $matchingRulesStr
         |    exchange-transaction-broadcast {
         |      broadcast-until-confirmed = yes
         |      interval = 1 day
         |      max-pending-time = 30 days
         |    }
         |  }
         |}""".stripMargin

    loadConfig(ConfigFactory.parseString(configStr))
  }
}
