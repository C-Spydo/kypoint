package com.cspydo.kypoint.controllers;

import com.cspydo.kypoint.services.KeyValueStore;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class KeyValueController implements HttpHandler{
    private final KeyValueStore keyValueStore = new KeyValueStore();
    Map<String, Object> response_map = new HashMap<>();
    String[] params;

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        response_map.put("code", 200);
        response_map.put("data", null);
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
                    response_map.put("message","Route Not Found");
                    response_map.put("code",404);
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
                    response_map.put("message","Route Not Found");
                    response_map.put("code",404);
                }
                break;
            case "DELETE":
                handleDelete();
                break;
            default:
                response_map.put("message","Method Not Allowed");
                response_map.put("code",405);
                break;
        }

        // Send response
        exchange.sendResponseHeaders((Integer) response_map.get("code"), response_map.toString().length());
        OutputStream os = exchange.getResponseBody();
        os.write(response_map.toString().getBytes());
        os.close();
    }

    private void handlePut() {
        String key = params[0].split("=")[1];
        String value = params[1].split("=")[1];

        Boolean store_response = keyValueStore.put(key, value);
        if(store_response){
            response_map.put("message", "Key-Value pair added");
        }
        else{
            response_map.put("code", 500);
            response_map.put("message", "Error Occured, Please try again");
        }
        response_map.put("data", null);
    }

    private void handleBatchPut() {
        String key = params[0].split("=")[1];
        String value = params[1].split("=")[1];

        Boolean store_response = keyValueStore.put(key, value);
        if(store_response){
            response_map.put("message", "Key-Value pair added");
        }
        else{
            response_map.put("code", 500);
            response_map.put("message", "Error Occured, Please try again");
        }
        response_map.put("data", null);
    }

    private void handleRead() {
        String key = params[0].split("=")[1];
        String store_response = keyValueStore.get(key);
        response_map.put("message", "Value retrieved successfully");
        response_map.put("data", store_response);
    }

    private void handleReadKeyRange() {
        String key = params[0].split("=")[1];
        String store_response = keyValueStore.get(key);
        response_map.put("message", "Value retrieved successfully");
        response_map.put("data", store_response);
    }

    private void handleDelete() {
        String key = params[0].split("=")[1];
        String store_response = keyValueStore.get(key);
        response_map.put("message", "Value retrieved successfully");
        response_map.put("data", store_response);
    }

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

}
