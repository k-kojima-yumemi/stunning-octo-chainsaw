package jp.co.yumemi.koma;

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
