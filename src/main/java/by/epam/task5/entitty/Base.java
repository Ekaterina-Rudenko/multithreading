package by.epam.task5.entitty;

import by.epam.task5.exception.CustomException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class Base {
    static final Logger logger = LogManager.getLogger();
    private static Base instance;
    private static ReentrantLock lock = new ReentrantLock();
    private static final AtomicBoolean isExist = new AtomicBoolean(false);
    private final Deque<Terminal> terminalDeque = new ArrayDeque<>();
    private static final int NUMBER_OF_TERMINALS = 10;
    private static final int MAX_WAREHOUSE_CAPACITY = 1000;
    private static final AtomicInteger currentWarehouseSize = new AtomicInteger();
    private Semaphore semaphore;

    private Base() {
        for (int i = 0; i < NUMBER_OF_TERMINALS; i++) {
            terminalDeque.add(new Terminal());
        }
        currentWarehouseSize.set(200);
    }

    private void init() {

    }

    public static Base getInstance() {
        if (!isExist.get()) {
            try {
                lock.lock();
                if (instance == null) {
                    instance = new Base();
                    isExist.set(true);
                }
            } finally {
                lock.unlock();
            }
        }
        return instance;
    }

    public Terminal getTerminal(Truck truck) throws CustomException {
        try {
            semaphore.acquire();
        } catch (InterruptedException e) {
            logger.error("");
            throw new CustomException();
        }
    }

    public void releaseTerminal(Terminal terminal) {
    }

    public void addToWarehouse(int loadSize) throws CustomException {
        while ((currentWarehouseSize.intValue() + loadSize) >= MAX_WAREHOUSE_CAPACITY) {
            try {
                TimeUnit.MILLISECONDS.sleep(500);
                logger.info("The warehouse is overloaded...");
            } catch (InterruptedException e) {
                logger.info("Operation was interrupted", e);
                throw new CustomException("Operation was interrupted", e);
            }
        }
        currentWarehouseSize.getAndAdd(loadSize);
        logger.info("The current size of warehouse is " + currentWarehouseSize.intValue());
    }

    public void removeFromWarehouse(int truckCapacity) throws CustomException {
        while ((currentWarehouseSize.intValue() - truckCapacity) < 0) {
            try {
                TimeUnit.MILLISECONDS.sleep(500);
                logger.info("The warehouse is overloaded...");
            } catch (InterruptedException e) {
                logger.info("Operation was interrupted", e);
                throw new CustomException("Operation was interrupted", e);
            }
        }
        currentWarehouseSize.getAndAdd(-truckCapacity);
        logger.info("The current size of warehouse is " + currentWarehouseSize.intValue());
    }

}