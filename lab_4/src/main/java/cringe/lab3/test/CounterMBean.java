package cringe.lab3.test;

import javax.management.MXBean;

@MXBean
public interface CounterMBean {
    void update(boolean condition);
    int getTotalPoints();
    int getHitPoints();
    int getMissPoints();
    void drop();
}
