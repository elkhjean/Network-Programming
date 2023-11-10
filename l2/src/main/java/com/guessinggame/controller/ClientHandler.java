package com.guessinggame.controller;

import java.net.Socket;

import com.guessinggame.model.GuessingGame;

public class ClientHandler implements Runnable {
    Socket connectionSocket;
    GuessingGame gameInstance;

    public ClientHandler(Socket connectionSocket, GuessingGame gameInstance) {
        this.connectionSocket=connectionSocket;
        this.gameInstance = gameInstance;
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'run'");
    }
    
}
