//package l1;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * This is the chatserver, Starts with the argument of a port number to host it
 * from
 */
public class ChatServer {
    private static final ArrayList<ClientHandler> clientHandlers = new ArrayList<>();

    public static void main(String[] args) throws IOException {

        if (args.length < 1)
            System.out.println("Usage: Start by providing a port number <port>");
        int listenPort = Integer.parseInt(args[0]);

        try (ServerSocket welcomeSocket = new ServerSocket(listenPort)) {
            System.out.println("Started chat server on port " + listenPort); // Just to see that it's working'
            while (true) {
                Socket client = welcomeSocket.accept();
                // System.out.println("New user connected");
                ClientHandler t = new ClientHandler(client);
                synchronized (clientHandlers) {
                    clientHandlers.add(t);
                }
                t.start();

            }
        } catch (IOException ex) {
            System.err.print("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private static class ClientHandler extends Thread {
        private final Socket clientSocket;
        private PrintWriter writer;

        public ClientHandler(Socket client) {
            this.clientSocket = client;
        }

        @Override
        public void run() {

            try {
                InputStream input = clientSocket.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                OutputStream out = clientSocket.getOutputStream();
                writer = new PrintWriter(out, true);
                String line = "";
                String username = reader.readLine();
                String server = "New user connected: " + username;
                writer.println(server); // Send response to client
                updateClients(server, this); // Updates all clients
                while (!line.equals("exit")) {
                    line = reader.readLine();
                    server = username + ": " + line;
                    updateClients(server, this); // Updates all clients
                }
                // tar bort användaren när denna avslutar med frasen "exit"
                synchronized (clientHandlers) {
                    clientHandlers.remove(this);
                }
                System.out.println("Removed client " + username);
                updateClients(username + " left server", this);
                clientSocket.close();

            } catch (IOException e) {
                // e.printStackTrace();
            }

        }

        public void updateClients(String msg, ClientHandler user) throws IOException {
            synchronized (clientHandlers) {
                for (ClientHandler ch : clientHandlers) { // Sends message to all other connected users
                    if (ch != user)
                        ch.writeMessage(msg);
                }
            }
        }

        public void writeMessage(String msg) throws IOException {
            writer.println(msg);
        }

    }
}