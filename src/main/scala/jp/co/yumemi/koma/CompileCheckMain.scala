package jp.co.yumemi.koma

case class Position(x: Int, y: Int)

object CompileCheckMain {

  private def section(name: String)(body: => Unit): Unit = {
    println("-" * 10 + name + "-" * 10)
    body
  }

  def main(args: Array[String]): Unit = {
    // Java 9 - 17 (checked in the original article with 2.13.10 / 3.2.2)
    section("RecordExample")(RecordExample.main(args))
    section("HasPrivate")(HasPrivate.main(args))
    section("PatternMatch")(PatternMatch.main(args))
    // Compile error in 2.13.10 and 3.2.2, works in 2.13.18, 3.3.8 and 3.9.0
    section("SealedExample")(SealedExample.main(args))
    section("SealedInterface")(SealedInterface.main(args))
    section("ScalaSealedClass")(ScalaSealedClass.main(args))
    section("SwitchExpression")(SwitchExpression.main(args))
    section("TextBlock")(TextBlock.main(args))
    section("StaticInInner")(StaticInInner.main(args))

    // Java 21
    section("RecordPattern")(RecordPattern.main(args))
    section("SwitchPattern")(SwitchPattern.main(args))
    // Java 22
    section("UnnamedVariable")(UnnamedVariable.main(args))
    // Java 23
    section("MarkdownDoc")(MarkdownDoc.main(args))
    // Java 25
    // Compile error in 2.13.18, 3.3.8 and 3.9.0
    // section("ModuleImport")(ModuleImport.main(args))
    section("FlexibleConstructor")(FlexibleConstructor.main(args))

    // Use the Java records and sealed types from Scala
    section("UseJavaFromScala")(UseJavaFromScala.run())
  }
}

object UseJavaFromScala {
  def run(): Unit = {
    val point = RecordPattern.Point.of(Position(1, 2))
    val line = new RecordPattern.Line(point, new RecordPattern.Point(3, 4))
    println(s"${line.start().x()},${line.start().y()} -> ${line.end().x()},${line.end().y()}")

    val shapes: Seq[SealedInterface] = Seq(
      new SealedInterface.Circle(2),
      new SealedInterface.Rectangle(Position(0, 0), Position(1, 1)),
    )
    shapes.foreach { shape =>
      val name = shape match {
        case c: SealedInterface.Circle    => s"Circle(${c.radius()})"
        case r: SealedInterface.Rectangle => s"Rectangle(${r.topLeft()}, ${r.bottomRight()})"
        case o: SealedInterface.Other     => s"Other(${o.area()})"
      }
      println(s"$name => ${SealedInterface.describe(shape)}")
    }
  }
}
