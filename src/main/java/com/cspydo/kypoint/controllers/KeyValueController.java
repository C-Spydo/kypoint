package com.cspydo.kypoint.controllers;

import com.cspydo.kypoint.services.KeyValueStore;
import com.cspydo.kypoint.services.RequestHandler;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class KeyValueController implements HttpHandler{

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        RequestHandler handler = new RequestHandler(exchange);
        handler.process();
    }

}
