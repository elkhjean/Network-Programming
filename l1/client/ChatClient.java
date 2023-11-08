//package l1.client;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;


/**
 * This is the chat client program. Type "exit" to exit the program
 */
public class ChatClient {
    private final String hostname;
    private final int port;
    private String username;

    public ChatClient(String host, int port, String user) {
        this.hostname = host;
        this.port = port;
        this.username = user;
    }

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
            Receiver reader = new Receiver(socket); //Tread for reading responses from server
            Sender sender = new Sender(socket);//Tread for writing responses to server
            new Thread(reader).start();
            new Thread(sender).start();
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
                    System.out.println("\n" + bufRead.readLine()); //reading response from server
                    System.out.print(username + ": ");//Printing username to terminal
                } catch (IOException e) {
                    if (e.getMessage().equals("Socket closed")) {
                        System.out.println("Exiting chatserver");
                    } else System.out.println("Error reading from server: " + e.getMessage());
                    break;
                }
            }

        }
    }

    public class Sender implements Runnable {
        private Socket outSock;
        private PrintWriter writer; //for sending messages to server

        public Sender(Socket socket) {
            this.outSock = socket;

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
           // Console reader = System.console(); //reading input from user
            //BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            Scanner reader = new Scanner(System.in, "Cp850");
            writer.println(username);
            do {
                //text = reader.readLine(username + ": ");
                text = reader.nextLine();
                writer.println(text); //Send input from user to server
            } while (text != null && !text.equals("exit"));
            reader.close();
            try {
                outSock.close();
            } catch (IOException e) {
                System.out.println("Error writing to server" + e.getMessage());
            }
        }
    }
}