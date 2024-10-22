package com.cspydo.kypoint.utils;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

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

}
