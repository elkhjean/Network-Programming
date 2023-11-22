import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.net.MalformedURLException;

public class SSLClient {
    public static void main(String[] args) {
        SSLSocketFactory sf = (SSLSocketFactory) SSLSocketFactory.getDefault();

        HttpsURLConnection.setDefaultSSLSocketFactory(sf);

        String host = "webmail.kth.se";
        PrintWriter writer = null;
        BufferedReader reader = null;
        try (SSLSocket socket = (SSLSocket) sf.createSocket(host, 993)) { // default HTTPS port

            socket.startHandshake();
            writer = new PrintWriter(socket.getOutputStream());
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            int msgNum = 0;

            Console console = System.console();
            writer.println(msgNum++ + " LOGIN " + (console.readLine("Username: ")) + " "
                    + new String(console.readPassword("Password: ")));
            writer.println("a" + msgNum++ + " select inbox");
            writer.println("a" + msgNum++ + " search all");
            writer.println("a" + msgNum++ + " fetch 4 body[header]");
            writer.println("a" + msgNum++ + " fetch 4 body[text]");
            writer.println("a" + msgNum++ + " logout");
            writer.flush();

            String response;
            while ((response = reader.readLine()) != null)
                System.out.println(response);
            writer.close();
            reader.close();

        } catch (MalformedURLException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}