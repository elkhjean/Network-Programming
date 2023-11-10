package com.guessinggame.controller;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;

import com.guessinggame.model.GuessingGame;

public class HttpServer {
    static final int port = 8080;
    static final ArrayList<GuessingGame> gameSessions = new ArrayList<>();
    int cookieCtr;

    public static void main(String[] args) throws IOException {

        ServerSocket welcomeSocket = new ServerSocket(port);

        while (true) {
            Socket connectionSocket = welcomeSocket.accept();
            Runnable clientHandler = new ClientHandler(connectionSocket);
            new Thread(clientHandler).start();
        }
    }


}
