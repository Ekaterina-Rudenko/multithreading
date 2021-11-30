package by.epam.task5.util;

public class IdGenerator {
    private static long count;
    public static long generate(){
        return ++count;
    }
}
