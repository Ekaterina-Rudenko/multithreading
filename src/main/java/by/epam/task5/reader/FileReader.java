package by.epam.task5.reader;

import by.epam.task5.exception.CustomException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileReader {
    static final Logger logger = LogManager.getLogger();
    public List<String> readFile(String path) throws CustomException {
        ClassLoader loader = getClass().getClassLoader();
        URL resource = loader.getResource(path);
        if (resource == null) {
            throw new CustomException("Resource is null " + path);
        }
        String filePath = new File(resource.getFile()).getPath();
        List<String> dataList = new ArrayList<>();
        try {
            Files.lines(Paths.get(filePath))
                    .forEach(dataList::add);
        } catch (IOException e)  {
            logger.log(Level.ERROR, "Reading from " + filePath + " was failed or interrupted", e);
            throw new CustomException("Reading from " + filePath + " was failed or interrupted", e);
        }
        return dataList;
    }
}
