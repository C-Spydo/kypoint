package com.cspydo.kypoint.interfaces;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;

public interface IRequestHandler {
    void process() throws IOException;
    void handlePut();
    void handleBatchPut();
    void handleRead();
    void handleReadKeyRange();
    void handleDelete();
}