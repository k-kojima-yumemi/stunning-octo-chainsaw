package jp.co.yumemi.koma;

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
        return switch (time.getSecond() % 5) {
            case 0, 1 -> "a";
            case 2 -> List.of();
            case 3 -> Map.of();
            case 4 -> new Position(time.getHour(), time.getMinute());
            default -> throw new AssertionError("unreachable");
        };
    }
}
