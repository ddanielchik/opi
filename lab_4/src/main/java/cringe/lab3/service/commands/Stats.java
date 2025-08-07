package cringe.lab3.service.commands;

import cringe.lab3.entity.Point;

import java.util.List;

public class Stats {
    private int pointCounter;
    private int missPoints;
    private int hitPoints;

    public Stats() {
        pointCounter = 0;
        missPoints = 0;
        hitPoints = 0;
    }

    public int getPointCounter() {
        return pointCounter;
    }

    public int getMissPoints() {
        return missPoints;
    }

    public int getHitPoints() {
        return hitPoints;
    }

    void updateStats(List<Point> points) {
        if (points.isEmpty()) {
            return;
        }

        pointCounter += points.size();

        long hits = points.stream()
                .filter(Point::isCondition)
                .count();

        hitPoints += hits;
        missPoints += points.size() - hits;
    }


}
