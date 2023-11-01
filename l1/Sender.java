package l1;

import java.net.Socket;

public class Sender implements Runnable {
    Socket outSock;

    public Sender(Socket outSock){
        this.outSock = outSock;
    }

    @Override
    public void run() {
        
        
    }
}
