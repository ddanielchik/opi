package cringe.lab3.test;

import javax.management.MXBean;

@MXBean
public interface CalculatorMBean {
    void update();
    float getPercents();
}
