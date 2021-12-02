package by.epam.task5.reader;

import by.epam.task5.exception.CustomException;

import java.util.List;

public interface FileReader {
    List<String> readTruckData(String path) throws CustomException;
}
