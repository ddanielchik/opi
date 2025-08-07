package cringe.lab3.test;

import cringe.lab3.entity.Point;
import cringe.lab3.test.StatsMBeanImpl;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class StatsController implements Serializable {
    @Inject
    private StatsMBeanImpl statsMBean;

    public void updateStats(List<Point> points) {
        statsMBean.processPoints(points);
    }

    // Геттеры для отображения статистики в JSF
    public int getTotalPoints() {
        System.out.println(statsMBean.getTotalPoints());
        return statsMBean.getTotalPoints();
    }

    public int getHitPoints() {
        System.out.println(statsMBean.getHitPoints());
        return statsMBean.getHitPoints();
    }

    public int getMissPoints() {
        System.out.println(statsMBean.getMissPoints());
        return statsMBean.getMissPoints();
    }

    public int getConsecutiveMisses() {
        return statsMBean.getConsecutiveMisses();
    }

    public void resetConsecutiveMisses() {
        statsMBean.resetConsecutiveMisses();
    }
}