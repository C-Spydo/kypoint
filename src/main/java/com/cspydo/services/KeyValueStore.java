package com.cspydo.services;

import java.io.*;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class KeyValueStore {
    private final ConcurrentHashMap<String, String> store;
    private final String filePath;

    // Constructor accepting a file path
    public KeyValueStore() {
        this.filePath = Objects.requireNonNull(getClass().getClassLoader().getResource("datastore.bin")).getFile();
        this.store = new ConcurrentHashMap<>();
        load(); // Load existing data at startup
    }

    // Load the key-value pairs from the binary file
    public void load() {
        File file = new File(filePath);
        if (file.exists() && file.length() > 0) { // Check if the file exists and is not empty
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
                ConcurrentHashMap<String, String> loadedStore = (ConcurrentHashMap<String, String>) ois.readObject();
                store.putAll(loadedStore); // Add loaded entries to the current store
            } catch (EOFException e) {
                // Handle the case where the file is empty
                System.out.println("EOFException: The file is empty or contains no valid serialized data.");
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("File does not exist or is empty. Initializing a new store.");
        }
    }

    // Save the key-value pairs to the binary file
    public void save() {
        System.out.println("Saving to: " + new File(filePath).getAbsolutePath());
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(store); // Serialize the ConcurrentHashMap
            oos.flush(); // Flush the stream explicitly
        } catch (IOException e) {
            e.printStackTrace();
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
            System.out.println("Saving store: " + store);

            save();
            return true;
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    // Optional: Remove a key-value pair from the store
    public void remove(String key) {
        store.remove(key);
        save(); // Persist the change to the file
    }

    // Optional: Get all keys
    public String[] keys() {
        return store.keySet().toArray(new String[0]);
    }
}
