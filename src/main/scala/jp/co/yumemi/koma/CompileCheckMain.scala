package jp.co.yumemi.koma

case class Position(x: Int, y: Int)

object CompileCheckMain {

  def main(args: Array[String]): Unit = {
    println("-" * 10 + "RecordExample" + "-" * 10)
    RecordExample.main(args)
    println("-" * 10 + "HasPrivate" + "-" * 10)
    HasPrivate.main(args)
    println("-" * 10 + "PatternMatch" + "-" * 10)
    PatternMatch.main(args)
    //    println("-" * 10 + "SealedExample" + "-" * 10)
    //    SealedExample.main(args)
    println("-" * 10 + "ScalaSealedClass" + "-" * 10)
    ScalaSealedClass.main(args)
    println("-" * 10 + "SwitchExpression" + "-" * 10)
    SwitchExpression.main(args)
    println("-" * 10 + "TextBlock" + "-" * 10)
    TextBlock.main(args)
  }
}
