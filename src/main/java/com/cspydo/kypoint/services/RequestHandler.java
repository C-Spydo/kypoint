package com.cspydo.kypoint.services;

import com.cspydo.kypoint.utils.Parser;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RequestHandler {

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

    private void handleBatchPut() {
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

    private void handleRead() {
        String key = params[0].split("=")[1];
        String store_response = keyValueStore.get(key);
        response.put("message", "Value retrieved successfully");
        response.put("data", store_response);
    }

    private void handleReadKeyRange() {
        String key = params[0].split("=")[1];
        String store_response = keyValueStore.get(key);
        response.put("message", "Value retrieved successfully");
        response.put("data", store_response);
    }

    private void handleDelete() {
        String key = params[0].split("=")[1];
        String store_response = keyValueStore.get(key);
        response.put("message", "Value retrieved successfully");
        response.put("data", store_response);
    }

}
