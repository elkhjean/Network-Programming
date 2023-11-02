package l1;


import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ChatServer {
    private static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();

    public static void main(String[] args) throws IOException {

        if (args.length < 1) System.out.println("Usage: Start by providing a port number <port>");
        int listenPort = Integer.parseInt(args[0]);

        try (ServerSocket welcomeSocket = new ServerSocket(listenPort)) {
            System.out.println("Started chat server on port " + listenPort);
            while (true) {
                Socket client = welcomeSocket.accept();
                System.out.println("New user connected");
                ClientHandler t = new ClientHandler(client);
                clientHandlers.add(t);
                t.start();

            }
        } catch (IOException ex) {
            System.err.print("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private static boolean number(String str) {
        return str != null && str.matches("[0-9]+");
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
                String line;
                String username = reader.readLine();
                String server = "New user connected: " + username;
                writer.println(server);
                do {
                    line = reader.readLine();
                    server = "[" + username + "]: " + line;
                    updateClients(server, this);
                } while (!line.equals("exit"));
                //tar bort användaren när denna avslutar med frasen "exit"
                clientHandlers.remove(this);
                clientSocket.close();
                //kanske kan ha något server meddelande om att denna user har avslutat

                // LIGG OCH LYSSNA
                //clientSocket.wait();
                // FÅR NÅGOT KALLA METOD NOTIFY I ALLA HANDLERS I LISTAN
                //updateClients(clientSocket.getInputStream().readAllBytes(), this);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        public void updateClients(String msg, ClientHandler user) throws IOException {
            for (ClientHandler ch : clientHandlers) { //writes to all other connected user
                if (ch != user) ch.writeMessage(msg);
            }
        }

        public void writeMessage(String msg) throws IOException {
            writer.println(msg);
            //clientSocket.getOutputStream().write(msg);
        }

    }
}