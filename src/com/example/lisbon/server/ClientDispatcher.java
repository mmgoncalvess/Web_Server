package com.example.lisbon.server;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Random;

public class ClientDispatcher implements Runnable{

    String messageIn;
    String messageOut;
    String contentType;
    File file;
    Socket clientSocket;
    BufferedReader bufferedReader;
    BufferedOutputStream bufferedOutputStream;
    BufferedInputStream bufferedInputStream;

    public ClientDispatcher(Socket connection) {
        this.clientSocket = connection;
    }

    @Override
    public void run() {
        receive();
        process();
        send();
        close();
    }

    public void receive() {
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            messageIn = bufferedReader.readLine();
            System.out.println("SECOND: message received: " + messageIn);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void process() {
        if(messageIn == null) return;
        String resource = messageIn.substring(5, messageIn.length() - 9);


        if(resource.endsWith(".jpg")) {
            System.out.println("jpg");
            file = new File("Resources/" + resource);
            contentType = "image/jpeg";
        } else if (resource.endsWith(".html")) {
            file = new File("Resources/" + resource);
            contentType = "text/html; charset=UTF-8";
        } else {
            file = new File("Resources/file1.html");
            contentType = "text/html; charset=UTF-8";
        }
        System.out.println("Resource: " + resource);

        messageOut = "HTTP/1.0 200 Document Follows\r\nContent-Type: " + contentType + "\r\nContent-Length: <" + file.length() + "> \r\n\r\n";  //text/html; charset=UTF-8  image/jpeg
        System.out.println("THIRD: response ... " + messageOut.substring(0, 54));
    }

    public void send() {
        if (file == null) return;
        try {
            bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
            System.out.println(file.getName());
            bufferedOutputStream = new BufferedOutputStream(clientSocket.getOutputStream());
            bufferedOutputStream.write(messageOut.getBytes(StandardCharsets.UTF_8));
            int totalBytes = 0;
            int ch;
            while ((ch = bufferedInputStream.read()) != -1) {
                bufferedOutputStream.write(ch);
                totalBytes++;
            }
            bufferedOutputStream.flush();
            System.out.println("FOURTH: total bytes sent: " + totalBytes);
        } catch (IOException e){
                e.printStackTrace();
        }
    }

    public void close() {
        try {
            clientSocket.getOutputStream().close();
            clientSocket.close();
            System.out.println("FIFTH: client socket closed, server socket closed...");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
