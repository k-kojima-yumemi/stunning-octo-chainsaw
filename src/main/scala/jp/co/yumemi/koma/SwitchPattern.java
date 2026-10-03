package jp.co.yumemi.koma;

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
