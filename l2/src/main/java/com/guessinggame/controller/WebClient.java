package com.guessinggame.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WebClient {
    public static void main(String[] args) {
        try {
            URL u;
            HttpURLConnection h;
            InputStream is;
            //u = new URL("https://www.kth.se");
            u = new URL("https://127.0.0.1:8080");

            h = (HttpURLConnection) u.openConnection();
            h.setRequestProperty("Cookie", "SESSIONID=3242342343243;exp");
            //Addera headfält mm innan "submit"

            h.setRequestMethod("GET");
            h.connect();
            //Nu kan vi endast läsa för nu är redan request skickad och endast response finns.
            //h.getHeaderField(name)
            is = h.getInputStream();
            System.out.println(h.getHeaderField("Server"));
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String str;
            while ((str = br.readLine()) != null) {
                System.out.println(str);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}