package com.jasongiedymin.util

import org.scalatest.FreeSpec
import com.jasongiedymin.util.Memoize.Gauge
import org.scalatest.time.{Millis, Span}
import org.scalatest.concurrent.Timeouts._

class MemoizeTest extends FreeSpec {

  def addTen(x:Int) = {
    Thread.sleep(2000)
    x + 10
  }

  "Test function works within 2100 Milliseconds" - {
    failAfter(Span(2100, Millis)) {
      assert(addTen(10) == 20)
    }
  }

  "Single arg memoization" - {
    val addTenCached = Memoize(addTen _)(Gauge("addTenCached"))

    "works by calling addTenCached and spending 2000 Milliseconds on first execution" - {
      failAfter(Span(2100, Millis)) {
        assert(addTenCached(10) == 20)
      }

      "then less than 100 Milliseconds on further execution" - {
        failAfter(Span(100, Millis)) {
          assert(addTenCached(10) == 20)
        }
      }

    }
  }

}
