package cringe.lab3.test;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;

import javax.management.*;
import java.lang.management.ManagementFactory;

@ApplicationScoped
public class MBeanActivator {

    @Inject
    Instance<Object> allBeans; // CDI будет инжектить все бины

    @PostConstruct
    public void init() {
        try {
            MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();

            for (Object bean : allBeans) {
                if (isMBean(bean)) {
                    registerMBean(bean, mbs);
                }
            }
        } catch (Exception e) {
            System.err.println("MBean activation failed:");
            e.printStackTrace();
        }
    }

    private boolean isMBean(Object bean) {
        Class<?>[] interfaces = bean.getClass().getInterfaces();
        for (Class<?> iface : interfaces) {
            if (iface.isAnnotationPresent(MXBean.class)) {
                return true;
            }
        }
        return false;
    }

    private void registerMBean(Object bean, MBeanServer mbs) throws Exception {
        String domain = bean.getClass().getPackageName();
        String type = bean.getClass().getSimpleName();
        ObjectName name = new ObjectName(domain + ":type=" + type);

        if (!mbs.isRegistered(name)) {
            mbs.registerMBean(bean, name);
            System.out.println("Registered MBean: " + name);
        }
    }
}