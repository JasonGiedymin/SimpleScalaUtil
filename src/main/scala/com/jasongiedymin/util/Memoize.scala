package com.jasongiedymin.util

import com.yammer.metrics.util.RatioGauge
import com.yammer.metrics.scala.{Counter, Timer, Instrumented}
import scala.math.ScalaNumber

object Memoize {

  case class Gauge(name:String) extends RatioGauge with Instrumented {
    val mName = "meter-%s" format name
    val cName = "counter-%s" format name
    val tName = "timer-%s" format name

    var hits:Counter = metrics.counter(cName)
    var calls:Timer = metrics.timer(tName)

    def getNumerator:Double = hits.count
    def getDenominator:Double = calls.count
  }

  /**
   * A memoized single-argument function
   **/
  class MemoizedFunction1[-T, +R](f: T => R)(gauge:Gauge) extends (T => R) {

    import scala.collection.mutable
    private[this] val cache = mutable.Map.empty[T, R]

    def apply(x: T): R = gauge.calls.time {
      //      println("key: %s" format x)
      cache.get(x) match {
        case Some(r) =>
          gauge.hits += 1
          //          println("hit1")
          r
        case None =>
          cache += (x -> f(x))
          cache(x)
      }
    }
  }

  /**
   * A memoized two-argument function
   **/
  class MemoizedFunction2[-T1, -T2, +R](f: (T1, T2) => R)(gauge:Gauge)
    extends ((T1, T2) => R) {

    import scala.collection.mutable

    private[this] val cache = mutable.Map.empty[(T1, T2), R]

    def apply(x: T1, y: T2): R = gauge.calls.time {
      val key = (x, y)
      //      println("key: %s" format key)
      //      println("size: %s" format  cache.size)
      cache.get(key) match {
        case Some(v) =>
          gauge.hits += 1
          //          println("hit2")
          v
        case None =>
          //          println("none found for size: %s, key: %s" format (cache.size, key))
          cache += (key -> f(x, y))
          cache(key)
      }
    }
  }


  /**
   * A memoized three-argument function
   **/
  class MemoizedFunction3[-T1, -T2, -T3, +R](f: ((T1, T2, T3) => R))(gauge:Gauge)
    extends ((T1, T2, T3) => R) {

    import scala.collection.mutable

    private[this] val cache = mutable.Map.empty[(T1, T2, T3), R]

    def apply(x: T1, y: T2, z: T3): R = gauge.calls.time {
      val key = (x, y, z)
      //      println("key: %s" format key)
      cache.get(key) match {
        case Some(v) =>
          gauge.hits += 1
          //          println("hit")
          v
        case None =>
          cache += (key -> f(x, y, z))
          cache(key)
      }
    }
  }


  /**
   * A memoized four-argument function
   **/
  class MemoizedFunction4[-T1, -T2, -T3, -T4, +R](f: ((T1, T2, T3, T4) => R))(gauge:Gauge)
    extends ((T1, T2, T3, T4) => R) {

    import scala.collection.mutable

    private[this] val cache = mutable.Map.empty[(T1, T2, T3, T4), R]

    def apply(a: T1, b: T2, c: T3, d: T4): R = gauge.calls.time {
      val key = (a, b, c, d)
      //      println("key: %s" format key)
      cache.get(key) match {
        case Some(v) =>
          gauge.hits += 1
          //          println("hit")
          v
        case None =>
          cache += (key -> f(a, b, c, d))
          cache(key)
      }
    }
  }

  def apply[T, R](f: T => R)(gauge:Gauge) = new MemoizedFunction1(f)(gauge)

  def apply[T1, T2, R](f: (T1, T2) => R)(gauge:Gauge) = new MemoizedFunction2(f)(gauge)

  def apply[T1, T2, T3, R](f: (T1, T2, T3) => R)(gauge:Gauge) = new MemoizedFunction3(f)(gauge)

  def apply[T1, T2, T3, T4, R](f: (T1, T2, T3, T4) => R)(gauge:Gauge) = new MemoizedFunction4(f)(gauge)
}
