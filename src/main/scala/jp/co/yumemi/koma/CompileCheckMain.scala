package jp.co.yumemi.koma

object CompileCheckMain {

  private def section(name: String)(body: => Unit): Unit = {
    println("-" * 10 + name + "-" * 10)
    body
  }

  def main(args: Array[String]): Unit = {
    // Java 9 - 17
    section("RecordExample")(RecordExample.main(args))
    section("HasPrivate")(HasPrivate.main(args))
    section("PatternMatch")(PatternMatch.main(args))
    // Did not compile on 2.13.10 and 3.2.2. Compiles on 2.13.18, 3.3.8 and 3.9.0
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
