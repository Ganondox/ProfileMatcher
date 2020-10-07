package com.company;

import javax.crypto.Cipher;
import java.math.BigInteger;
import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.ProxySelector;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;


public class Client {

    public static void main(String args[]){



        //String requestBody = "username: " + args[0] + " password: " + args[1];

        String requestBody = args[0] + "#" + args[1];


        //ObjectMapper objectMapper = new ObjectMapper();
        //String requestBody = objectMapper.writeValueAsString(values);


        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8001/Home")).GET().build();
        HttpClient client = HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1).build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response.toString());
            System.out.println(response.body().toString());

            String[] parameters = response.body().toString().split(":");


            for(int i = 0; i < parameters.length; i++){
                System.out.println(i + ":" + parameters[i]);
            }

            for(int i = 0; i < parameters[2].split("\n").length; i++){
                System.out.println(i + ":" + parameters[2].split("\n")[i]);
            }

            BigInteger modulus = new BigInteger(parameters[2].split("\n")[0].trim());
            System.out.println(modulus);
            BigInteger exponent = new BigInteger(parameters[3].trim());
            System.out.println(exponent);

            RSAPublicKeySpec spec = new RSAPublicKeySpec(modulus, exponent);
            KeyFactory factory = KeyFactory.getInstance("RSA");
            PublicKey pub = factory.generatePublic(spec);

            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, pub);
            byte[] encrypted = cipher.doFinal(requestBody.getBytes("UTF-8"));

            for(int i = 0; i < encrypted.length; i++){
                System.out.println(encrypted[i]);
            }

            request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8001/Register")).POST(HttpRequest.BodyPublishers.ofByteArray(encrypted)).build();
            response = client.send(request, HttpResponse.BodyHandlers.ofString());

            //POST(HttpRequest.BodyPublishers.ofString(requestBody))

        } catch (Exception e){

            System.out.println(e.getCause());
            System.out.println("FAIL");

        }


    }
}
