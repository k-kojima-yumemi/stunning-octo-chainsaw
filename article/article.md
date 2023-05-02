# はじめに

Scalaを使用している方々はほぼ全てのコードをScalaで書くでしょう。
しかしBetter JavaとしてScalaを使っている人には、時にJavaのライブラリの制約によってJavaをScalaのプロジェクトに入れなければならない場合があります。
そのような場合に、Javaの最近導入された機能たちがScala環境でコンパイルできるかを検証します。

# 環境

## Java

```shell
$ java --version
openjdk 17.0.7 2023-04-18
OpenJDK Runtime Environment Temurin-17.0.7+7 (build 17.0.7+7)
OpenJDK 64-Bit Server VM Temurin-17.0.7+7 (build 17.0.7+7, mixed mode)
```

## SBT

```shell
$ sbt --version
sbt version in this project: 1.8.2
sbt script version: 1.8.2
```

## Scala

* 2.13.10
* 3.2.2

# 対象の機能

* `var`
* private method in interface
* Pattern Matching for `instanceof`
* Records
* Sealed Classes
* Switch Expressions
* Text Blocks

各機能の詳細は省きます。

## 参考

* [Java8からJava11への変更点](https://qiita.com/nowokay/items/1ce24079f4daafc73b4a)
* [Java 12新機能まとめ](https://qiita.com/nowokay/items/0e860819b6ffb1aca90a)
* [Java 13新機能まとめ](https://qiita.com/nowokay/items/3e1625a77cb435394547)
* [Java 14新機能まとめ](https://qiita.com/nowokay/items/ec85d97a7cecaaac8123)
* [Java 15新機能まとめ](https://qiita.com/nowokay/items/2858699bc1cd89222cd8)
* [Java 16新機能まとめ](https://qiita.com/nowokay/items/215769cdcb14d6c5412f)
* [Java 17新機能まとめ](https://qiita.com/nowokay/items/ec58bf8f30d236a12acb)
* [JEPs in JDK 17 integrated since JDK 11](https://openjdk.org/projects/jdk/17/jeps-since-jdk-11)

# コード

<details><summary>private method in interface</summary><div>

```java
public interface HasPrivate {
    default double pow(Position position) {
        return Math.pow(position.x(), this.internalParameter());
    }

    private double internalParameter() {
        return 3d;
    }

    static void main(String[] args) {
        var hasPrivate = new HasPrivate() {
        };
        System.out.println(hasPrivate.pow(new Position(2, 2)));
    }
}
```

</div></details>

<details><summary>Pattern Matching for <code>instanceof</code></summary><div>

```java
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

public class PatternMatch {
    public static void main(String[] args) {
        var something = returnSomething(LocalTime.now());
        if (something instanceof String s) {
            System.out.println("String! " + s);
        } else if (something instanceof List<?>) {
            System.out.println("List" + something.getClass());
        } else if (something instanceof Map<?, ?> m && m.isEmpty()) {
            System.out.println("Empty map, " + m.getClass());
        } else if (something instanceof Position p) {
            System.out.println(p);
        } else {
            throw new IllegalStateException("What?");
        }
    }

    private static Object returnSomething(LocalTime time) {
        return switch (time.getNano() % 5) {
            case 0, 1 -> "a";
            case 2 -> List.of();
            case 3 -> Map.of();
            case 4 -> new Position(time.getHour(), time.getMinute());
            default -> throw new AssertionError("unreachable");
        };
    }
}
```

</div></details>

<details><summary>Records</summary><div>

```java
public record RecordExample(Position pos) {
    public RecordExample {
        if (this.pos().x() < 0 || this.pos().y() < 0)
            throw new IllegalArgumentException("coordinate must be 0 or positive");
    }

    public RecordExample(int x, int y) {
        this(Position.apply(x, y));
    }

    public static void main(String[] args) {
        var pos = new RecordExample(0, 0);
        System.out.println(pos);
    }
}
```

</div></details>

<details><summary>Sealed Classes</summary><div>

```java
public sealed class SealedExample {

    public static void main(String[] args) {
        var example = new SealedExample();
        System.out.println(example.getClass());
        System.out.println(new Child1().getClass());
        System.out.println(new Child2().getClass());
        System.out.println(example.new Child3().getClass());
    }

    public static final class Child1 extends SealedExample {
    }

    public static non-sealed class Child2 extends SealedExample {
    }

    non-sealed class Child3 extends SealedExample {
    }
}
```

</div></details>

<details><summary>Sealed Classes of Scala</summary><div>

```scala
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

```

</div></details>

<details><summary>Switch Expressions</summary><div>

```java
import java.time.LocalDate;
import java.time.LocalTime;

public class SwitchExpression {
    public static void main(String[] args) {
        int a = switch (LocalTime.now().getSecond() % 3) {
            case 0 -> 4;
            case 1 -> {
                int min = LocalTime.now().getMinute();
                yield min + LocalTime.now().getSecond();
            }
            case 2 -> 2;
            default -> throw new AssertionError("unreachable");
        };
        System.out.println("Count: " + a);

        switch (LocalDate.now().getDayOfWeek()) {
            case SATURDAY, SUNDAY:
                System.out.println("Holiday!");
            case FRIDAY:
                System.out.println("Before holiday");
            default:
                System.out.println("Work");
        }
    }
}
```

</div></details>

<details><summary>Text Blocks</summary><div>

```java
public class TextBlock {
    public static void main(String[] args) {
        System.out.println("""
            This is an example of multi line string.
            This file may be called from scala file.""");
    }
}

```

</div></details>

<details><summary>呼び出し元</summary><div>

```scala

case class Position(x: Int, y: Int)

object CompileCheckMain {

  def main(args: Array[String]): Unit = {
    // Compile error in 3.2.2
    println("-" * 10 + "RecordExample" + "-" * 10)
    RecordExample.main(args)
    println("-" * 10 + "HasPrivate" + "-" * 10)
    HasPrivate.main(args)
    println("-" * 10 + "PatternMatch" + "-" * 10)
    PatternMatch.main(args)
    // Compile error in both 2.13.10 and 3.2.2
    println("-" * 10 + "SealedExample" + "-" * 10)
    SealedExample.main(args)
    // Of course, scala sealed class can compile
    println("-" * 10 + "ScalaSealedClass" + "-" * 10)
    ScalaSealedClass.main(args)
    println("-" * 10 + "SwitchExpression" + "-" * 10)
    SwitchExpression.main(args)
    println("-" * 10 + "TextBlock" + "-" * 10)
    TextBlock.main(args)
  }
}

```

</div></details>

# 結果

| Function                          | 2.13.10 | 3.2.2 |
|-----------------------------------|:-------:|:-----:|
| `var`                             |    ✅    |   ✅   |
| private method in interface       |    ✅    |   ✅   |
| Pattern Matching for `instanceof` |    ✅    |   ✅   |
| Records                           |    ✅    |   ❌   |
| Sealed Classes                    |    ❌    |   ❌   |
| Switch Expressions                |    ✅    |   ✅   |
| Text Blocks                       |    ✅    |   ✅   |

Recordが3.2.2でサポートされておらず、JavaのSealed classは現状のバージョンではサポートされていませんでした。
Sealed classはInterfaceであっても同じ結果となります。

## 備考

### 2.13

* Sealed Classes
  * https://github.com/scala/bug/issues/12159
  * https://github.com/scala/bug/issues/12171
  * https://github.com/scala/scala/pull/10105
  * https://github.com/scala/scala/pull/10348
  * 2.13.11で使えるようになる可能性
* Records
  * https://github.com/scala/bug/issues/11908
  * https://github.com/scala/bug/issues/12474
  * https://github.com/scala/scala/pull/9551
* Text Blocks
  * https://github.com/scala/bug/issues/12290
  * https://github.com/scala/scala/pull/9548

### 3

* Sealed Classes
  * ???
* Records
  * https://github.com/lampepfl/dotty/issues/14846
  * https://github.com/lampepfl/dotty/pull/16762
