package com.company;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;

public class HomeHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        System.out.println("Go! ");



        String key = Server.server.pub.toString();

        exchange.sendResponseHeaders(200, key.length());

        OutputStream response = exchange.getResponseBody();
        response.write(key.getBytes());
        response.close();

        /*exchange.sendResponseHeaders(200, 0);
        int letter = 0;
        while(letter != -1){
            letter = exchange.getRequestBody().read();
            System.out.println(HandlerSupporter.intToChar(letter));
        }*/
        //System.out.println(exchange.getRequestBody().toString());
        //exchange.setAttribute("Stuff", "Stuff");
        //exchange.close();

    }
}
