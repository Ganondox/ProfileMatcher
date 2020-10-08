package com.company;

import javax.crypto.Cipher;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.ProxySelector;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.*;
import java.security.spec.RSAPublicKeySpec;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.security.KeyPairGenerator;



public class Client {

    public static void main(String args[]){


        try {
            KeyPair kpg = KeyPairGenerator.getInstance("RSA").generateKeyPair();
            PrivateKey pri = kpg.getPrivate();
            PublicKey pub = kpg.getPublic();



            //String requestBody = "username: " + args[0] + " password: " + args[1];

            String key = reformatKey(pub.toString());

            String requestBody = key + "#" + args[0] + "#" + args[1] + "#";

            //String requestBody = key + "#" + "bob#jones#Bob Jones is a god among mortal men#Y#1@2@3@4@5&1@2@3@4@5#Y" + "#";

            while(requestBody.length() % 245 != 0){
                requestBody += '0';
            }



            //ObjectMapper objectMapper = new ObjectMapper();
            //String requestBody = objectMapper.writeValueAsString(values);


            HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8001/Home")).GET().build();
            HttpClient client = HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1).build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response.toString());
            System.out.println(response.body());

           /* String[] parameters = response.body().toString().split(":");


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

            */

            String[] parameters = reformatKey(response.body()).split("#");
            BigInteger modulus = new BigInteger(parameters[0]);
            BigInteger exponent = new BigInteger(parameters[1]);


            RSAPublicKeySpec spec = new RSAPublicKeySpec(modulus, exponent);
            KeyFactory factory = KeyFactory.getInstance("RSA");
            PublicKey pub2 = factory.generatePublic(spec);



            //Cipher cipher = Cipher.getInstance("RSA");
            //cipher.init(Cipher.ENCRYPT_MODE, pub2);
            byte[] encrypted = HandlerSupporter.cipherTrans(true, pub2, requestBody.getBytes("UTF-8"));

          /*  for(int i = 0; i < encrypted.length; i++){
                System.out.println(encrypted[i] + "p");
            }

           */

            request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8001/Match")).POST(HttpRequest.BodyPublishers.ofByteArray(encrypted)).build();
            HttpResponse<InputStream> response2 = client.send(request, HttpResponse.BodyHandlers.ofInputStream());


            System.out.println(HandlerSupporter.decryptStream(response2.body(), pri));

            //System.out.println(response.body().toString());



            //POST(HttpRequest.BodyPublishers.ofString(requestBody))

            //mcgilacuty

        } catch (Exception e){

            System.out.println(e.getCause());
            System.out.println("FAIL");

        }


    }

    public static String reformatKey(String key){
        String[] parameters = key.split(":");
        String modulus = parameters[2].split("\n")[0].trim();
        //System.out.println(modulus);
        String exponent = parameters[3].trim();
        //System.out.println(exponent);
        return modulus +"#"+ exponent;
    }
}
