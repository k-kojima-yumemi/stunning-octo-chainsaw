package jp.co.yumemi.koma;

/// Java 23 (JEP 467): Markdown documentation comments.
///
/// A `///` comment is a documentation comment written in **Markdown**.
///
/// - works on classes, methods and fields
/// - links such as [Position] and [java.util.List] are allowed
///
/// ```java
/// MarkdownDoc.main(new String[0]);
/// ```
public class MarkdownDoc {
    /// Doubles the coordinates of the given [Position].
    ///
    /// @param position the position to scale
    /// @return the scaled position
    static Position twice(Position position) {
        return Position.apply(position.x() * 2, position.y() * 2);
    }

    public static void main(String[] args) {
        System.out.println(twice(new Position(1, 2)));
    }
}
