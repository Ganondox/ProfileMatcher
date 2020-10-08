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
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MatchHandler implements HttpHandler {


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
            //String payload = parameters[2] + '#' + parameters[3] + "#" + parameters[4] + "#" + parameters[5] + "#" + parameters[6] + "#" + parameters[7];
            //System.out.println(payload);

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
                    if(Server.server.users.get(username).isCompleted) {
                        if(!Server.server.users.get(username).isEmployer) {

                            //Server.server.users.put(username, FileManager.stringToRecord(payload));

                            //Build the working set
                            List<ProfileVector> users = new LinkedList<>();
                            List<ProfileVector> employers = new LinkedList<>();
                            Map<Integer, String> idToUsername = new HashMap<>();
                            ProfileVector target = Server.server.users.get(username).profile;
                            for (String un : Server.server.users.keySet()) {
                                UserRecord ur = Server.server.users.get(un);
                                if (ur.isCompleted) {
                                    if(!ur.username.equals(username)){
                                        idToUsername.put(ur.profile.getId(), un);
                                        if(ur.isEmployer){
                                            employers.add(ur.profile);
                                        } else {
                                            users.add(ur.profile);
                                        }
                                    }
                                }
                            }
                            //find matches
                            System.out.println("Finding matches");
                            int[] matches = MatchFinder.getRanking(target, employers, users, Server.APT_N, Server.INT_M, Server.NUM);


                            String key = "";
                            for(int i = 0; i < matches.length; i++){

                                String match = idToUsername.get(matches[i]);
                                key += i + ": " + match + " - " + Server.server.users.get(match).description + "\n";
                            }
                            //String key = FileManager.recordToString(record);
                            key += "\n";
                            while (key.length() % 245 != 0) {
                                key += " ";
                            }
                            byte[] msg = HandlerSupporter.cipherTrans(true, pub2, key.getBytes());
                            exchange.sendResponseHeaders(200, msg.length);

                            OutputStream response = exchange.getResponseBody();
                            response.write(msg);
                            response.close();
                        } else {
                            String key = "Only users find matches. Wait until a user reaches out to you.";
                            while(key.length() % 245 != 0){
                                key += " ";
                            }
                            byte[] msg = HandlerSupporter.cipherTrans(true, pub2, key.getBytes());
                            exchange.sendResponseHeaders(200, msg.length);

                            OutputStream response = exchange.getResponseBody();
                            response.write(msg);
                            response.close();
                        }
                    } else {
                        String key = "Profile must be completed before it can be matched.";
                        while(key.length() % 245 != 0){
                            key += " ";
                        }
                        byte[] msg = HandlerSupporter.cipherTrans(true, pub2, key.getBytes());
                        exchange.sendResponseHeaders(200, msg.length);

                        OutputStream response = exchange.getResponseBody();
                        response.write(msg);
                        response.close();
                    }
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
