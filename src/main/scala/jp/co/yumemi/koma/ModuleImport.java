package jp.co.yumemi.koma;

// Java 25 (JEP 511): module import declarations (preview in 23 and 24)
//
// The Java parser of scalac (2.13.18) and dotty (3.3.8, 3.9.0) does not accept `import module`,
// so this file is commented out to keep the mixed compilation working.
// Uncomment this file and the call in CompileCheckMain to reproduce:
//   ModuleImport.java:4:15: `;` expected but identifier found.
/*
import module java.base;

// Uses List, Map, TreeMap, Collectors, Function and LocalDate
// without single-type-import declarations.
public class ModuleImport {
    public static void main(String[] args) {
        List<Position> positions = List.of(new Position(3, 4), new Position(1, 2));
        Map<Integer, Position> byX = positions.stream()
                .collect(Collectors.toMap(Position::x, Function.identity()));
        System.out.println(new TreeMap<>(byX));
        System.out.println(LocalDate.of(2026, 10, 3).getDayOfWeek());
    }
}
*/
