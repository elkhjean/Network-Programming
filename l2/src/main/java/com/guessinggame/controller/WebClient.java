package com.guessinggame.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;

public class WebClient {
    String cookie = "";
    String method = "GET";
    int numberOfguesses = 0;
    int numberOfgames = 0;
    int guess = 0;
    int min = 1;
    int max = 100;
    String url = "http://127.0.0.1:8080";

    public static void main(String[] args) {
        WebClient client = new WebClient();
        client.init();
    }

    private void init() {
        URL u;
        try {
            u = new URL(url);
            connect(u);
        } catch (MalformedURLException e) {
            System.out.println("Connection error: " + e.getMessage());
        }

    }

    private void connect(URL u) {
        try {
            HttpURLConnection h;
            h = (HttpURLConnection) u.openConnection();
            h.setRequestMethod(method);
            h.connect();
            response(h);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void response(HttpURLConnection h) {
        cookie = h.getHeaderField("Set-cookie");
        Random rand = new Random();
        guess = rand.nextInt(100);
        String gues = "guess=" + String.valueOf(guess);
        method = "POST";
        play(gues);

    }

    private void play(String guess) {
        try {
            URL u;
            u = new URL(url);
            HttpURLConnection h;

            h = (HttpURLConnection) u.openConnection();
            h.setRequestProperty("Cookie", cookie);
            h.setRequestMethod(method);

            h.setDoOutput(true);
            h.getOutputStream().write(guess.getBytes());
            h.connect();
            checkGuess(h);


        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void checkGuess(HttpURLConnection h) {
        InputStream is;
        try {
            is = h.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String str;
            Random rand = new Random();
            numberOfguesses++;
            while ((str = br.readLine()) != null) {
                if (str.contains("lower")) {
                    max = guess;
                    guess = rand.nextInt(min, max);
                } else if (str.contains("higher")) {
                    min = guess + 1;
                    if (min == max)
                        min--;
                    guess = rand.nextInt(min, max);
                } else if (str.contains("You")) {
                    numberOfgames++;
                    if (numberOfgames == 100) {
                        System.out.println("Average number of guesses " + (double) numberOfguesses / numberOfgames);
                        System.exit(0);
                    }
                    cookie = "";
                    method = "GET";
                    min = 1;
                    max = 100;
                    init();
                }
            }
            method = "POST";
            play("guess=" + String.valueOf(guess));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}