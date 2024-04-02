package edu.gwu.seas.csci.architecture6461.models;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import javafx.util.Pair;
import lombok.Getter;

public final class Cache {
    private static Cache instance;
    private int cacheSize;
    @Getter
    private int capacity;

    private LinkedHashMap<Integer, Pair<Integer, int[]>> memoryCache;

    private List<CacheEventHandler> eventHandler = new ArrayList<>();


    private Cache() {}

    /**
     * Returns the current instance of the Cache model.
     * 
     * @return The application cache.
     */
    public static Cache getInstance() {
        if (instance == null) {
            instance = new Cache();
        }

        return instance;
    }

    public void initialize(int cacheSize) {
        this.capacity = cacheSize;
        this.cacheSize = 0;
        this.memoryCache = new LinkedHashMap<>();
    }

    public int[] getValue(int tag) {
        for (var item : memoryCache.entrySet()) {
            if (item.getValue().getKey() == tag) {
                return item.getValue().getValue().clone();
            }
        }

        return null;
    }

    public Pair<Integer, int[]> getCacheLine(int lineNr) {
        if (this.memoryCache.containsKey(lineNr)) {
            return this.memoryCache.get(lineNr);
        }
        else {
            return new Pair<>(0, new int[8]);
        }
    }

    protected void setValue(int tag, int[] values) {
        for (var entry : memoryCache.entrySet()) {
            var pair = entry.getValue();
            if (pair.getKey().equals(tag)) {
                this.memoryCache.remove(entry.getKey());
                this.memoryCache.put(entry.getKey(), new Pair<>(tag, values));

                // Update bindings and exit
                this.eventHandler.forEach(handler -> handler.onUpdate(memoryCache));
                return;
            }
        }
        if (cacheSize < capacity) {
            this.memoryCache.put(this.cacheSize, new Pair<>(tag, values));
            this.cacheSize++;
        } else {
            // evict old item then add new item
            this.memoryCache.remove(0);
            this.memoryCache.put(this.cacheSize, new Pair<>(tag, values));
        }
        this.eventHandler.forEach(handler -> handler.onUpdate(memoryCache));
    }

    public void addEventListener(CacheEventHandler eventHandler) {
        this.eventHandler.add(eventHandler);
    }


    public boolean removeEventListener(CacheEventHandler eventHandler) {
        return this.eventHandler.remove(eventHandler);
    }


    @FunctionalInterface
    public interface CacheEventHandler {
        void onUpdate(LinkedHashMap<Integer, Pair<Integer, int[]>>  cache);
    }
    
}
