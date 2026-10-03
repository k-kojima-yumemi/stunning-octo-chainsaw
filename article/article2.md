# はじめに

Scalaを使用している方々はほぼ全てのコードをScalaで書くでしょう。
しかしBetter JavaとしてScalaを使っている人には、時にJavaのライブラリの制約によってJavaをScalaのプロジェクトに入れなければならない場合があります。
そのような場合に、Javaに最近導入された機能がScala環境でコンパイルできるかを検証します。

Java 18 から 25 までに正式化された言語機能を、Scala 2.13.18, 3.3.8 (LTS), 3.9.0 で検証します。本記事でベースとする Java のバージョンは 25 です。

この記事は2026/10/03時点での内容です。

Java 17 までは前回の記事 [Javaの新しいシンタックスはScalaでコンパイルできるのか](https://qiita.com/k-kojima-yumemi/items/6d6eed3c9567bc56864a) で確認しています。

`compileOrder`の話はしません。JavaもScalaも相互に参照している前提です。
片方への参照がない場合は`JavaThenScala`か`ScalaThenJava`の適切な方を設定すれば正しくコンパイルされます。
この記事では`compileOrder := CompileOrder.Mixed`の設定です。

:::note info

* この記事は95%AIが生成しています。生成後人手でレビューし気になった5%を修正しています
* コードもAI生成で、レビューを行った後に動作確認をしています

:::

# 環境

## Java

```shell
$ java --version
openjdk 25.0.4.1 2026-08-18 LTS
OpenJDK Runtime Environment Temurin-25.0.4.1+1 (build 25.0.4.1+1-LTS)
OpenJDK 64-Bit Server VM Temurin-25.0.4.1+1 (build 25.0.4.1+1-LTS, mixed mode, sharing)
```

## SBT

```shell
$ sbt --version
sbt version in this project: 2.0.10
sbt runner version: 2.0.10
```

## Scala

* 2.13.18
* 3.3.8 (LTS)
* 3.9.0

# 対象の機能

Java 25 までに正式化された言語機能だけを見ます。
Java 25 時点でプレビューのままの機能は対象外です。プレビューで入って、Java 25 までに正式化された機能は対象です。

* Java 18
  * 正式化された言語機能はなし
* Java 19
  * 正式化された言語機能はなし
* Java 20
  * 正式化された言語機能はなし
* Java 21
  * Record Patterns (19, 20 はプレビュー)
  * Pattern Matching for `switch` (17, 18, 19, 20 はプレビュー)
* Java 22
  * Unnamed Variables & Patterns (21 はプレビュー)
* Java 23
  * Markdown Documentation Comments
* Java 24
  * 正式化された言語機能はなし
* Java 25
  * Module Import Declarations (23, 24 はプレビュー)
  * Flexible Constructor Bodies (22, 23, 24 はプレビュー)

クラス定義が不要なソースファイルや `IO.println` のように、Java の起動方法や API だけの変更は対象外です。
Scala には元からスクリプトがあり、`println` も修飾なしで使えます。
Java 26 と 27 も確認しましたが、言語機能の変更はありませんでした。

各機能の詳細は省きます。

Java 17 までの機能 (`var`、Records、Sealed Classes、Switch Expressions、Text Blocks など) も同じリポジトリで一緒に再実行しています。
コードはリポジトリを参照してください。
sealed interface と、前回は検証コードに含めていなかった inner class 内の static メンバーも今回コード欄に載せています。

## 参考

* [Java 18新機能まとめ](https://qiita.com/nowokay/items/17d990aa8a5b1c5223c8)
* [Java 19新機能まとめ](https://qiita.com/nowokay/items/b903c10502f9ffe50c3a)
* [Java 20新機能まとめ](https://qiita.com/nowokay/items/e42a7c7f403fd5f85d16)
* [Java 21新機能まとめ](https://qiita.com/nowokay/items/174f75b9e48cc7a80838)
* [Java 22新機能まとめ](https://qiita.com/nowokay/items/3b8307a911f014038873)
* [Java 23新機能まとめ](https://qiita.com/nowokay/items/7650b959fd4b0be54751)
* [Java 24新機能まとめ](https://qiita.com/nowokay/items/03e5d720433bd4ba0eec)
* [Java 25新機能まとめ](https://qiita.com/nowokay/items/7e05b4c42ded043a298a)
* [JEPs in JDK 21 integrated since JDK 17](https://openjdk.org/projects/jdk/21/jeps-since-jdk-17)
* [JEPs in JDK 25 integrated since JDK 21](https://openjdk.org/projects/jdk/25/jeps-since-jdk-21)
* [JEP 440: Record Patterns](https://openjdk.org/jeps/440)
* [JEP 441: Pattern Matching for switch](https://openjdk.org/jeps/441)
* [JEP 456: Unnamed Variables & Patterns](https://openjdk.org/jeps/456)
* [JEP 467: Markdown Documentation Comments](https://openjdk.org/jeps/467)
* [JEP 511: Module Import Declarations](https://openjdk.org/jeps/511)
* [JEP 513: Flexible Constructor Bodies](https://openjdk.org/jeps/513)

# コード

<details><summary>static members in inner class</summary><div>

```java
// Java 16 (JEP 395): inner classes may declare static members
public class StaticInInner {
    class Inner {
        static int counter = 0;

        static class Nested {
        }

        static int increment() {
            return ++counter;
        }

        record Pair(Position first, Position second) {
        }
    }

    public static void main(String[] args) {
        var outer = new StaticInInner();
        var inner = outer.new Inner();
        System.out.println(inner.getClass().getSimpleName());
        System.out.println(Inner.increment());
        System.out.println(Inner.increment());
        System.out.println(new Inner.Nested().getClass().getSimpleName());
        System.out.println(new Inner.Pair(new Position(1, 2), new Position(3, 4)));
    }
}
```

</div></details>

<details><summary>Sealed Interface</summary><div>

```java
// Java 17 (JEP 409): sealed interface with explicit permits clause
public sealed interface SealedInterface
        permits SealedInterface.Circle, SealedInterface.Rectangle, SealedInterface.Other {

    double area();

    record Circle(double radius) implements SealedInterface {
        @Override
        public double area() {
            return Math.PI * radius * radius;
        }
    }

    record Rectangle(Position topLeft, Position bottomRight) implements SealedInterface {
        @Override
        public double area() {
            return Math.abs((bottomRight.x() - topLeft.x()) * (bottomRight.y() - topLeft.y()));
        }
    }

    non-sealed interface Other extends SealedInterface {
    }

    // Java 21 (JEP 441): exhaustive switch over a sealed hierarchy, no default needed
    static String describe(SealedInterface shape) {
        return switch (shape) {
            case Circle c -> "circle r=" + c.radius();
            case Rectangle(Position tl, Position br) -> "rectangle " + tl + " - " + br;
            case Other o -> "other with area " + o.area();
        };
    }

    static void main(String[] args) {
        SealedInterface[] shapes = {
                new Circle(1),
                new Rectangle(new Position(0, 0), new Position(2, 3)),
                (Other) () -> 42d,
        };
        for (var shape : shapes) {
            System.out.println(describe(shape) + " => " + shape.area());
        }
    }
}
```

</div></details>

<details><summary>Record Patterns</summary><div>

```java
// Java 21 (JEP 440): record patterns (preview in 19 and 20)
public class RecordPattern {
    public record Point(int x, int y) {
        public static Point of(Position position) {
            return new Point(position.x(), position.y());
        }
    }

    public record Line(Point start, Point end) {
    }

    public record Box<T>(T content) {
    }

    public static void main(String[] args) {
        Object obj = new Line(Point.of(new Position(0, 0)), Point.of(new Position(3, 4)));

        // nested record pattern with instanceof, mixing `var` and explicit types
        if (obj instanceof Line(Point(var x1, var y1), Point(int x2, int y2))) {
            System.out.println("Length: " + Math.hypot(x2 - x1, y2 - y1));
        }

        // record pattern in switch with a guard
        String description = switch (obj) {
            case Line(Point s, Point e) when s.equals(e) -> "degenerate line at " + s;
            case Line(Point s, Point e) -> "line from " + s + " to " + e;
            case Point p -> "point " + p;
            default -> "unknown";
        };
        System.out.println(description);

        // type inference for generic record pattern
        Box<String> box = new Box<>("hello");
        if (box instanceof Box(var content)) {
            System.out.println(content.toUpperCase());
        }
    }
}
```

</div></details>

<details><summary>Pattern Matching for <code>switch</code></summary><div>

```java
import java.time.DayOfWeek;
import java.util.List;

// Java 21 (JEP 441): pattern matching for switch (preview in 17, 18, 19 and 20)
public class SwitchPattern {
    public static void main(String[] args) {
        Object[] values = {null, "", "text", 42, 7, 3.14, List.of(1, 2), new Position(1, 2)};
        for (Object value : values) {
            System.out.println(describe(value));
        }
        System.out.println(dayKind(DayOfWeek.SATURDAY));
        System.out.println(dayKind(DayOfWeek.WEDNESDAY));
        System.out.println(nullOrDefault(null));
        System.out.println(nullOrDefault(1L));
    }

    static String describe(Object value) {
        return switch (value) {
            case null -> "null!";
            case String s when s.isEmpty() -> "empty string";
            case String s -> "string of length " + s.length();
            case Integer i when i > 10 -> "big int " + i;
            case Integer i -> "int " + i;
            case List<?> list -> "list of size " + list.size();
            case Position p -> "scala case class Position(" + p.x() + ", " + p.y() + ")";
            default -> "something else: " + value.getClass().getSimpleName();
        };
    }

    // Java 21 (JEP 441): qualified enum constants as case labels
    static String dayKind(DayOfWeek day) {
        return switch (day) {
            case DayOfWeek.SATURDAY, DayOfWeek.SUNDAY -> "holiday";
            case DayOfWeek d -> "weekday " + d;
        };
    }

    // Java 21 (JEP 441): `case null, default`
    static String nullOrDefault(Object value) {
        return switch (value) {
            case Integer i -> "int " + i;
            case null, default -> "null or default";
        };
    }
}
```

</div></details>

<details><summary>Unnamed Variables &amp; Patterns</summary><div>

```java
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

// Java 22 (JEP 456): unnamed variables & patterns (preview in 21)
public class UnnamedVariable {
    public record Pair(Position first, Position second) {
    }

    public static void main(String[] args) {
        // unnamed local variable
        var _ = new Position(0, 0);

        // unnamed variable in enhanced for
        int count = 0;
        for (var _ : List.of("a", "b", "c")) {
            count++;
        }
        System.out.println("count = " + count);

        // unnamed exception parameter
        try {
            Integer.parseInt("not a number");
        } catch (NumberFormatException _) {
            System.out.println("not a number");
        }

        // unnamed lambda parameters
        BiFunction<Integer, Integer, Integer> first = (a, _) -> a;
        System.out.println("first = " + first.apply(1, 2));
        Map.of("key", 1).forEach((_, v) -> System.out.println("value = " + v));

        // unnamed pattern variable and unnamed pattern
        Object obj = new Pair(new Position(1, 2), new Position(3, 4));
        if (obj instanceof Pair(Position f, _)) {
            System.out.println("first = " + f);
        }
        switch (obj) {
            case Pair(_, Position s) -> System.out.println("second = " + s);
            default -> System.out.println("not a pair");
        }

        // multiple patterns in a single case label (allowed when they declare no bindings)
        String kind = switch (obj) {
            case Pair _, String _ -> "pair or string";
            default -> "other";
        };
        System.out.println(kind);
    }
}
```

</div></details>

<details><summary>Markdown Documentation Comments</summary><div>

```java
/// Java 23 (JEP 467): Markdown documentation comments.
///
/// A `///` comment is a documentation comment written in **Markdown**.
///
/// - works on classes, methods and fields
/// - links such as [Position] and [java.util.List] are allowed
///
/// ```java
/// MarkdownDoc.main(new String[0]);
/// ```
public class MarkdownDoc {
    /// Doubles the coordinates of the given [Position].
    ///
    /// @param position the position to scale
    /// @return the scaled position
    static Position twice(Position position) {
        return Position.apply(position.x() * 2, position.y() * 2);
    }

    public static void main(String[] args) {
        System.out.println(twice(new Position(1, 2)));
    }
}
```

</div></details>

<details><summary>Module Import Declarations</summary><div>

リポジトリではファイルごとコメントアウトしています。コメントを外すと下のエラーになります。

```java
// Java 25 (JEP 511): module import declarations (preview in 23 and 24)
import module java.base;

// Uses List, Map, TreeMap, Collectors, Function and LocalDate
// without single-type-import declarations.
public class ModuleImport {
    public static void main(String[] args) {
        List<Position> positions = List.of(new Position(3, 4), new Position(1, 2));
        Map<Integer, Position> byX = positions.stream()
                .collect(Collectors.toMap(Position::x, Function.identity()));
        System.out.println(new TreeMap<>(byX));
        System.out.println(LocalDate.of(2026, 10, 3).getDayOfWeek());
    }
}
```

</div></details>

<details><summary>Flexible Constructor Bodies</summary><div>

```java
// Java 25 (JEP 513): flexible constructor bodies (preview in 22, 23 and 24)
public class FlexibleConstructor {
    static class Base {
        final String label;

        Base(String label) {
            this.label = label;
            // virtual call from the super constructor: sees fields initialized before super()
            describe();
        }

        void describe() {
            System.out.println("Base.describe label=" + label);
        }
    }

    static class Derived extends Base {
        private final Position position;

        Derived(Position position) {
            // statements before super(): validation, field initialization, local variables
            if (position.x() < 0 || position.y() < 0) {
                throw new IllegalArgumentException("coordinate must be 0 or positive: " + position);
            }
            this.position = position;
            var label = "(" + position.x() + ", " + position.y() + ")";
            super(label);
            System.out.println("Derived constructed: " + this.label);
        }

        Derived(int x, int y) {
            // statements before this()
            var position = new Position(x, y);
            System.out.println("Delegating with " + position);
            this(position);
        }

        @Override
        void describe() {
            System.out.println("Derived.describe position=" + position);
        }
    }

    public static void main(String[] args) {
        new Derived(1, 2);
        try {
            new Derived(new Position(-1, 0));
        } catch (IllegalArgumentException e) {
            System.out.println("Rejected: " + e.getMessage());
        }
    }
}
```

</div></details>

<details><summary>Scala から Java の record と sealed を使う</summary><div>

```scala
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
```

matchによるパターンマッチで、sealedの網羅性チェックが2.13.18と3.9.0でなされます。

</div></details>

### 呼び出し元

```scala
case class Position(x: Int, y: Int)

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
```

# 結果

## 実行結果

`PatternMatch` と `SwitchExpression` の出力は実行時刻で変わります。
以下は 2026/10/03 の実行で、2.13.18、3.3.8、3.9.0 とも同じ出力でした。
コンパイルできない `ModuleImport` はコード上でコメントアウトして実行しています。

```
----------RecordExample----------
RecordExample[pos=Position(0,0)]
----------HasPrivate----------
8.0
----------PatternMatch----------
Listclass java.util.ImmutableCollections$ListN
----------SealedExample----------
class jp.co.yumemi.koma.SealedExample
class jp.co.yumemi.koma.SealedExample$Child1
class jp.co.yumemi.koma.SealedExample$Child2
class jp.co.yumemi.koma.SealedExample$Child3
----------SealedInterface----------
circle r=1.0 => 3.141592653589793
rectangle Position(0,0) - Position(2,3) => 6.0
other with area 42.0 => 42.0
----------ScalaSealedClass----------
class jp.co.yumemi.koma.ScalaSealedClass$Child1
class jp.co.yumemi.koma.ScalaSealedClass$Child2
class jp.co.yumemi.koma.ScalaSealedClass$Child3
----------SwitchExpression----------
Count: 2
Holiday!
Before holiday
Work
----------TextBlock----------
This is an example of multi line string.
This file may be called from scala file.
----------StaticInInner----------
Inner
1
2
Nested
Pair[first=Position(1,2), second=Position(3,4)]
----------RecordPattern----------
Length: 5.0
line from Point[x=0, y=0] to Point[x=3, y=4]
HELLO
----------SwitchPattern----------
null!
empty string
string of length 4
big int 42
int 7
something else: Double
list of size 2
scala case class Position(1, 2)
holiday
weekday WEDNESDAY
null or default
null or default
----------UnnamedVariable----------
count = 3
not a number
first = 1
value = 1
first = Position(1,2)
second = Position(3,4)
pair or string
----------MarkdownDoc----------
Position(2,4)
----------FlexibleConstructor----------
Delegating with Position(1,2)
Derived.describe position=Position(1,2)
Derived constructed: (1, 2)
Rejected: coordinate must be 0 or positive: Position(-1,0)
----------UseJavaFromScala----------
1,2 -> 3,4
Circle(2.0) => circle r=2.0
Rectangle(Position(0,0), Position(1,1)) => rectangle Position(0,0) - Position(1,1)
```

## まとめ

| Function                                   | 2.13.18 | 3.3.8 | 3.9.0 |
|--------------------------------------------|:-------:|:-----:|:-----:|
| `var`                                      |    ✅    |   ✅   |   ✅   |
| private method in interface                |    ✅    |   ✅   |   ✅   |
| Pattern Matching for `instanceof`          |    ✅    |   ✅   |   ✅   |
| Records                                    |    ✅    |   ✅   |   ✅   |
| static members in inner class              |    ✅    |   ✅   |   ✅   |
| Sealed Classes                             |    ✅    |   ✅   |   ✅   |
| Sealed Interface (`permits`)               |    ✅    |   ✅   |   ✅   |
| Switch Expressions                         |    ✅    |   ✅   |   ✅   |
| Text Blocks                                |    ✅    |   ✅   |   ✅   |
| Record Patterns                            |    ✅    |   ✅   |   ✅   |
| Pattern Matching for `switch`              |    ✅    |   ✅   |   ✅   |
| Unnamed Variables & Patterns               |    ✅    |   ✅   |   ✅   |
| Markdown Documentation Comments            |    ✅    |   ✅   |   ✅   |
| Module Import Declarations                 |    ❌    |   ❌   |   ❌   |
| Flexible Constructor Bodies                |    ✅    |   ✅   |   ✅   |

Records と Sealed Classes は前回の記事の時点ではコンパイルできないバージョンがありましたが、今回の 3 バージョンではすべてコンパイルできました。
Sealed interface についても同様にコンパイル可能です。

今回の範囲でコンパイルできなかったのは Module Import Declarations (`import module java.base;`) だけです。
javac 自体はこの構文を受け付けます。
`Mixed` では scalac / dotty も Java ソースを読んでシグネチャを取るため、そのパーサが知らない構文で落ちます。
`sealed`、`permits`、`non-sealed`、`record`、`import module` のように宣言に現れる構文は、Scala 側の対応が必要です。

Scala から Java の sealed 型を `match` したときの網羅性チェックは、2.13.18 と 3.9.0 では警告が出ます。
`UseJavaFromScala` の `Other` の分岐を外すと `match may not be exhaustive` になります。
3.3.8 はこの警告を出しません。

## 備考

### 動作確認のリポジトリ

https://github.com/k-kojima-yumemi/stunning-octo-chainsaw

### Module Import Declarations

2.13.18

```
[error] ModuleImport.java:4:15: `;` expected but identifier found.
[error] import module java.base;
[error]               ^
```

3.3.8 と 3.9.0

```
[error] -- Error: ModuleImport.java:4:14 ---
[error] 4 |import module java.base;
[error]   |              ^^^^
[error]   |              ';' expected but identifier found.
```

行番号は、コメントを外したあとのファイルでの位置です。

### 2.13

* Sealed Classes
  * https://github.com/scala/scala/pull/10105
  * https://github.com/scala/scala/pull/10348
  * 2.13.11 で使えるようになった
* Records
  * https://github.com/scala/scala/pull/9551
  * 2.13.7 で使えるようになった。前回の 2.13.10 でもコンパイルできていた

### 3

* Sealed Classes
  * https://github.com/scala/scala3/pull/19080 (3.4.2)
  * LTS へのバックポートは https://github.com/scala/scala3/pull/20943 。3.3.8 では使えた
* Records
  * https://github.com/scala/scala3/pull/16762 (3.3.1)
  * 前回の 3.2.2 ではコンパイルできなかった
* Java の sealed に対する網羅性チェック
  * https://github.com/scala/scala3/pull/25788 (3.9.0)
  * 3.3.8 には入っていない
* Java の record を Scala のパターンで分解する (`case Point(x, y) =>`) のは 3.10.0 の予定で、今回は未確認
  * https://github.com/scala/scala3/pull/26497
