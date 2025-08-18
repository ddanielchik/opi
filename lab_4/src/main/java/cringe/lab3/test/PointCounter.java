package cringe.lab3.test;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import org.primefaces.PrimeFaces;

import javax.management.MBeanServer;
import javax.management.Notification;
import javax.management.NotificationBroadcasterSupport;
import javax.management.ObjectName;
import java.io.Serializable;
import java.lang.management.ManagementFactory;

@Named("counter")
@ApplicationScoped
public class PointCounter extends NotificationBroadcasterSupport implements CounterMBean, Serializable {
    private int totalPoints = 0;
    private int hitPoints = 0;
    private int missPoints = 0;
    private int consecutiveMisses = 0;
    private long sequenceNumber = 1;

    @PostConstruct
    public void init() {
        try {
            MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
            ObjectName name = new ObjectName("cringe.lab3:type=PointCounter");

            if (!mbs.isRegistered(name)) {
                mbs.registerMBean(this, name);
                System.out.println("PointCounter MBean registered successfully");
            }
        } catch (Exception e) {
            System.err.println("Error during PointCounter initialization:");
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize PointCounter", e);
        }
    }

    @Override
    public void update(boolean condition) {
        totalPoints ++;

        if (condition) {
            hitPoints++;
            consecutiveMisses = 0;
        } else {
            missPoints++;
            consecutiveMisses++;

            if (consecutiveMisses >= 3) {
                consecutiveMisses = 0;

                sendJmxNotification();
                showUiNotification();
            }
        }
    }

    private void sendJmxNotification() {
        sendNotification(new Notification(
                "three.misses.in.row",
                this,
                sequenceNumber++,
                System.currentTimeMillis(),
                "User made 3 consecutive misses!"
        ));
    }

    private void showUiNotification() {
        if (FacesContext.getCurrentInstance() != null) {
            PrimeFaces.current().executeScript(
                    "PF('missDialog').jq.css('top','20px').css('left','20px');" +
                            "PF('missDialog').show();" +
                            "setTimeout(function(){PF('missDialog').hide()}, 5000)"
            );
        }
    }

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
    public void drop() {
        totalPoints = 0;
        hitPoints = 0;
        missPoints = 0;
        consecutiveMisses = 0;
    }
}
