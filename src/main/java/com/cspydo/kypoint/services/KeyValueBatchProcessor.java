package com.cspydo.kypoint.services;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class KeyValueBatchProcessor {

    private final KeyValueStore keyValueStore;
    private final ExecutorService executorService;

    public KeyValueBatchProcessor(KeyValueStore keyValueStore) {
        this.keyValueStore = keyValueStore;
        // Create a thread pool of 10 threads for processing
        this.executorService = Executors.newFixedThreadPool(10);
    }

    public void batchPut(List<Map<String, String>> keyValuePairs) {
        for (Map<String, String> pair : keyValuePairs) {
            String key = pair.get("key");
            String value = pair.get("value");

            // Submit each put operation to be executed in the background
            executorService.submit(() -> {
                keyValueStore.put(key, value);
                System.out.println("Stored key: " + key + " with value: " + value);
            });
        }
    }

    // Shutdown the executor when done (optional)
    public void shutdown() {
        executorService.shutdown();
    }
}
