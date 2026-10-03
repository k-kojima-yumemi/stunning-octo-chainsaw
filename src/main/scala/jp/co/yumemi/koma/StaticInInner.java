package jp.co.yumemi.koma;

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
