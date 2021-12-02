package by.epam.task5.parser;

import by.epam.task5.entitty.Truck;
import by.epam.task5.exception.CustomException;

import java.util.List;

public interface DataParser {
    Truck parseTruck(String data) throws CustomException;
    List<Truck> parseTruckList(List<String> data) throws CustomException;
}
