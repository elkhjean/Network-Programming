package l1.client;

import java.io.Console;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class Sender extends Thread {
    private Socket outSock;
    private PrintWriter writer;
    private ChatClient client;

    public Sender(Socket socket, ChatClient client) {
        this.outSock = socket;
        this.client = client;
        try {
            OutputStream out = outSock.getOutputStream();
            writer = new PrintWriter(out, true);
        } catch (IOException e) {
            System.out.println("Error creating output stream" + e.getMessage());
        }
    }

    @Override
    public void run() {
        String text;
        Console reader = System.console();
        String user = client.getUserName();
        writer.println(user);

        do {
            text = reader.readLine("[" + user + "]: ");
            writer.println(text);
        } while (!text.equals("exit"));
        try {
            outSock.close();
        } catch (IOException e) {
            System.out.println("Error writing to server" + e.getMessage());
        }
    }
}