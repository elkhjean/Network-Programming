package l1.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * This is the chat client program. Type "exit" to exit the program
 */
public class ChatClient {
    private final String hostname;
    private final int port;

    private String username; //kanske kan ha så man kan urskilja vem som skriver vad.

    public ChatClient(String host, int port, String user) {
        this.hostname = host;
        this.port = port;
        this.username = user;
    }

    // Sätt igång tråd att lyssna på socket efter msg
    // vänta på input från användare
    // skriv till server genom socket
    public static void main(String[] args) {
        if (args.length < 3) System.out.println("Usage: <hostname> <port> <username> ");
        String host = args[0];
        int port = Integer.parseInt(args[1]);
        String user = args[2];
        ChatClient client = new ChatClient(host, port, user);
        client.start();
    }

    public String getUserName() {
        return this.username;
    }

    private void start() {
        try {
            Socket socket = new Socket(hostname, port);
            System.out.println("Connected to chatserver");
            Receiver reader = new Receiver(socket);
            //Sender sender = new Sender(socket, this);
            new Thread(reader).start();
            new Sender(socket, this).start();
        } catch (UnknownHostException e) {
            System.out.println("Server not found: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO error: " + e.getMessage());
        }
    }

    private class Receiver implements Runnable {
        private Socket connectionSocket;
        private BufferedReader bufRead;

        public Receiver(Socket socket) throws IOException {
            this.connectionSocket = socket;
            InputStream inStream = connectionSocket.getInputStream();
            bufRead = new BufferedReader(new InputStreamReader(inStream));
        }

        @Override
        public void run() {
            while (true) {
                try {
                    System.out.println("\n" + bufRead.readLine());
                    System.out.println("[" + username + "]: ");
                } catch (IOException e) {
                    System.out.println("Error reading from server: " + e.getMessage());
                    break;
                }
            }
        }
    }
}