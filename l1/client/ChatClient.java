//package l1.client;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
//import l1.client.Sender;
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
                    System.out.print("[" + username + "]: ");
                } catch (IOException e) {
                    if (e.getMessage().equals("Socket closed")) {
                        System.out.println("Exiting chatserver");
                    }
                    else System.out.println("Error reading from server: " + e.getMessage());

                    //System.exit(1);
                    break;
                }
            }

        }
    }
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
            String text = "";
            Console reader = System.console();
            /*BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));*/
            //Scanner reader = new Scanner(System.in);
            String user = client.getUserName();
            writer.println(user);

            do {
                //text = reader.nextLine();
                text = reader.readLine("[" + user + "]: ");
                /*try {
                    text = reader.readLine();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }*/
                writer.println(text);
            } while (!text.equals("exit"));
            try {
                outSock.close();
            } catch (IOException e) {
                System.out.println("Error writing to server" + e.getMessage());
            }
        }
    }
}