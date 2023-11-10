package com.guessinggame.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.StringTokenizer;

import com.guessinggame.model.GuessingGame;

public class ClientHandler implements Runnable {
    private Socket connectionSocket;
    private GuessingGame gameInstance;

    public ClientHandler(Socket connectionSocket) {
        this.connectionSocket = connectionSocket;
    }

    @Override
    public void run() {
        BufferedReader buffRead;
        try {
            buffRead = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));

            // TODO: handle exception

            parseRequest(buffRead);

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private String parseRequest(BufferedReader buffRead) throws IOException {
        String path = null;
        String version = null;
        String cookie = null;
        String guess = null;
        String method = null;

        StringTokenizer tokenizer;
        for (String line = buffRead.readLine(); line != null; line = buffRead.readLine()) {
            tokenizer = new StringTokenizer(line);
            if (tokenizer.hasMoreTokens())
                switch (tokenizer.nextToken()) {
                    case "GET":
                        method = "GET";
                        path = tokenizer.nextToken();
                        version = tokenizer.nextToken();
                        break;

                    case "POST":
                        guess = tokenizer.nextToken();
                        method = "POST";
                        break;

                    case "Cookie":
                        cookie = tokenizer.nextToken();
                        break;

                    case "Content-Type":
                        break;
                    default:
                        break;
                }
        }

        if (method.equals("GET"))
            this.gameInstance = getSession(cookie);
        return null;
    }

    public GuessingGame getSession(String sessionId) {
        if (sessionId != null)
            for (GuessingGame session : HttpServer.gameSessions) {
                String gameSessionId = session.getSessionId();
                if (gameSessionId.equals(sessionId))
                    return session;
            }

        return createNewGameSession();
    }

    private GuessingGame createNewGameSession() {
        GuessingGame newGame = new GuessingGame(generateCookie());
        HttpServer.gameSessions.add(newGame);
        return newGame;
    }

    private String generateCookie() {
        // todo generate random sessionid using securerandom
        return null;
    }

}
