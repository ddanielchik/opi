package cringe.lab3.test;

import cringe.lab3.entity.Point;
import jakarta.enterprise.context.ApplicationScoped;

import javax.management.Notification;
import javax.management.NotificationBroadcasterSupport;
import java.util.List;

@ApplicationScoped
public class StatsMBeanImpl extends NotificationBroadcasterSupport implements StatsMBean {
    private int totalPoints;
    private int hitPoints;
    private int missPoints;
    private int consecutiveMisses;
    private long sequenceNumber = 1;

    @Override
    public int getTotalPoints() {
        return totalPoints;
    }

    @Override
    public int getHitPoints() {
        return hitPoints;
    }

    @Override
    public int getMissPoints() {
        return missPoints;
    }

    @Override
    public int getConsecutiveMisses() {
        return consecutiveMisses;
    }

    @Override
    public void resetConsecutiveMisses() {
        consecutiveMisses = 0;
    }

    public synchronized void processPoints(List<Point> points) {
        if (points == null || points.isEmpty()) return;

        totalPoints += points.size();

        for (Point point : points) {
            if (point.isCondition()) {
                hitPoints++;
                consecutiveMisses = 0;
            } else {
                missPoints++;
                consecutiveMisses++;

                if (consecutiveMisses >= 3) {
                    sendNotification(new Notification(
                            "three.misses.in.row",
                            this,
                            sequenceNumber++,
                            System.currentTimeMillis(),
                            "User made 3 consecutive misses!"
                    ));
                    consecutiveMisses = 0;
                }
            }
        }
    }
}