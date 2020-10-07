package com.company;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import javax.crypto.Cipher;
import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;

public class RegisterHandler implements HttpHandler {


    @Override
    public void handle(HttpExchange exchange) throws IOException {
        System.out.println("Ready!");
        List<Byte> bytereader = new LinkedList<>();
        int letter = exchange.getRequestBody().read();
        while(letter != -1){
            Byte bite = (byte)letter;
            System.out.println(bite);
            bytereader.add(bite);
            letter = exchange.getRequestBody().read();
        }

        byte[] bytes = new byte[bytereader.size()];
        for(int i = 0; i < bytes.length; i++){
            bytes[i] = bytereader.get(i);
        }

        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, Server.server.pri);
            System.out.println("OHHHH");
            byte[] out = cipher.doFinal(bytes);

            String message = "";
            for(int i = 0; i < out.length; i++){
                message +=HandlerSupporter.intToChar(out[i]);
            }
            String[] parameters = message.split("#");
            for(int i = 0; i < parameters.length; i++){
                System.out.println(i + ":" + parameters[i]);
            }

            String username = parameters[0];
            String password = parameters[1];

            if(Server.server.users.containsKey(username)){
                //refuse request as the username is already taken
                String key = "Username is already taken";
                exchange.sendResponseHeaders(200, key.length());

                OutputStream response = exchange.getResponseBody();
                response.write(key.getBytes());
                response.close();
            } else {
                UserRecord record = new UserRecord(username, password, "", false, new ProfileVector(), false);
                Server.server.users.put(username, record);

                String key = "Registration successful!";
                exchange.sendResponseHeaders(200, key.length());

                OutputStream response = exchange.getResponseBody();
                response.write(key.getBytes());
                response.close();
            }

        } catch (Exception e){

            System.out.println("BAD");

        }
    }
}
