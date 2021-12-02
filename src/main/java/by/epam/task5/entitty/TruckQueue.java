package by.epam.task5.entitty;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

public class TruckQueue {
    static final Logger logger = LogManager.getLogger();
    private final Deque<Truck> truckQueue = new ArrayDeque<>();

    public Truck getTruckFromQueue() {
        Truck truck = truckQueue.poll();
        logger.info("Truck " + truck.getId() + " is waiting for terminal...");
        return truck;
    }

    public void addTruckListToQueue(List<Truck> truckList) {
        for (Truck t : truckList) {
            addTruckToQueue(t);
        }
        logger.info("All trucks were added to Queue");
    }

    public int size() {
        return truckQueue.size();
    }

    public Deque<Truck> getTruckQueue() {
        return truckQueue;
    }

    public void addTruckToQueue(Truck truck) {
        if (truck.isPerishable()) {
            truckQueue.addFirst(truck);
            logger.info("Truck " + truck.getId() + " with perishable goods was added to Queue");
        } else {
            truckQueue.addLast(truck);
            logger.info("Truck " + truck.getId() + " was added to Queue");
        }

    }
}
