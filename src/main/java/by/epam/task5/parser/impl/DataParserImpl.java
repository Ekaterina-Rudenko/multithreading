package by.epam.task5.parser.impl;

import by.epam.task5.entitty.Truck;
import by.epam.task5.exception.CustomException;
import by.epam.task5.parser.DataParser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;


public class DataParserImpl implements DataParser {
    static Logger logger = LogManager.getLogger();
    static final String REGEX_DELIMITER = " ";
    static final String EQUALS_DELIMITER = "=";
    static final int PARAMETERS_SIZE = 4;

    @Override
    public List<Truck> parseTruckList(List<String> data) throws CustomException {
        List<Truck> trucks = new ArrayList<>();
        for (String s : data) {
            trucks.add(parseTruck(s));
        }
        logger.info("Trucks creation is completed. Number of trucks: " + trucks.size());
        return trucks;
    }

    @Override
    public Truck parseTruck(String lineTruck) throws CustomException {
        Truck truck;
        lineTruck.trim();
        String[] truckParameter = lineTruck.split(REGEX_DELIMITER);
        if (truckParameter.length != PARAMETERS_SIZE) {
            logger.error("The line is incorrect or incomplete");
            throw new CustomException("The line is incorrect or incomplete");
        }
        List<String> parameterList = new ArrayList<>();
        for (String s : truckParameter) {
            String[] subData = s.split(EQUALS_DELIMITER);
            parameterList.add(subData[1]);
        }
        long truckId = Long.parseLong(parameterList.get(0));
        int loadSize = Integer.parseInt(parameterList.get(1));
        int capacity = Integer.parseInt(parameterList.get(2));
        boolean isPerishable = Boolean.parseBoolean(parameterList.get(3));
        truck = new Truck(truckId, loadSize, capacity, isPerishable);
        logger.info("Truck " + truck.getId() + " is created " + truck);
        return truck;
    }
}
