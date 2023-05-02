package jp.co.yumemi.koma;


public record RecordExample(Position pos) {
    public RecordExample {
        if (pos.x() < 0 || pos.y() < 0)
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
