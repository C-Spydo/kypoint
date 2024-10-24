package com.cspydo.kypoint.services;

import com.cspydo.kypoint.interfaces.IRequestHandler;
import com.cspydo.kypoint.utils.Parser;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class RequestHandler implements IRequestHandler {

    private final KeyValueStore keyValueStore = new KeyValueStore();
    String[] params;
    HttpExchange exchange;
    Map<String, Object> response = new HashMap<>();

    public RequestHandler(HttpExchange exchange) throws IOException {
        this.exchange = exchange;
        process(); // Load existing data at startup
    }

    public void process() throws IOException {
        String method = exchange.getRequestMethod();
        response.put("code", 200);
        response.put("data", null);
        String query = exchange.getRequestURI().getQuery();
        String route = exchange.getRequestURI().getPath();
        params = query.split("&");

        switch (method) {
            case "GET":
                if (route.equals("/key-value/read")) {
                    handleRead();
                }
                else if (route.equals("/key-value/read-key-range")) {
                    handleReadKeyRange();
                }
                else{
                    response.put("message","Route Not Found");
                    response.put("code",404);
                }
                break;
            case "PUT":
                if (route.equals("/key-value/put")) {
                    handlePut();
                }
                else if (route.equals("/key-value/batch-put")) {
                    handleBatchPut();
                }
                else{
                    response.put("message","Route Not Found");
                    response.put("code",404);
                }
                break;
            case "DELETE":
                handleDelete();
                break;
            default:
                response.put("message","Method Not Allowed");
                response.put("code",405);
                break;
        }
        Parser.sendJsonResponse(exchange, response);
    }

    private void handlePut() {
        String key = params[0].split("=")[1];
        String value = params[1].split("=")[1];

        Boolean store_response = keyValueStore.put(key, value);
        if(store_response){
            response.put("message", "Key-Value pair added");
        }
        else{
            response.put("code", 500);
            response.put("message", "Error Occured, Please try again");
        }
        response.put("data", null);
    }

    private void handleBatchPut() throws IOException {
        try {
            List<Map<String, String>> keyValuePairs = Parser.parseRequestBody(exchange);
            if (keyValuePairs != null) {
                KeyValueBatchProcessor batchProcessor = new KeyValueBatchProcessor(keyValueStore);
                batchProcessor.batchPut(keyValuePairs);
            }
            response.put("message", "Key-Value pairs added");

        }
        catch(IOException e){
            response.put("code", 500);
            response.put("message", "Error Occured, Please try again");
        }
        response.put("data", null);
    }

    private void handleRead() {
        String key = params[0].split("=")[1];
        String store_response = keyValueStore.get(key);
        response.put("message", "Value retrieved successfully");
        response.put("data", store_response);
    }

    private void handleReadKeyRange() {
        // Extract startKey and endKey from the request params
        int startKey = Integer.parseInt(params[0].split("=")[1]);
        int endKey = Integer.parseInt(params[1].split("=")[1]);

        // Store retrieved values in a list or map to return in the response
        Map<String, String> retrievedValues = new HashMap<>();

        // Iterate through the key-value store and retrieve values in the given range
        for (Map.Entry<String, String> entry : keyValueStore.store.entrySet()) {
            String key = entry.getKey();

            try {
                // Ensure the key is a numeric string
                int numericKey = Integer.parseInt(key);

                // Check if the numeric key is within the range
                if (numericKey >= startKey && numericKey <= endKey) {
                    // Add the key-value pair to the retrieved results
                    retrievedValues.put(key, entry.getValue());
                }
            } catch (NumberFormatException e) {
                // Ignore keys that are not numeric
                System.out.println("Skipping non-numeric key: " + key);
            }
        }

        // Prepare response with the retrieved values
        if (!retrievedValues.isEmpty()) {
            response.put("message", "Values retrieved successfully");
            response.put("data", retrievedValues);
        } else {
            response.put("message", "No values found in the given range");
            response.put("data", new HashMap<>());
        }
    }

    private void handleDelete() {
        String key = params[0].split("=")[1];
        Boolean store_response = keyValueStore.remove(key);
        if(store_response){
            response.put("message", "Key-Value pair Removed");
        }
        else{
            response.put("code", 500);
            response.put("message", "Error Occured, Please try again");
        }
        response.put("data", null);
    }

}
