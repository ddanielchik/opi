package cringe.lab3.test;

import cringe.lab3.entity.Point;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import org.primefaces.PrimeFaces;

import javax.management.Notification;
import javax.management.NotificationBroadcasterSupport;
import java.io.Serializable;

@Named("counter")
@ApplicationScoped
public class PointCounter extends NotificationBroadcasterSupport implements CounterMBean, Serializable {
    private int totalPoints = 0;
    private int hitPoints = 0;
    private int missPoints = 0;
    private int consecutiveMisses = 0;
    private long sequenceNumber = 1;

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
