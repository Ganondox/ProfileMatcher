package com.company;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import javax.crypto.Cipher;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.LinkedList;
import java.util.List;

public class UpdateHandler implements HttpHandler {


    @Override
    public void handle(HttpExchange exchange) throws IOException {


        try {

            String message = HandlerSupporter.decryptStream(exchange.getRequestBody(), Server.server.pri);

            String[] parameters = message.split("#");
            for(int i = 0; i < parameters.length; i++){
                System.out.println(i + ":" + parameters[i]);
            }

            BigInteger modulus = new BigInteger(parameters[0]);
            BigInteger exponent = new BigInteger(parameters[1]);

            RSAPublicKeySpec spec = new RSAPublicKeySpec(modulus, exponent);
            KeyFactory factory = KeyFactory.getInstance("RSA");
            PublicKey pub2 = factory.generatePublic(spec);


            String username = parameters[2];
            String password = parameters[3];

            //rebuild payload
            String payload = parameters[2] + '#' + parameters[3] + "#" + parameters[4] + "#" + parameters[5] + "#" + parameters[6] + "#" + parameters[7];
            System.out.println(payload);

            if(!Server.server.users.containsKey(username)){
                //refuse request as the username is already taken
                String key = "Username is not registered";
                while(key.length() % 245 != 0){
                    key += " ";
                }
                byte[] msg = HandlerSupporter.cipherTrans(true, pub2, key.getBytes());
                exchange.sendResponseHeaders(200, msg.length);

                OutputStream response = exchange.getResponseBody();
                response.write(msg);
                response.close();
            } else {
                UserRecord record = Server.server.users.get(username);
                if(password.equals(record.password)){
                    Server.server.users.put(username, FileManager.stringToRecord(payload));
                    String key = "Update successful!";
                    //String key = FileManager.recordToString(record);
                    key += "#";
                    while(key.length() % 245 != 0){
                        key += " ";
                    }
                    byte[] msg = HandlerSupporter.cipherTrans(true, pub2, key.getBytes());
                    exchange.sendResponseHeaders(200, msg.length);

                    OutputStream response = exchange.getResponseBody();
                    response.write(msg);
                    response.close();
                } else {
                    String key = "Password is incorrect.";
                    while(key.length() % 245 != 0){
                        key += " ";
                    }
                    byte[] msg = HandlerSupporter.cipherTrans(true, pub2, key.getBytes());
                    exchange.sendResponseHeaders(200, msg.length);

                    OutputStream response = exchange.getResponseBody();
                    response.write(msg);
                    response.close();
                }


            }

        } catch (Exception e){

            System.out.println("BAD");
            exchange.sendResponseHeaders(400, 0);
            exchange.close();

        }
    }
}
