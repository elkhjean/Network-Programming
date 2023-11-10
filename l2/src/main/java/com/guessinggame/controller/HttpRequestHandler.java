package com.guessinggame.controller;

import java.io.IOException;
import java.net.*;

import com.guessinggame.model.GuessingGame;

public class HttpRequestHandler {

    public static void main(String[] args) throws IOException {

        ServerSocket welcomeSocket = new ServerSocket(Integer.valueOf(args[0]));

        while (true) {
            Socket connectionSocket = welcomeSocket.accept();
            Runnable serverProcess = new ClientHandler(connectionSocket, new GuessingGame());
            new Thread(serverProcess).start();
        }
    }
}
