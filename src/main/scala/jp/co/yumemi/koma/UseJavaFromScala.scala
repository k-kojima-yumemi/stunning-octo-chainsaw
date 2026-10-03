package jp.co.yumemi.koma

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
      // The `Other` case is required for exhaustiveness on 2.13.18 and 3.9.0.
      // 3.3.8 stays silent when this case is removed (scala/scala3#25788 is 3.9.0).
      val name = shape match {
        case c: SealedInterface.Circle    => s"Circle(${c.radius()})"
        case r: SealedInterface.Rectangle => s"Rectangle(${r.topLeft()}, ${r.bottomRight()})"
        case o: SealedInterface.Other     => s"Other(${o.area()})"
      }
      println(s"$name => ${SealedInterface.describe(shape)}")
    }
  }
}
