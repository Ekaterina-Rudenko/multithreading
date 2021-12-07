package by.epam.task5.main;

import by.epam.task5.entitty.Truck;
import by.epam.task5.entitty.TruckQueue;
import by.epam.task5.exception.CustomException;
import by.epam.task5.parser.DataParser;
import by.epam.task5.parser.impl.DataParserImpl;
import by.epam.task5.reader.FileReader;
import by.epam.task5.reader.impl.FileReaderImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class Main {
    static Logger logger = LogManager.getLogger();
    public static final String TRUCK_INFO_FILE_PATH = "data/truck.txt";

    public static void main(String[] args) {
        FileReader reader = new FileReaderImpl();
        DataParser parser = new DataParserImpl();
        try {
            List<String> truckData = reader.readTruckData(TRUCK_INFO_FILE_PATH);
            List<Truck> trucks = parser.parseTruckList(truckData);
            List<Future<String>> information = new ArrayList<>();
            ExecutorService executorService = Executors.newFixedThreadPool(trucks.size());
            TruckQueue truckQueue = new TruckQueue();
            truckQueue.addTruckListToQueue(trucks);
            while (truckQueue.size() > 0) {
                Truck truck = truckQueue.getTruckFromQueue();
                information.add(executorService.submit(truck));
                TimeUnit.MILLISECONDS.sleep(100);
            }
            executorService.shutdown();
            for (Future<String> future : information) {
                logger.info(future.get());
            }
        } catch (InterruptedException | CustomException | ExecutionException e) {
            logger.error("Exception ", e);
            e.printStackTrace();
        }
    }
}
