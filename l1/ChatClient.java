package l1;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class ChatClient {
    private Socket connectionSocket;
    // Sätt igång tråd att lyssna på socket efter msg
    // vänta på input från användare
    // skriv till server genom socket
    public static void main(String[] args) {
        
    }

    private class Receiver implements Runnable {
        private Socket connectionSocket;
        private BufferedReader bufRead;

        public Receiver() throws IOException {
            InputStream inStream = connectionSocket.getInputStream();
            bufRead = new BufferedReader(new InputStreamReader(inStream));
        }

        @Override
        public void run() {
            while (true) {
                try {
                    System.out.println(bufRead.readLine());
                } catch (IOException e) {
                }
            }
        }
    }
}
