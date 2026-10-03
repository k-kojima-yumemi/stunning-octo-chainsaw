package jp.co.yumemi.koma;

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
