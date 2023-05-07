package com.example.lisbon.server;

public class Main {

    public static void main(String[] args) {
        WebServer webServer = new WebServer(8080);
        webServer.startServer();

        while (true) {
            ClientDispatcher clientDispatcher = new ClientDispatcher(webServer.connect());
            Thread thread = new Thread(clientDispatcher);
            thread.start();
        }
    }
}
