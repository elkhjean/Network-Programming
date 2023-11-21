
import java.io.*;
import java.net.MalformedURLException;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class SSLClient {
    public static void main(String[] args) {
        SSLSocketFactory sf = (SSLSocketFactory) SSLSocketFactory.getDefault();

        HttpsURLConnection.setDefaultSSLSocketFactory(sf);
        SSLSocket socket = null;

        String host = "webmail.kth.se";

        try {
            socket = (SSLSocket) sf.createSocket(host, 993); // default HTTPS port

        } catch (MalformedURLException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        PrintWriter writer = null;
        BufferedReader reader = null;
        try {
            socket.startHandshake();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        try {
            writer = new PrintWriter(socket.getOutputStream());
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        int msgNum = 0;
        Console console = System.console();
        writer.println(msgNum++ + " LOGIN " + new String(console.readLine("Username: ")) + " "
                + new String(console.readPassword("Password: ")));
        writer.println("a" + msgNum++ +" select inbox");
        writer.println("a" +msgNum++ +" search all");
        writer.println("a" +msgNum++ +" fetch 1 body[header]");
        writer.println("a" +msgNum++ +" fetch 1 body[text]");
        writer.println("a" +msgNum++ +" logout");
        writer.flush();

        try {
            String str;
            while ((str = reader.readLine()) != null)
                System.out.println(str);
            writer.close();
            reader.close();
            socket.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
