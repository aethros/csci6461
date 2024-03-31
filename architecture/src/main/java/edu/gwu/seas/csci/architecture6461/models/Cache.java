package edu.gwu.seas.csci.architecture6461.models;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import javafx.util.Pair;
import lombok.Getter;

public final class Cache {
    private static Cache instance;
    private int cacheSize;
    @Getter
    private int capacity;

    private Queue<Pair<Integer, int[]>> memoryCache;

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
        this.memoryCache = new LinkedList<>();
    }

    public int[] getValue(int tag) {
        for (Pair<Integer, int[]> item : memoryCache) {
            if (item.getKey() == tag) {
                return item.getValue().clone();
            }
        }

        return new int[8];
    }

    protected void setValue(int tag, int[] values) {
        if (this.memoryCache.stream().filter(item -> item.getKey() == tag).count() > 0) {
            this.memoryCache.removeIf(item -> item.getKey() == tag);
            this.memoryCache.add(new Pair<>(tag, values));
        } else {
            if (cacheSize < capacity) {
                this.memoryCache.add(new Pair<>(tag, values));
                this.cacheSize++;
            } else {
                // evict old item then add new item
                this.memoryCache.poll();
                this.memoryCache.add(new Pair<>(tag, values));
            }
        }

        this.eventHandler.forEach(handler -> handler.onUpdate(memoryCache));
    }

    public void addEventListener(CacheEventHandler eventHandler) {
        this.eventHandler.add(eventHandler);
    }


    public boolean removeEventListener(CacheEventHandler eventHandler) {
        return this.eventHandler.remove(eventHandler);
    }


    public interface CacheEventHandler {
        void onUpdate(Queue<Pair<Integer, int[]>>  cache);
    }
    
}
