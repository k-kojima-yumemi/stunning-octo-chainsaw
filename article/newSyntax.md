# Java new syntax

## Java 9 - 17

* `var`
* `private` in interface
* `switch`
  * yield
  * arrow
  * multi case
* `instanceof` pattern matching
* text block
* sealed class
  * permits
  * class
  * interface
* records
  * constructor
* static class in non static class

## Java 18 - 25

Only language features which were finalized (not preview) by Java 25 are listed.
Preview features which have not been finalized by Java 25 (primitive types in patterns, etc.) are excluded.

* Java 18
  * (no finalized language feature)
* Java 19
  * (no finalized language feature)
* Java 20
  * (no finalized language feature)
* Java 21
  * [JEP 440](https://openjdk.org/jeps/440) Record Patterns (preview in 19, 20)
    * `instanceof` with record pattern
    * nested record pattern
    * `var` in record pattern
    * generic record pattern (type inference)
  * [JEP 441](https://openjdk.org/jeps/441) Pattern Matching for `switch` (preview in 17, 18, 19, 20)
    * type pattern
    * record pattern in `switch`
    * guard (`when`)
    * `case null`
    * `case null, default`
    * qualified enum constant as case label
    * exhaustive `switch` over sealed hierarchy
* Java 22
  * [JEP 456](https://openjdk.org/jeps/456) Unnamed Variables & Patterns (preview in 21)
    * unnamed local variable
    * unnamed variable in enhanced `for`
    * unnamed exception parameter
    * unnamed lambda parameter
    * unnamed pattern variable (`Position _`)
    * unnamed pattern (`_`)
    * multiple patterns in one case label
* Java 23
  * [JEP 467](https://openjdk.org/jeps/467) Markdown Documentation Comments (`///`)
* Java 24
  * (no finalized language feature)
* Java 25
  * [JEP 511](https://openjdk.org/jeps/511) Module Import Declarations (preview in 23, 24)
    * `import module java.base;`
  * [JEP 513](https://openjdk.org/jeps/513) Flexible Constructor Bodies (preview in 22, 23, 24)
    * statements before `super(...)`
    * statements before `this(...)`
    * field initialization before `super(...)`

### Excluded

* Java only features which have nothing to do with Scala interop
  * [JEP 512](https://openjdk.org/jeps/512) Compact Source Files and Instance Main Methods (Java 25)
  * API changes (Sequenced Collections, Virtual Threads, Stream Gatherers, Scoped Values, FFM API, Class-File API, ...)
* Preview features in Java 25
  * [JEP 507](https://openjdk.org/jeps/507) Primitive Types in Patterns, instanceof, and switch
  * Structured Concurrency, Stable Values, PEM Encodings
* Java 26, 27 have no language feature changes (checked)

## Result

### Environment

* Java: Temurin 25.0.4.1 (JEP 511 and 513 need Java 25)
* sbt: 2.0.10
* Scala
  * 2.13.18
  * 3.3.8 (LTS)
  * 3.9.0
* `compileOrder := CompileOrder.Mixed`

### Summary

| Java | Function                                   | File                       | 2.13.18 | 3.3.8 | 3.9.0 |
|-----:|--------------------------------------------|----------------------------|:-------:|:-----:|:-----:|
|   10 | `var`                                      | `PatternMatch.java` etc.   |    ✅    |   ✅   |   ✅   |
|    9 | private method in interface                | `HasPrivate.java`          |    ✅    |   ✅   |   ✅   |
|   16 | Pattern Matching for `instanceof`          | `PatternMatch.java`        |    ✅    |   ✅   |   ✅   |
|   16 | Records                                    | `RecordExample.java`       |    ✅    |   ✅   |   ✅   |
|   16 | static members in inner class              | `StaticInInner.java`       |    ✅    |   ✅   |   ✅   |
|   17 | Sealed Classes (class, implicit permits)   | `SealedExample.java`       |    ✅    |   ✅   |   ✅   |
|   17 | Sealed Classes (interface, `permits`)      | `SealedInterface.java`     |    ✅    |   ✅   |   ✅   |
|   14 | Switch Expressions                         | `SwitchExpression.java`    |    ✅    |   ✅   |   ✅   |
|   15 | Text Blocks                                | `TextBlock.java`           |    ✅    |   ✅   |   ✅   |
|   21 | Record Patterns                            | `RecordPattern.java`       |    ✅    |   ✅   |   ✅   |
|   21 | Pattern Matching for `switch`              | `SwitchPattern.java`       |    ✅    |   ✅   |   ✅   |
|   22 | Unnamed Variables & Patterns               | `UnnamedVariable.java`     |    ✅    |   ✅   |   ✅   |
|   23 | Markdown Documentation Comments            | `MarkdownDoc.java`         |    ✅    |   ✅   |   ✅   |
|   25 | Module Import Declarations                 | `ModuleImport.java`        |    ❌    |   ❌   |   ❌   |
|   25 | Flexible Constructor Bodies                | `FlexibleConstructor.java` |    ✅    |   ✅   |   ✅   |

In the original article (2023/05/02, 2.13.10 / 3.2.2) Records were ❌ in 3.2.2 and Sealed Classes were ❌ in both.
Both are now ✅.

### Notes

* Module Import Declarations (`import module java.base;`)
  * The Java parser in scalac / dotty parses Java sources to extract signatures when `compileOrder` is `Mixed`,
    and it does not recognize `import module`. javac itself has no problem.
  * 2.13.18
    ```
    [error] ModuleImport.java:4:15: `;` expected but identifier found.
    [error] import module java.base;
    [error]               ^
    ```
  * 3.3.8 / 3.9.0
    ```
    [error] -- Error: ModuleImport.java:4:14 ---
    [error] 4 |import module java.base;
    [error]   |              ^^^^
    [error]   |              ';' expected but identifier found.
    ```
  * `ModuleImport.java` is commented out in the repository so that the other checks can be compiled.
* Everything inside a method body (switch patterns, record patterns, `_`, statements before `super()`)
  is skipped by the Java parser of scalac / dotty, so these features work as long as javac supports them.
  Features which appear at declaration level (`sealed`, `permits`, `non-sealed`, `record`, `import module`)
  need explicit support from the Scala side.
* Scala side pattern matching over a Java sealed hierarchy (`SealedInterface`)
  * Exhaustiveness check works in 2.13.18 and 3.9.0 (dropping `case o: SealedInterface.Other` in `UseJavaFromScala.scala` emits "match may not be exhaustive").
  * 3.3.8 does not emit the warning. The check arrived in https://github.com/scala/scala3/pull/25788 (3.9.0).
* Java records can be used from Scala in all three versions (`RecordPattern.Point`, `SealedInterface.Circle`).
  Deconstructing Java records in Scala pattern matching (`case Point(x, y) =>`) is planned for Scala 3.10.0
  (https://github.com/scala/scala3/pull/26497), not checked here.

### Related links

* 2.13
  * Sealed Classes: https://github.com/scala/scala/pull/10105 , https://github.com/scala/scala/pull/10348 (2.13.11)
  * Records: https://github.com/scala/scala/pull/9551 (2.13.7)
* 3
  * Sealed Classes: https://github.com/scala/scala3/pull/19080 (3.4.2)
  * Records: https://github.com/scala/scala3/pull/16762 (3.3.1)
  * Exhaustiveness check for Java sealed classes: https://github.com/scala/scala3/pull/25788 (3.9.0)
  * Java records in pattern matching: https://github.com/scala/scala3/pull/26497 (3.10.0)

## Ref

* [Java8からJava11への変更点](https://qiita.com/nowokay/items/1ce24079f4daafc73b4a)
* [Java 12新機能まとめ](https://qiita.com/nowokay/items/0e860819b6ffb1aca90a)
* [Java 13新機能まとめ](https://qiita.com/nowokay/items/3e1625a77cb435394547)
* [Java 14新機能まとめ](https://qiita.com/nowokay/items/ec85d97a7cecaaac8123)
* [Java 15新機能まとめ](https://qiita.com/nowokay/items/2858699bc1cd89222cd8)
* [Java 16新機能まとめ](https://qiita.com/nowokay/items/215769cdcb14d6c5412f)
* [Java 17新機能まとめ](https://qiita.com/nowokay/items/ec58bf8f30d236a12acb)
* [Java 18新機能まとめ](https://qiita.com/nowokay/items/17d990aa8a5b1c5223c8)
* [Java 19新機能まとめ](https://qiita.com/nowokay/items/b903c10502f9ffe50c3a)
* [Java 20新機能まとめ](https://qiita.com/nowokay/items/e42a7c7f403fd5f85d16)
* [Java 21新機能まとめ](https://qiita.com/nowokay/items/174f75b9e48cc7a80838)
* [Java 22新機能まとめ](https://qiita.com/nowokay/items/3b8307a911f014038873)
* [Java 23新機能まとめ](https://qiita.com/nowokay/items/7650b959fd4b0be54751)
* [Java 24新機能まとめ](https://qiita.com/nowokay/items/03e5d720433bd4ba0eec)
* [Java 25新機能まとめ](https://qiita.com/nowokay/items/7e05b4c42ded043a298a)
* [JEPs in JDK 17 integrated since JDK 11](https://openjdk.org/projects/jdk/17/jeps-since-jdk-11)
* [JEPs in JDK 21 integrated since JDK 17](https://openjdk.org/projects/jdk/21/jeps-since-jdk-17)
* [JEPs in JDK 25 integrated since JDK 21](https://openjdk.org/projects/jdk/25/jeps-since-jdk-21)
