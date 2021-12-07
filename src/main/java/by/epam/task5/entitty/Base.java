package by.epam.task5.entitty;

import by.epam.task5.exception.CustomException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Properties;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class Base {
    static Logger logger = LogManager.getLogger();
    private static final String BASE_PROPERTIES_PATH = "base.properties";
    private static Base instance;
    private static ReentrantLock lock = new ReentrantLock();
    private static ReentrantLock lockTerminal = new ReentrantLock();
    private static AtomicBoolean isExist = new AtomicBoolean(false);
    private final Deque<Terminal> terminalDeque = new ArrayDeque<>();
    private static AtomicInteger currentWarehouseSize = new AtomicInteger();
    private Semaphore semaphore;

    private final int NUMBER_OF_TERMINALS;
    private final int MAX_WAREHOUSE_CAPACITY;

    private final int DEFAULT_NUMBER_TERMINALS = 6;
    private final int DEFAULT_CAPACITY = 1000;

    private Base() {
        URL resource = getClass().getClassLoader().getResource(BASE_PROPERTIES_PATH);
        Properties properties = new Properties();
        if (resource != null) {
            File file = new File(resource.getFile());
            try (FileReader fileReader = new FileReader(file)) {
                properties.load(fileReader);
            } catch (IOException e) {
                logger.error("Base properties file is unavailable");
            }
        }
        String numberOfTerminals = properties.getProperty("numberOfTerminals");
        NUMBER_OF_TERMINALS = (numberOfTerminals != null) ? Integer.parseInt(numberOfTerminals) : DEFAULT_NUMBER_TERMINALS;
        for (int i = 0; i < NUMBER_OF_TERMINALS; i++) {
            terminalDeque.add(new Terminal());
        }
        String maxWarehouseCapacity = properties.getProperty("warehouseCapacity");
        MAX_WAREHOUSE_CAPACITY = (maxWarehouseCapacity != null) ? Integer.parseInt(maxWarehouseCapacity) : DEFAULT_CAPACITY;
        currentWarehouseSize.set(MAX_WAREHOUSE_CAPACITY / NUMBER_OF_TERMINALS);
        semaphore = new Semaphore(NUMBER_OF_TERMINALS, true);
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

    public Terminal getTerminal(Truck truck) {
        Terminal freeTerminal = null;
        try {
            semaphore.acquire();
            lockTerminal.lock();
            freeTerminal = terminalDeque.poll();
            logger.info("Truck " + truck.getId() + " got terminal " + freeTerminal.getTerminalId());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lockTerminal.unlock();
            return freeTerminal;
        }
    }

    public void releaseTerminal(Terminal terminal) {
        lockTerminal.lock();
        try {
            terminalDeque.add(terminal);
            logger.info(Thread.currentThread().getName() + " released " + terminal.getTerminalId());
        } finally {
            lockTerminal.unlock();
            semaphore.release();
        }
    }

    public void addToWarehouse(int loadSize) throws CustomException {
        while ((currentWarehouseSize.intValue() + loadSize) >= MAX_WAREHOUSE_CAPACITY) {
            try {
                TimeUnit.SECONDS.sleep(1);
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
                TimeUnit.SECONDS.sleep(1);
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
