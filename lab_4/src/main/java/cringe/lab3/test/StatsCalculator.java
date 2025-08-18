package cringe.lab3.test;

import cringe.lab3.db.DBManager;
import cringe.lab3.entity.Point;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import javax.management.*;
import java.lang.management.ManagementFactory;
import java.io.Serializable;
import java.util.List;

@Named("calculator")
@ApplicationScoped
public class StatsCalculator implements CalculatorMBean, Serializable {

    @Inject
    private DBManager dbManager;

    private volatile int totalPoints = 0;
    private volatile int missPoints = 0;

    @PostConstruct
    public void init() {
        try {
            MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
            ObjectName name = new ObjectName("cringe.lab3:type=StatsCalculator");

            if (!mbs.isRegistered(name)) {
                mbs.registerMBean(this, name);
                System.out.println("StatsCalculator MBean registered successfully");
            }

            update();

        } catch (Exception e) {
            System.err.println("Error during StatsCalculator initialization:");
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize StatsCalculator", e);
        }
    }

    @Override
    public synchronized void update() {
        if (dbManager != null) {
            List<Point> points = dbManager.getPoints();
            if (points != null) {
                int total = 0;
                int miss = 0;
                for (Point point : points) {
                    total++;
                    if (!point.isCondition()) {
                        miss++;
                    }
                }
                this.totalPoints = total;
                this.missPoints = miss;
            }
        }
    }

    @Override
    public float getPercents() {
        return totalPoints == 0 ? 0.0f : (missPoints * 100.0f) / totalPoints;
    }
}