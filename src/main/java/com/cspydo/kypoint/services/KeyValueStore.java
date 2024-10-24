package com.cspydo.kypoint.services;

import java.io.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class KeyValueStore {
    final ConcurrentHashMap<String, String> store;
    private final String[] replicaPaths;

    // Constructor initializes the paths for the replicas
    public KeyValueStore() {
        this.replicaPaths = new String[] {
                "datastore.bin",
                "datastore_replica_one.bin",
                "datastore_replica_two.bin",
                "datastore_replica_three.bin"
        };
        this.store = new ConcurrentHashMap<>();
        load(); // Load existing data at startup
    }

    // Load the key-value pairs from the available replica
    public void load() {
        for (String path : replicaPaths) {
            File file = new File(path);
            if (file.exists() && file.length() > 0) { // Check if the file exists and is not empty
                try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path))) {
                    ConcurrentHashMap<String, String> loadedStore = (ConcurrentHashMap<String, String>) ois.readObject();
                    store.putAll(loadedStore); // Add loaded entries to the current store
                    System.out.println("Loaded data from: " + path);
                    break; // Stop loading once a valid replica is found
                } catch (EOFException e) {
                    System.out.println("EOFException: The file is empty or contains no valid serialized data.");
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("File " + path + " does not exist or is empty.");
            }
        }
    }

    // Save the key-value pairs to all replicas
    public void save() {
        for (String path : replicaPaths) {
            System.out.println("Saving to: " + new File(path).getAbsolutePath());
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path))) {
                oos.writeObject(store); // Serialize the ConcurrentHashMap
                oos.flush(); // Flush the stream explicitly
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Get the value for a given key
    public String get(String key) {
        return store.get(key);
    }

    // Put a key-value pair into the store
    public Boolean put(String key, String value) {
        try {
            store.put(key, value);
            save();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public Boolean remove(String key) {
        try {
            store.remove(key);
            save(); // Persist the change to the replicas
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
