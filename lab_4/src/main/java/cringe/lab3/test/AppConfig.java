package cringe.lab3.test;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import javax.management.*;
import java.lang.management.ManagementFactory;

@ApplicationScoped
public class AppConfig {
    @Inject
    private PointCounter pointCounter;

    @Inject
    private StatsCalculator statsCalculator;

    private ObjectName pointCounterName;
    private ObjectName statsCalculatorName;

    @PostConstruct
    public void init() {
        try {
            MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();

            pointCounterName = new ObjectName("cringe.lab3:type=PointCounter");
            mbs.registerMBean(pointCounter, pointCounterName);

            statsCalculatorName = new ObjectName("cringe.lab3:type=StatsCalculator");
            mbs.registerMBean(statsCalculator, statsCalculatorName);
        } catch (Exception e) {
            throw new RuntimeException("Failed to register MBeans", e);
        }
    }

    @PreDestroy
    public void cleanup() {
        try {
            MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();

            if (pointCounterName != null) {
                mbs.unregisterMBean(pointCounterName);
            }

            if (statsCalculatorName != null) {
                mbs.unregisterMBean(statsCalculatorName);
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to unregister MBeans", e);
        }
    }
}