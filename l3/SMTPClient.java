import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class SMTPClient {

    public static void main(String[] args) {
        // SMTP server details
        String smtpServer = "smtp.kth.se";
        int smtpPort = 587; // Use port 587 for STARTTLS

        // Accept user input for sender's email and credentials
        String from = getUserInput("Enter your email address: ");
        String username = getUserInput("Enter your username: ");
        String password = getPasswordInput("Enter your password: ");

        // Accept user input for recipient's email, subject, and body
        String to = getUserInput("Enter recipient's email address: ");
        String subject = getUserInput("Enter the email subject: ");
        String body = getUserInput("Enter the email body: ");

        // Connect to the SMTP server
        try {
            Socket socket = new Socket(smtpServer, smtpPort);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            // Receive the server's welcome message
            System.out.println("Server: " + in.readLine());

            // Send EHLO command
            out.println("EHLO " + smtpServer);

            // Check for STARTTLS support in the server's response
            boolean supportsStartTLS = false;
            String line;
            while ((line = in.readLine()) != null) {
                System.out.println("Server: " + line);
                if (line.startsWith("250-STARTTLS")) {
                    supportsStartTLS = true;
                }
                if (line.startsWith("250 ") && !line.startsWith("250-")) {
                    break;
                }
            }

            if (supportsStartTLS) {
                // Send STARTTLS command
                out.println("STARTTLS");
                String response = in.readLine();
                System.out.println("Server: " + response);

                // Check if the server supports STARTTLS
                if (response.startsWith("220 2.0.0 Ready to start TLS")) {

                    // Upgrade the connection to a secure connection
                    SSLSocketFactory sslSocketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
                    SSLSocket sslSocket = (SSLSocket) sslSocketFactory.createSocket(socket,
                            socket.getInetAddress().getHostAddress(), socket.getPort(), true);

                    // Reassign the socket to the SSL socket
                    socket = sslSocket;
                    in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    out = new PrintWriter(socket.getOutputStream(), true);

                    // Start STARTTLS handshake
                    sslSocket.startHandshake();

                    // Send EHLO again to establish the secure connection
                    out.println("EHLO " + smtpServer);
                    while ((line = in.readLine()) != null) {
                        System.out.println("Server: " + line);
                        if (line.startsWith("250 ") && !line.startsWith("250-")) {
                            break;
                        }
                    }

                    // Send authentication (BASE64 encoded) over the secure connection
                    String encodedUser = Base64.getEncoder()
                            .encodeToString((username).getBytes(StandardCharsets.UTF_8));
                    String encodedPw = Base64.getEncoder()
                            .encodeToString(password.getBytes(StandardCharsets.UTF_8));

                    out.println("AUTH LOGIN");
                    line = in.readLine();
                    System.out.println(line);

                    out.println(encodedUser);
                    line = in.readLine();
                    System.out.println(line);

                    out.println(encodedPw);
                    line = in.readLine();
                    System.out.println(line);

                    // Send MAIL FROM command
                    out.println("MAIL FROM:<" + from + ">");
                    line = in.readLine();
                    System.out.println(line);
                    // Send RCPT TO command
                    out.println("RCPT TO:<" + to + ">");
                    line = in.readLine();
                    System.out.println(line);
                    // Send DATA command
                    out.println("DATA");
                    line = in.readLine();
                    System.out.println("DATA " + line);
                    // Send email headers and content
                    out.println("From: " + from);
                    out.println("To: " + to);
                    out.println("Subject: " + subject);
                    out.println();
                    out.println(body);
                    out.println(".");
                    out.flush();
                    line = in.readLine();
                    if (line.contains("Ok"))
                        System.out.println("Email sent successfully! " + line);
                    else
                        System.out.println("Error sending email! " + line);
                } else {
                    System.out.println("Server does not support STARTTLS.");
                }
            }

        } catch (Exception e) {
            // Handle exceptions, e.g., failed to connect to the server
            e.printStackTrace();
        }
    }

    private static String getUserInput(String prompt) {
        System.out.print(prompt);
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            return reader.readLine();
        } catch (IOException e) {
            throw new RuntimeException("Error reading user input", e);
        }
    }

    private static String getPasswordInput(String prompt) {
        Console console = System.console();
        if (console == null) {
            throw new RuntimeException("Console not available. Cannot hide password.");
        }

        char[] passwordChars = console.readPassword(prompt);
        return new String(passwordChars);
    }
}