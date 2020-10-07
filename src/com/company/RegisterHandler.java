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

public class RegisterHandler implements HttpHandler {


    @Override
    public void handle(HttpExchange exchange) throws IOException {
        System.out.println("Ready!");
        /*List<Byte> bytereader = new LinkedList<>();
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

         */
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

            if(Server.server.users.containsKey(username)){
                //refuse request as the username is already taken
                String key = "Username is already taken";
                System.out.println("Taken");
                while(key.length() % 245 != 0){
                    key += " ";
                }
                /*cipher = Cipher.getInstance("RSA");
                cipher.init(Cipher.ENCRYPT_MODE, pub2 );
                byte[] msg = cipher.doFinal(key.getBytes());
                 */
                byte[] msg = HandlerSupporter.cipherTrans(true, pub2, key.getBytes());
                exchange.sendResponseHeaders(200, msg.length);

                OutputStream response = exchange.getResponseBody();
                response.write(msg);
                response.close();
            } else {
                UserRecord record = new UserRecord(username, password, "", false, new ProfileVector(), false);
                Server.server.users.put(username, record);

                String key = "Registration successful!";
                while(key.length() % 245 != 0){
                    key += " ";
                }
                byte[] msg = HandlerSupporter.cipherTrans(true, pub2, key.getBytes());
                exchange.sendResponseHeaders(200, msg.length);

                OutputStream response = exchange.getResponseBody();
                response.write(msg);
                response.close();
            }

        } catch (Exception e){

            System.out.println("BAD");
            exchange.sendResponseHeaders(400, 0);
            exchange.close();

        }
    }
}
