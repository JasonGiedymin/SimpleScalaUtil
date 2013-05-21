package com.jasongiedymin.util


trait LifterTypes[A] {
  type BaseType = A
  type OptionType = Option[A]
  type ValueList = List[OptionType]
  type Function = (A) => Option[A]
  type OptFunction = Option[Function]
  type FunctionList = List[OptFunction]
  type Result = List[A]
}

trait LifterComponent[A] extends LifterTypes[A] {
  def m(valList:ValueList, funcs:FunctionList): Result = {
    for {
      i <- valList
      r <- funcs.flatten.foldLeft(i)(_ flatMap _)
    } yield r
  }
}

case class Lifter[A]() extends LifterComponent[A]

object Lifter  {
  def run[A] = new Lifter[A]()
}