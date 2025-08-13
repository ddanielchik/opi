package cringe.lab3;

import cringe.lab3.entity.Point;
import cringe.lab3.service.commands.AreaChecker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PointTest {
    private AreaChecker checker;

    @BeforeEach
    void setUp() {
        checker = new AreaChecker();
        System.out.println("Инициализация AreaChecker");
    }

    @Test
    void checkCondition() {
        Point p1 = new Point(1, 2, 3);  // Не попадает
        Point p2 = new Point(0, 0, 4);  // Попадает
        Point p3 = new Point(-1, -1, 5); // Попадает

        System.out.println("Точки для проверки:");
        System.out.println("  - " + p1.isCondition() + " (ожидается: false)");
        System.out.println("  - " + p2.isCondition() + " (ожидается: true)");
        System.out.println("  - " + p3.isCondition() + " (ожидается: true)");

        System.out.println("Запуск AreaChecker.action()");
        checker.action(List.of(p1, p2, p3));

        System.out.println("Проверка результатов...");
        assertAll(
                () -> assertFalse(p1.isCondition(), "Точка 1 не должна попадать"),
                () -> assertTrue(p2.isCondition(), "Точка 2 должна попадать"),
                () -> assertTrue(p3.isCondition(), "Точка 3 должна попадать")
        );

        System.out.println("Тест пройден!");
    }
}