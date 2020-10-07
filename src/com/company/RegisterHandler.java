package com.company;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import javax.crypto.Cipher;
import java.io.IOException;
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

            for(int i = 0; i < out.length; i++){
                System.out.println(HandlerSupporter.intToChar(out[i]));
            }
        } catch (Exception e){

            System.out.println("BAD");

        }
    }
}
