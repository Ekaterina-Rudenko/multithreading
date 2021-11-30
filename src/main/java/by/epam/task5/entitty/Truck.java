package by.epam.task5.entitty;

import java.util.StringJoiner;
import java.util.concurrent.Callable;

public class Truck implements Callable {
    private long truckId;
    private int loadSize;
    private int capacity;
    private boolean isPerishable;

    public Truck(long id, int loadSize, int capacity, boolean isPerishable) {
        this.truckId = id;
        this.loadSize = loadSize;
        this.capacity = capacity;
        this.isPerishable = isPerishable;
    }

    @Override
    public String call() throws Exception {
        Base base = null;
        Terminal terminal = null;
        String terminalId;
        try {
            base = Base.getInstance();
            terminal = base.getTerminal(this);
            terminalId = String.valueOf(terminal.terminalId);
            terminal.unloadTruck(this);
            terminal.loadTruck(this);
        } finally {
            base.releaseTerminal(terminal);
        }
        return "Truck #" + this.truckId + " was served by terminal #" + terminalId + ". Truck isPerishable - " + this.isPerishable();
    }

    public long getId() {
        return truckId;
    }

    public int getLoadSize() {
        return loadSize;
    }

    public void setLoadSize(int loadSize) {
        this.loadSize = loadSize;
    }

    public int getCapacity() {
        return capacity;
    }

    public boolean isPerishable() {
        return isPerishable;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        Truck that = (Truck) o;
        return truckId == that.truckId
                && loadSize == that.loadSize
                && capacity == that.capacity
                && isPerishable == that.isPerishable;
    }

    @Override
    public int hashCode() {
        int result = 11;
        result += result * 31 + truckId;
        result += result * 31 + loadSize;
        result += result * 31 + capacity;
        return result;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Truck.class.getSimpleName() + "[", "]")
                .add("truckId=" + truckId)
                .add("loadSize=" + loadSize)
                .add("capacity=" + capacity)
                .add("isPerishable=" + isPerishable)
                .toString();
    }
}
