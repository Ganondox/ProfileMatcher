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

    final static int APT_N = 100;
    final static int INT_M = 100;
    final static int NUM = 3;

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
            server.createContext("/Login", new LoginHandler());
            server.createContext("/Delete", new DeleteHandler());
            server.createContext("/Update", new UpdateHandler());
            server.createContext("/Match", new MatchHandler());
            server.start();
        } catch (Exception e){


        }

    }
}
