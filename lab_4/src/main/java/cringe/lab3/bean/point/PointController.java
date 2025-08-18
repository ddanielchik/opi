package cringe.lab3.bean.point;

import cringe.lab3.db.DBManager;
import cringe.lab3.entity.Point;
import cringe.lab3.service.ServiceManager;
import cringe.lab3.service.ServicesName;
import cringe.lab3.test.PointCounter;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Named(value = "controller")
@ApplicationScoped
public class PointController implements Serializable {

    @Inject
    private CoordinateHandler coordinateHandler;

    @Inject
    private CheckBoxChecker checkBoxChecker;

    private transient ServiceManager serviceManager;
    private transient DBManager dbManager;

    @Inject
    private PointCounter pointCounter;

    private List<Point> cachedPoints = Collections.emptyList();
    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    @PostConstruct
    public void init() {
        this.serviceManager = new ServiceManager();
        this.dbManager = new DBManager();
        serviceManager.registerObserver(dbManager);

        loadInitialPoints();
    }

    public void save() {
        Float[] selectedCheckBoxes = checkBoxChecker.getSelectedCheckBoxes();
        List<Point> points = coordinateHandler.createPoints(selectedCheckBoxes);

        serviceManager.execute(ServicesName.AREA_CHECKER, points);
        serviceManager.execute(ServicesName.SAVE, points);
        addPointsToCache(points);

        points.forEach(point -> pointCounter.update(point.isCondition()));
    }

    public void delete() {
        lock.writeLock().lock();
        try {
            serviceManager.execute(ServicesName.DELETE, cachedPoints);

            cachedPoints = Collections.emptyList();

            pointCounter.drop();
        } finally {
            lock.writeLock().unlock();
        }
    }

    public List<Point> getPoints() {
        lock.readLock().lock();
        try {
            return new ArrayList<>(cachedPoints);
        } finally {
            lock.readLock().unlock();
        }
    }

    private void loadInitialPoints() {
        lock.writeLock().lock();
        try {
            cachedPoints = new ArrayList<>(dbManager.getPoints());
            cachedPoints.forEach(point -> pointCounter.update(point.isCondition()));
        } finally {
            lock.writeLock().unlock();
        }
    }

    private void addPointsToCache(List<Point> points) {
        lock.writeLock().lock();
        try {
            if (cachedPoints.isEmpty()) {
                cachedPoints = new ArrayList<>();
            }
            cachedPoints.addAll(points);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @PreDestroy
    public void destroy() {
        serviceManager.unregisterObserver(dbManager);
        if (dbManager != null) {
            dbManager.close();
        }
    }
}