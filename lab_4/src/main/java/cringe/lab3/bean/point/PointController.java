package cringe.lab3.bean.point;

import cringe.lab3.db.DBManager;
import cringe.lab3.entity.Point;

import cringe.lab3.service.ServiceManager;
import cringe.lab3.service.ServicesName;

import cringe.lab3.test.PointCounter;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.List;

@Named(value = "controller")
@SessionScoped
public class PointController implements Serializable {

    @Inject
    private CoordinateHandler coordinateHandler;

    @Inject
    private CheckBoxChecker checkBoxChecker;

    private transient ServiceManager serviceManager;
    private DBManager dbManager;

    @Inject
    private PointCounter pointCounter;

    @PostConstruct
    public void init() {
        this.serviceManager = new ServiceManager();
        this.dbManager = new DBManager();
        serviceManager.registerObserver(dbManager);
    }

    public void save() {
        Float[] selectedCheckBoxes = checkBoxChecker.getSelectedCheckBoxes();
        List<Point> points = coordinateHandler.createPoints(selectedCheckBoxes);

        serviceManager.execute(ServicesName.AREA_CHECKER, points);
        pointCounter.update(points);
        serviceManager.execute(ServicesName.SAVE, points);
    }

    public void delete() {
        serviceManager.execute(ServicesName.DELETE, dbManager.getPoints());
        pointCounter.drop();
    }

    public List<Point> getPoints() {
        List<Point> points = dbManager.getPoints();
        pointCounter.update(points);
        return points;
    }

    @PreDestroy
    public void destroy() {
        serviceManager.unregisterObserver(dbManager);
        dbManager.close();
    }
}
