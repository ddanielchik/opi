package cringe.lab3;

import cringe.lab3.entity.Point;
import cringe.lab3.service.commands.AreaChecker;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;

public class PointTest {
    private AreaChecker checker;

    @BeforeEach
    void setUp() {
        checker = new AreaChecker();
        System.out.println("\n=== Инициализация AreaChecker ===");
    }

    @Test
    void checkCondition() {
        Point p1 = new Point(1, 2, 3);
        Point p2 = new Point(0, 0, 4);
        Point p3 = new Point(-1, -1, 5);

        System.out.println("Тестируемые точки:");
        System.out.println("p1: " + p1 + " (ожидается: не попадает в область)");
        System.out.println("p2: " + p2 + " (ожидается: попадает в область)");
        System.out.println("p3: " + p3 + " (ожидается: попадает в область)");

        System.out.println("\nЗапуск AreaChecker...");
        checker.action(List.of(p1, p2, p3));

        System.out.println("\nРезультаты:");
        assertFalse(p1.isCondition(), "Точка 1 (" + p1 + ") не должна попадать в область");
        System.out.println("p1: корректно не попала в область");

        assertTrue(p2.isCondition(), "Точка 2 (" + p2 + ") должна попадать в область");
        System.out.println("p2: корректно попала в область");

        assertTrue(p3.isCondition(), "Точка 3 (" + p3 + ") должна попадать в область");
        System.out.println("p3: корректно попала в область");

        System.out.println("\nТест пройден успешно!");
    }
}
