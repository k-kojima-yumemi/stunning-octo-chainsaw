package jp.co.yumemi.koma;

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
