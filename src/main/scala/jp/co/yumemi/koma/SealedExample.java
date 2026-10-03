package jp.co.yumemi.koma;

// Java 17 (JEP 409): sealed class with implicitly permitted subclasses
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
