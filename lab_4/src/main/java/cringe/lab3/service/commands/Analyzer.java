package cringe.lab3.service.commands;


public class Analyzer {

    int getPercent(int pointCounter, int missPoints) {
        return (pointCounter * 100) / missPoints;
    }
}
