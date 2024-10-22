package com.cspydo.kypoint;

import com.cspydo.kypoint.controllers.KeyValueController;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class KyPoint {
    public static void main(String[] args) throws IOException {
        // HTTP server listening on port 8080
        HttpServer server = HttpServer.create(new InetSocketAddress(25001), 0);
        server.createContext("/key-value", new KeyValueController());
        server.setExecutor(null); // creates a default executor
        server.start();
        System.out.println("Server started on port 25001");
    }
}