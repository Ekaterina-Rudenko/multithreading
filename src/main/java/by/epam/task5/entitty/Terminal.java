package by.epam.task5.entitty;

import by.epam.task5.exception.CustomException;
import by.epam.task5.util.IdGenerator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.TimeUnit;

public class Terminal {
    static final Logger logger = LogManager.getLogger();
    private final long terminalId;

    public Terminal() {
        this.terminalId = IdGenerator.generate();
    }

    public long getTerminalId() {
        return terminalId;
    }

    public void loadTruck(Truck truck) throws CustomException {
        Base base = Base.getInstance();
        logger.info("Truck " + truck.getId() + " is being loaded ... -" + truck.getCapacity());
        try {
            base.removeFromWarehouse(truck.getCapacity());
            TimeUnit.MILLISECONDS.sleep(300);
        } catch (InterruptedException e) {
            logger.error("Operation was interrupted", e);
            throw new CustomException("Operation was interrupted", e);
        }
    }

    public void unloadTruck(Truck truck) throws CustomException {
        Base base = Base.getInstance();
        logger.info("Truck " + truck.getId() + " is being unloaded... +" + truck.getLoadSize());
        try {
            base.addToWarehouse(truck.getLoadSize());
            TimeUnit.MILLISECONDS.sleep(300);
        } catch (InterruptedException e) {
            logger.error("Operation was interrupted", e);
            throw new CustomException("Operation was interrupted", e);
        }
    }

}
