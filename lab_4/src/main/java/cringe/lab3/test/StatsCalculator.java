package cringe.lab3.test;

import cringe.lab3.db.DBManager;
import cringe.lab3.entity.Point;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.util.List;

@Named("calculator")
@ApplicationScoped
public class StatsCalculator {
    @Inject
    private DBManager dbManager;

    private int totalPoints = 0;
    private int missPoints = 0;

    public void update() {
        List<Point> points = dbManager.getPoints();
        for (Point point : points) {
            totalPoints++;
            if (!point.isCondition()) {
                missPoints++;
            }
        }
    }

    public float getPercents() {
        return totalPoints == 0 ? 0.0f : (missPoints * 100.0f) / totalPoints;
    }
}
