package com.guessinggame.controller;

import com.guessinggame.model.gameModel;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Base64;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;

public class HttpServer {
    static int port = 443;
    static ArrayList<gameModel> gameSessions = new ArrayList<>();
    int cookieCtr;

    public static void main(String[] args) throws IOException {

        try {
            KeyStore ks = KeyStore.getInstance("PKCS12");
            InputStream is = new FileInputStream(new File("l2\\src\\main\\java\\com\\guessinggame\\controller\\myserverkeystore.pfx"));
            char[] pwd = "qwerty".toCharArray();
            System.out.println("Läser in certifikat");
            ks.load(is, pwd);
            System.out.println("Klart");
            SSLContext ctx = SSLContext.getInstance("TLS");
            KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            kmf.init(ks, pwd);
            // Motivera de tre parametrarna servernyckel,
            ctx.init(kmf.getKeyManagers(), null, null);

            SSLServerSocketFactory ssf = ctx.getServerSocketFactory();
            SSLServerSocket ss = (SSLServerSocket) ssf.createServerSocket(443);
            String[] cipher = { "TLS_RSA_WITH_AES_128_CBC_SHA" };

            while (true) {
                SSLSocket connectionSocket = (SSLSocket) ss.accept();
                Runnable clientHandler = new ClientHandler(connectionSocket);
                new Thread(clientHandler).start();
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());

        }
    }

    static gameModel getSession(String sessionId) {
        if (sessionId != null)
            synchronized (gameSessions) {
                for (gameModel session : gameSessions) {
                    String gameSessionId = session.getSessionId();
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

    static void removeGameSession(gameModel gameSession) {
        synchronized (gameSessions) {
            gameSessions.remove(gameSession);
        }
    }

    private static String generateCookie() {
        byte[] cookieBytes = new byte[16];
        SecureRandom secRand = new SecureRandom();
        secRand.nextBytes(cookieBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(cookieBytes); // URL safe string
    }

}