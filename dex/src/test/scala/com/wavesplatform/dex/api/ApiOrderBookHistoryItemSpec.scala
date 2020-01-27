package com.wavesplatform.dex.api

import com.wavesplatform.dex.domain.asset.Asset.{IssuedAsset, Waves}
import com.wavesplatform.dex.domain.asset.AssetPair
import com.wavesplatform.dex.domain.bytes.ByteStr
import com.wavesplatform.dex.domain.order.OrderType
import com.wavesplatform.dex.model.{AcceptedOrderType, OrderStatus}
import com.wavesplatform.dex.test.matchers.DiffMatcherWithImplicits
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.should.Matchers
import play.api.libs.json.Json

class ApiOrderBookHistoryItemSpec extends AnyFreeSpec with Matchers with DiffMatcherWithImplicits {

  private val json =
    """{
  "id" : "7VEr4T9icqopHWLawGAZ7AQiJbjAcnzXn65ekYvbpwnN",
  "type" : "buy",
  "orderType" : "limit",
  "amount" : 7757865201004347,
  "filled" : 0,
  "price" : 489,
  "fee" : 6345852410462127,
  "filledFee" : 0,
  "feeAsset" : null,
  "timestamp" : 1578074613225,
  "status" : "Accepted",
  "assetPair" : {
    "amountAsset" : "6rRegyHpdvZBENW4mowKYtKMDs2xpxmMbyNMRMZaZQ7",
    "priceAsset" : "8pFqaP5CtPB4kP87gpu2T7vB4LxdfoH9e5mSPQduhCc"
  }
}"""

  private val historyItem = ApiOrderBookHistoryItem(
    id = ByteStr.decodeBase58("7VEr4T9icqopHWLawGAZ7AQiJbjAcnzXn65ekYvbpwnN").get,
    `type` = OrderType.BUY,
    orderType = AcceptedOrderType.Limit,
    amount = 7757865201004347L,
    filled = 0L,
    price = 489L,
    fee = 6345852410462127L,
    filledFee = 0L,
    feeAsset = Waves,
    timestamp = 1578074613225L,
    status = OrderStatus.Accepted.name,
    assetPair = AssetPair(
      IssuedAsset(ByteStr.decodeBase58("6rRegyHpdvZBENW4mowKYtKMDs2xpxmMbyNMRMZaZQ7").get),
      IssuedAsset(ByteStr.decodeBase58("8pFqaP5CtPB4kP87gpu2T7vB4LxdfoH9e5mSPQduhCc").get)
    )
  )

  "backward JSON compatibility" - {
    "serialization" in {
      Json.parse(json).as[ApiOrderBookHistoryItem] should matchTo(historyItem)
    }

    "deserialization" in {
      Json.prettyPrint(Json.toJson(historyItem)) should matchTo(json)
    }
  }
}