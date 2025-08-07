package cringe.lab3.test;

public interface StatsMBean {
    int getTotalPoints();
    int getHitPoints();
    int getMissPoints();
    int getConsecutiveMisses();
    void resetConsecutiveMisses();
}
