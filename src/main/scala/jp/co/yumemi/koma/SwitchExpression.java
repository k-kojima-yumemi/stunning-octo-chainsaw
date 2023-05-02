package jp.co.yumemi.koma;

import java.time.LocalDate;
import java.time.LocalTime;

public class SwitchExpression {
    public static void main(String[] args) {
        int a = switch (LocalTime.now().getSecond() % 3) {
            case 0 -> 4;
            case 1 -> {
                int min = LocalTime.now().getMinute();
                yield min + LocalTime.now().getSecond();
            }
            case 2 -> 2;
            default -> throw new AssertionError("unreachable");
        };
        System.out.println("Count: " + a);

        switch (LocalDate.now().getDayOfWeek()) {
            case SATURDAY, SUNDAY:
                System.out.println("Holiday!");
            case FRIDAY:
                System.out.println("Before holiday");
            default:
                System.out.println("Work");
        }
    }
}
