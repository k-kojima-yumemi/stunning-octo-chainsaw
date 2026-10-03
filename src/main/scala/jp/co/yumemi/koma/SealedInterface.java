package jp.co.yumemi.koma;

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
