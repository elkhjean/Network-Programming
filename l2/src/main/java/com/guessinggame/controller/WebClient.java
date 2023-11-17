package com.guessinggame.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

public class WebClient {
    private static final String URL_STRING = "http://127.0.0.1:8080";
    private static final int TOTAL_GAMES = 100;
    private static final Random random = new Random();

    public static void main(String[] args) {
        WebClient client = new WebClient();
        client.playMultipleGames(TOTAL_GAMES);
    }

    private void playMultipleGames(int totalGames) {
        int totalGuesses = 0;
        for (int i = 0; i < totalGames; i++) {
            totalGuesses += playSingleGame();
        }
        double averageGuesses = (double) totalGuesses / totalGames;
        System.out.println("Average number of guesses: " + averageGuesses);
    }

    private int playSingleGame() {
        int numberOfGuesses = 0;
        int min = 1;
        int max = 100;
        String cookie = initializeGame();

        while (true) {
            int guess = generateRandomGuess(min, max);
            String response = makeGuess(cookie, guess);
            numberOfGuesses++;
            System.out.println("guess " + guess);

            if (response.contains("lower")) {
                max = guess - 1;
            } else if (response.contains("higher")) {
                min = guess + 1;
            } else if (response.contains("You")) {
                return numberOfGuesses;
            }
        }
    }

    private String initializeGame() {
        try {
            HttpURLConnection connection = createConnection(URL_STRING, "GET", null);
            String cookie = connection.getHeaderField("Set-cookie");
            connection.disconnect();
            return cookie;
        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize game: " + e.getMessage(), e);
        }
    }

    private String makeGuess(String cookie, int guess) {
        try {
            String body = "guess=" + guess;
            HttpURLConnection connection = createConnection(URL_STRING, "POST", cookie);
            writeBody(connection, body);

            String response = readResponse(connection);
            connection.disconnect();
            return response;
        } catch (IOException e) {
            throw new RuntimeException("Failed to make guess: " + e.getMessage(), e);
        }
    }

    private HttpURLConnection createConnection(String urlString, String method, String cookie) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(method);
        if (cookie != null) {
            connection.setRequestProperty("Cookie", cookie);
        }
        return connection;
    }

    private void writeBody(HttpURLConnection connection, String body) throws IOException {
        connection.setDoOutput(true);
        try (OutputStream os = connection.getOutputStream()) {
            os.write(body.getBytes());
            os.flush();
        }
    }

    private String readResponse(HttpURLConnection connection) throws IOException {
        try (InputStream is = connection.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                response.append(line);
            }
            return response.toString();
        }
    }

    private int generateRandomGuess(int min, int max) {
        System.out.println("min " + min);
        System.out.println("max " + max);
        if(max < 1)
            max = 1;
        return random.nextInt((max - min) + 1) + min;
    }

}
