package com.company;

import com.sun.net.httpserver.HttpServer;

import java.io.File;
import java.net.InetSocketAddress;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.Map;


public class Server {

    public static Server server;
    public PrivateKey pri;
    public PublicKey pub;
    public Map<String, UserRecord> users;

    public static void main(String[] args){

        server = new Server();
        server.users = new FileManager(new File("Users"));


        try {
            KeyPair kpg = KeyPairGenerator.getInstance("RSA").generateKeyPair();
            server.pri = kpg.getPrivate();
            server.pub = kpg.getPublic();

            //RSAPublicKeySpec spec = new RSAPublicKeySpec()

            System.out.println(server.pri.toString());


            HttpServer server = HttpServer.create(new InetSocketAddress("localhost", 8001), 0);
            server.createContext("/Home", new HomeHandler());
            server.createContext("/Register", new RegisterHandler());
            server.start();
        } catch (Exception e){


        }

    }
}
