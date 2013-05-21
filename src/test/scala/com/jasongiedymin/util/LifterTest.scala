package com.jasongiedymin.util

import org.scalatest.FreeSpec


class LifterTest extends FreeSpec {
  trait TestData extends LifterTypes[Int] {
    def fTimesTwo(x: Int): Option[Int] = Some(x * 2)
    def fTimesThree(x: Int): Option[Int] = Some(x * 3)
    def fHalf(x: Int): Option[Int] = Some(x / 2)
    def fDivideThree(x: Int): Option[Int] = Some(x / 3)

    val myValueList: ValueList = List(Some(1), None, Some(2), Some(3))
    val myFuncList: FunctionList = List(Some(fTimesTwo _), Some(fTimesThree _), Some(fHalf _), Some(fDivideThree _), None)
  }

  "Simple Lifter Test" - new TestData {
    "Should lift and run the 'defined' methods against a 'defined' list of values" - {
      "As a class" in {
        assert( new Lifter[Int].m(myValueList, myFuncList) === List(1,2,3) )
      }
      "As a companion obj" in {
        assert( Lifter.run[Int].m(myValueList, myFuncList) === List(1,2,3) )
      }
      "As a companion obj with inferrence" in {
        assert( Lifter.run.m(myValueList, myFuncList) === List(1,2,3) )
      }
    }
  }
}
