package edu.gwu.seas.csci.architecture6461.models;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

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
        var set = memoryCache.entrySet();
        for (var entry : set) {
            var pair = entry.getValue();
            // if the tag is already in the cache, update the value
            if (pair.getKey().equals(tag)) {
                // remove the old entry
                this.memoryCache.remove(entry.getKey());
                this.cacheSize--;
                // update the insertion order for the queue
                var existingSet = this.deepCloneSet(memoryCache.entrySet());
                for (var existingEntry : existingSet) {
                    // if the key is greater than or equal to the current key, decrement the key
                    if (existingEntry.getKey() >= entry.getKey()) {
                        var key = existingEntry.getKey();
                        var value = existingEntry.getValue();
                        this.memoryCache.put(key - 1, value);
                    }
                }
                // add the new entry
                this.memoryCache.put(this.cacheSize, new Pair<>(tag, values));
                this.cacheSize++;

                // Update bindings and exit
                CompletableFuture.runAsync(() -> this.eventHandler.forEach(handler -> handler.onUpdate(memoryCache)));
                return;
            }
        }
        // if the tag is not in the cache, add it
        if (cacheSize < capacity) {
            // add new item
            this.memoryCache.put(this.cacheSize, new Pair<>(tag, values));
            this.cacheSize++;
        } else {
            // evict old item
            this.memoryCache.remove(0);
            // update the insertion order for the queue
            for (var entry : memoryCache.entrySet()) {
                var key = entry.getKey();
                var value = entry.getValue();
                this.memoryCache.put(key - 1, value);
            }
            //  then add new item
            this.memoryCache.put(this.cacheSize, new Pair<>(tag, values));
        }
                // Update bindings and exit
        CompletableFuture.runAsync(() -> this.eventHandler.forEach(handler -> handler.onUpdate(memoryCache)));
    }

    public void addEventListener(CacheEventHandler eventHandler) {
        this.eventHandler.add(eventHandler);
    }


    public boolean removeEventListener(CacheEventHandler eventHandler) {
        return this.eventHandler.remove(eventHandler);
    }

    private Set<Entry<Integer, Pair<Integer, int[]>>> deepCloneSet(Set<Entry<Integer, Pair<Integer, int[]>>> entrySet) {
        Map<Integer, Pair<Integer, int[]>> clonedMap = new LinkedHashMap<>();

        for (var entry : entrySet) {
            Integer clonedKey = Integer.valueOf(entry.getKey());
            Pair<Integer, int[]> clonedValue = this.deepClonePair(entry.getValue());
            clonedMap.put(clonedKey, clonedValue);
        }

        return clonedMap.entrySet();
    }

    private Pair<Integer, int[]> deepClonePair(Pair<Integer, int[]> value) {
        Integer clonedKey = Integer.valueOf(value.getKey());
        int[] clonedValue = value.getValue().clone();
        return new Pair<>(clonedKey, clonedValue);
    }

    @FunctionalInterface
    public interface CacheEventHandler {
        void onUpdate(LinkedHashMap<Integer, Pair<Integer, int[]>>  cache);
    }
    
}
