package jp.co.yumemi.koma;

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
