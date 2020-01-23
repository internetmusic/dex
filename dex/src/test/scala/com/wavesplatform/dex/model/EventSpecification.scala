package com.wavesplatform.dex.model

import com.wavesplatform.dex.MatcherSpecBase
import com.wavesplatform.dex.domain.account.KeyPair
import com.wavesplatform.dex.domain.asset.Asset.Waves
import com.wavesplatform.dex.domain.asset.AssetPair
import com.wavesplatform.dex.model.Events.OrderExecuted
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.should.Matchers

class EventSpecification extends AnyFreeSpec with Matchers with MatcherSpecBase {

  "Proper rounding scenario 1" in {
    val pair      = AssetPair(Waves, mkAssetId("BTC"))
    val counter   = sell(pair, 840340L, 0.00000238, matcherFee = Some(300000L))
    val submitted = buy(pair, 425532L, 0.00000238, matcherFee = Some(300000L))
    val exec      = OrderExecuted(LimitOrder(submitted), LimitOrder(counter), 0L, submitted.matcherFee, counter.matcherFee)
    exec.executedAmount shouldBe 420169L
    exec.counterRemainingAmount shouldBe 420171L
    exec.counterRemainingAmount shouldBe counter.amount - exec.executedAmount

    exec.counterRemainingFee shouldBe 150001L

    exec.submittedRemainingAmount shouldBe 5363L
    exec.submittedRemainingAmount shouldBe submitted.amount - exec.executedAmount

    exec.submittedRemainingFee shouldBe 3781L
  }

  "Remaining fee and amount checks" in {
    val pair      = AssetPair(Waves, mkAssetId("BTC"))
    val counter   = sell(pair, 100000000, 0.0008, matcherFee = Some(2000L))
    val submitted = buy(pair, 120000000, 0.00085, matcherFee = Some(1000L))

    val exec = OrderExecuted(LimitOrder(submitted), LimitOrder(counter), 0L, submitted.matcherFee, counter.matcherFee)
    exec.submittedRemainingAmount shouldBe 20000000L
    exec.submittedRemainingFee shouldBe 167L
  }

  "Reserved balance should empty after full rounded execution" in {
    val pair = AssetPair(mkAssetId("BTC"), mkAssetId("ETH"))

    val alicePk   = KeyPair("alice".getBytes("utf-8"))
    val counter   = buy(pair, 923431000L, 0.00031887, matcherFee = Some(300000), sender = Some(alicePk))
    val bobPk     = KeyPair("bob".getBytes("utf-8"))
    val submitted = sell(pair, 223345000L, 0.00031887, matcherFee = Some(300000), sender = Some(bobPk))

    val exec = OrderExecuted(LimitOrder(submitted), LimitOrder(counter), 0L, submitted.matcherFee, counter.matcherFee)
    exec.executedAmount shouldBe 223344937L
  }
}
