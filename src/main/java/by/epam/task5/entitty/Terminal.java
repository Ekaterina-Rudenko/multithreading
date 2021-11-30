package by.epam.task5.entitty;

import by.epam.task5.exception.CustomException;
import by.epam.task5.util.IdGenerator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.TimeUnit;

public class Terminal {
    static final Logger logger = LogManager.getLogger();
    public final long terminalId;

    public Terminal() {
        this.terminalId = IdGenerator.generate();
    }

    public long getTerminalId() {
        return terminalId;
    }
    public void loadTruck(Truck truck) throws CustomException {
        Base base = Base.getInstance();
        base.removeFromWarehouse(truck.getCapacity());
        try{
            logger.info("Truck #" + truck.getId() + " is being loaded");
            TimeUnit.MILLISECONDS.sleep(300);
        } catch (InterruptedException e){
            logger.error("Operation was interrupted",e);
            throw new CustomException("Operation was interrupted", e);
        }
    }
    public void unloadTruck(Truck truck) throws CustomException {
            Base base = Base.getInstance();
            base.addToWarehouse(truck.getLoadSize());
            try{
                logger.info("Truck #" + truck.getId() + " is being unloaded");
                TimeUnit.MILLISECONDS.sleep(300);
            } catch (InterruptedException e){
                logger.error("Operation was interrupted", e);
                throw new CustomException("Operation was interrupted", e);
            }
        }

}
