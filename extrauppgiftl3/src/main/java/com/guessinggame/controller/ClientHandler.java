package com.guessinggame.controller;

import com.guessinggame.model.gameModel;
import com.guessinggame.view.gameView;

import javax.net.ssl.SSLSocket;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ClientHandler implements Runnable {
    private final SSLSocket connectionSocket;
    private gameModel gameInstance;

    public ClientHandler(SSLSocket connectionSocket) {
        this.connectionSocket = connectionSocket;
    }

    @Override
    public void run() {
        try {

            BufferedReader reader = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));

            HttpRequest request = parseRequest(reader);

            if (request.method.equalsIgnoreCase("GET"))
                handleGetRequest(request);
            else if (request.method.equalsIgnoreCase("POST"))
                handlePostRequest(request);

            connectionSocket.close();

        } catch (IOException e) {
            System.out.println("Exception reading input stream: " + e.getMessage());
        }
    }

    private void handlePostRequest(HttpRequest request) throws IOException {
        String[] params = request.body.split("=");
        this.gameInstance = HttpsServer.getSession(request.getHeader("cookie"));
        String gameResponse = this.gameInstance.compareGuess(params[1]);
        String htmlResponse = gameView.getGamePage(gameResponse);
        httpRespond(htmlResponse, request);
        if (this.gameInstance.getWinStatus()) {
            HttpsServer.removeGameSession(this.gameInstance);
        }
    }

    private void handleGetRequest(HttpRequest request) throws IOException {
        if (!"/favicon.ico".equals(request.path)) { // Ignore any additional request to retrieve the bookmark-icon.
            this.gameInstance = HttpsServer.getSession(request.getHeader("cookie"));
            String htmlResponse = gameView.getGamePage();
            httpRespond(htmlResponse, request);
        }
    }

    private void httpRespond(String htmlResponse, HttpRequest request) throws IOException {

        PrintWriter out = new PrintWriter(connectionSocket.getOutputStream());

        // status line
        out.println("HTTPS/1.1 200 OK");

        // headers
        out.println("Content-Type: text/html; charset=UTF-8");
        out.println("Content-Length: " + htmlResponse.length());

        // Check if a new game session was created and set cookie if needed
        // And check if the cookie has expired
        if (request.getHeader("cookie") == null
                || !Objects.equals(request.getHeader("cookie"), gameInstance.getSessionId())) {
            out.println("Set-Cookie: sessionId=" + gameInstance.getSessionId());
        }

        // End headers with empty line
        out.println();

        // Write response body (HTML content)
        out.println(htmlResponse);
        out.flush();
    }

    private HttpRequest parseRequest(BufferedReader reader) throws IOException {

        // Creation of a new httpRequest obj
        HttpRequest request = new HttpRequest();

        // Read first first line (request line) of http request

        String line = reader.readLine();
        // Parse request line to extract parameters
        String[] requestLine = line.split(" ");
        request.method = requestLine[0];
        request.path = requestLine[1];
        request.version = requestLine[2];
        if ("/favicon.ico".equals(request.path)) // ignoring the favicon icon.
            return request;

        // Parse each remaining line of http request until an empty line is encountered
        // and extract headers. If cookie, get only the sessionID part
        while (!(line = reader.readLine()).isEmpty()) {
            String[] headerLine = line.split(":", 2);
            if (headerLine[0].trim().equalsIgnoreCase("cookie"))
                headerLine[1] = headerLine[1].split("=", 2)[1];
            request.setHeader(headerLine[0].trim(), headerLine[1].trim());
        }

        // If method is post, all remaining lines are part of body, we extract them
        if (request.method.equalsIgnoreCase("POST")) {
            int bodyLength = Integer.parseInt(request.getHeader("content-length"));
            char[] bodyData = new char[bodyLength];
            reader.read(bodyData, 0, bodyLength);
            request.body = new String(bodyData);
        }
        return request;
    }

    private class HttpRequest {
        String method;
        String path;
        String version;
        Map<String, String> headers = new HashMap<>();
        String body;

        void setHeader(String key, String value) {
            headers.put(key.toLowerCase(), value);
        }

        public String getHeader(String key) {
            return headers.get(key.toLowerCase());
        }
    }

}