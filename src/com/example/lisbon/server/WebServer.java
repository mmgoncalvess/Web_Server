package com.example.lisbon.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class WebServer {

    private final int portNumber;
    private ServerSocket serverSocket;

    public WebServer(int portNumber) {
        this.portNumber = portNumber;
    }

    public void startServer() {
        try {
            serverSocket = new ServerSocket(portNumber);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Socket connect() {
        Socket clientSocket = null;
        try {
            clientSocket = serverSocket.accept();    // Block while waiting for client connections
            System.out.println("\nFIRST: Client connected...  " + clientSocket.getInetAddress().getHostName());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return clientSocket;
    }
}
