package l1;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ChatServer {
    private static ArrayList<ClientHandler> clientHandlers;

    public static void main(String[] args) throws IOException {

        ServerSocket welcomeSocket = new ServerSocket();

        while (true) {
            Socket connectionSocket = welcomeSocket.accept();
            ClientHandler t = new ClientHandler(connectionSocket);
            clientHandlers.add(t);
            new Thread(t).start();

        }
    }

    private static class ClientHandler implements Runnable {
        private Socket connectionSocket;

        public ClientHandler(Socket connectionSocket) {
            this.connectionSocket = connectionSocket;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    // LIGG OCH LYSSNA
                    connectionSocket.wait();
                    // FÅR NÅGOT KALLA METOD NOTIFY I ALLA HANDLERS I LISTAN
                    updateClients(connectionSocket.getInputStream().readAllBytes());
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        public void updateClients(byte[] msg) throws IOException {
            for (ClientHandler ch : clientHandlers) {
                ch.writeMessage(msg);
            }
        }

        public void writeMessage(byte[] msg) throws IOException {
            connectionSocket.getOutputStream().write(msg);
        }

    }
}
