package com.guessinggame.controller;

import java.io.IOException;
import java.net.*;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Base64;

import com.guessinggame.model.gameModel;

public class HttpServer {
    static final int port = 8080;
    static ArrayList<gameModel> gameSessions = new ArrayList<>();
    int cookieCtr;

    public static void main(String[] args) throws IOException {

        ServerSocket welcomeSocket = new ServerSocket(port);

        while (true) {
            Socket connectionSocket = welcomeSocket.accept();
            Runnable clientHandler = new ClientHandler(connectionSocket);
            new Thread(clientHandler).start();
        }
    }

    static gameModel getSession(String sessionId) {
        if (sessionId != null)
            synchronized (gameSessions) {
                for (gameModel session : gameSessions) {
                    String gameSessionId = session.getSessionId();
                    System.out.println("cookie: " + gameSessionId);
                    if (gameSessionId.equals(sessionId))
                        return session;
                }
            }
        return createNewGameSession();
    }

    private static gameModel createNewGameSession() {
        gameModel newGame = new gameModel(generateCookie());
        synchronized (gameSessions) {
            gameSessions.add(newGame);
        }
        return newGame;
    }

    private static String generateCookie() {
        byte[] cookieBytes = new byte[16];
        SecureRandom secRand = new SecureRandom();
        secRand.nextBytes(cookieBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(cookieBytes); // URL safe string
    }

}