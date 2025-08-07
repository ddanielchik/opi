package cringe.lab3.test;

import cringe.lab3.test.StatsMBeanImpl;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import javax.management.*;
import java.lang.management.ManagementFactory;

@ApplicationScoped
public class AppConfig {
    @Inject
    private StatsMBeanImpl statsMBean;

    private ObjectName mbeanName;

    @PostConstruct
    public void init() {
        try {
            MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
            mbeanName = new ObjectName("cringe.lab3:type=Stats");
            mbs.registerMBean(statsMBean, mbeanName);
        } catch (Exception e) {
            throw new RuntimeException("Failed to register MBean", e);
        }
    }

    @PreDestroy
    public void cleanup() {
        try {
            ManagementFactory.getPlatformMBeanServer().unregisterMBean(mbeanName);
        } catch (Exception e) {
            // Log error
        }
    }
}
