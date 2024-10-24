package com.cspydo.kypoint.utils;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Parser {
    private String objectArrayToJson(Object[] array) {
        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < array.length; i++) {
            json.append("\"").append(array[i].toString()).append("\"");
            if (i < array.length - 1) {
                json.append(", ");
            }
        }
        json.append("]");
        return json.toString();
    }

    public static void sendJsonResponse(HttpExchange exchange, Map<String, Object> response) throws IOException {
        // Create JSON response string
        String jsonResponse = String.format(
                "{\"code\": %d, \"data\": %s, \"message\": \"%s\"}",
                (Integer) response.get("code"),
                response.get("data") == null ? "null" : "\"" + response.get("data") + "\"", // Handle null data
                response.get("message")
        );

        // Set the response headers
        exchange.getResponseHeaders().set("Content-Type", "application/json");

        // Send the response headers with the appropriate response code
        exchange.sendResponseHeaders((Integer) response.get("code"), jsonResponse.length());

        // Get the response body and write the JSON response
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(jsonResponse.getBytes());
        }
    }

    // Parse the request body as a list of key-value maps
    public static List<Map<String, String>> parseRequestBody(HttpExchange exchange) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(exchange.getRequestBody()));
        StringBuilder body = new StringBuilder();
        String line;

        // Read the request body into a String
        while ((line = reader.readLine()) != null) {
            body.append(line);
        }

        // Parse the String into a List of Maps
        return parseJsonArray(body.toString());
    }

    // A very simple method to parse JSON-like array of objects manually
    private static List<Map<String, String>> parseJsonArray(String jsonArray) {
        List<Map<String, String>> result = new ArrayList<>();

        // Clean up the string to remove [ ] and split by }, { to isolate each object
        jsonArray = jsonArray.trim().substring(1, jsonArray.length() - 1);  // Remove [ ] brackets
        String[] objects = jsonArray.split("\\},\\{");  // Split into individual objects

        for (String object : objects) {
            // Clean the braces in each object
            object = object.replace("{", "").replace("}", "").trim();

            // Split the object into key-value pairs
            String[] keyValues = object.split(",");

            Map<String, String> map = new HashMap<>();
            for (String keyValue : keyValues) {
                String[] entry = keyValue.split(":");

                // Remove surrounding quotes and whitespace
                String key = entry[0].replace("\"", "").trim();
                String value = entry[1].replace("\"", "").trim();

                map.put(key, value);
            }
            result.add(map);
        }
        return result;
    }

}
