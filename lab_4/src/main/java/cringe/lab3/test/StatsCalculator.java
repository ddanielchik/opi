package cringe.lab3.test;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;

@Named("calculator")
@ApplicationScoped
public class StatsCalculator {
    private float percents = 0.0f;

    public synchronized void setPercents(int totalPoints, int missPoints) {
        percents = totalPoints == 0 ? 0.0f : (missPoints * 100.0f) / totalPoints;
        System.out.println("Updated percents: " + percents);
    }

    public synchronized float getPercents() {
        return percents;
    }
}
