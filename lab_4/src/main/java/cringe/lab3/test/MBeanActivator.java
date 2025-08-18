package cringe.lab3.test;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import javax.management.*;
import java.lang.management.ManagementFactory;

@ApplicationScoped
public class MBeanActivator {

    @Inject
    private StatsCalculator statsCalculator;

    @Inject
    private PointCounter pointCounter;

    @PostConstruct
    public void init() {
        try {
            MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();

            ObjectName statsName = new ObjectName("cringe.lab3:type=StatsCalculator");
            if (!mbs.isRegistered(statsName)) {
                mbs.registerMBean(statsCalculator, statsName);
                System.out.println("Registered StatsCalculator MBean");
            }

            ObjectName counterName = new ObjectName("cringe.lab3:type=PointCounter");
            if (!mbs.isRegistered(counterName)) {
                mbs.registerMBean(pointCounter, counterName);
                System.out.println("Registered PointCounter MBean");
            }

        } catch (Exception e) {
            System.err.println("MBean activation failed:");
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize MBeans", e);
        }
    }
}