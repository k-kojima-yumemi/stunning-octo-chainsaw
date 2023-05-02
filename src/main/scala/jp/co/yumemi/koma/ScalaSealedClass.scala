package jp.co.yumemi.koma

sealed class ScalaSealedClass {
  class Child3 extends ScalaSealedClass
}

object ScalaSealedClass {
  final class Child1 extends ScalaSealedClass

  class Child2 extends ScalaSealedClass

  def main(args: Array[String]): Unit = {
    val example = new ScalaSealedClass
    val c1 = new Child1
    val c2 = new Child2
    val c3 = new example.Child3
    println(c1.getClass)
    println(c2.getClass)
    println(c3.getClass)
  }
}
