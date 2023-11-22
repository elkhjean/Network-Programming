import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class SMTPClient {

    private static final String SERVER = "Server: ";

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
        try (Socket socket = new Socket(smtpServer, smtpPort)) {

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            // Receive the server's welcome message
            printResponse(in);

            // Send EHLO command
            out.println("EHLO " + smtpServer);
            boolean supportsStartTLS = checkStartTLS(in, false);
            // Check for STARTTLS support in the server's response
            if (supportsStartTLS) {
                // Send STARTTLS command
                out.println("STARTTLS");

                String response = in.readLine();
                printResponse(response);

                // Check if the server supports STARTTLS
                if (response.startsWith("220 2.0.0 Ready to start TLS")) {

                    // Upgrade the connection to a secure connection
                    SSLSocketFactory sslSocketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
                    try (SSLSocket sslSocket = (SSLSocket) sslSocketFactory.createSocket(socket,
                            socket.getInetAddress().getHostAddress(), socket.getPort(), true)) {

                        // Reassign the reader & writer to the SSL socket
                        in = new BufferedReader(new InputStreamReader(sslSocket.getInputStream()));
                        out = new PrintWriter(sslSocket.getOutputStream(), true);

                        // Start STARTTLS handshake
                        sslSocket.startHandshake();

                        // Send EHLO again to establish the secure connection
                        out.println("EHLO " + smtpServer);
                        while ((response = in.readLine()) != null) {
                            printResponse(response);
                            if (response.startsWith("250 ") && !response.startsWith("250-")) {
                                break;
                            }
                        }

                        // Send authentication (BASE64 encoded) over the secure connection
                        String encodedUser = Base64.getEncoder()
                                .encodeToString((username).getBytes(StandardCharsets.UTF_8));
                        String encodedPw = Base64.getEncoder()
                                .encodeToString(password.getBytes(StandardCharsets.UTF_8));

                        out.println("AUTH LOGIN");
                        printResponse(in);

                        out.println(encodedUser);
                        printResponse(in);

                        out.println(encodedPw);
                        printResponse(in);

                        // Send MAIL FROM command
                        out.println("MAIL FROM:<" + from + ">");
                        printResponse(in);
                        // Send RCPT TO command
                        out.println("RCPT TO:<" + to + ">");
                        printResponse(in);
                        // Send DATA command
                        out.println("DATA");
                        printResponse(in);
                        // Send email headers and content
                        out.println("From: " + from);
                        out.println("To: " + to);
                        out.println("Subject: " + subject);
                        out.println();
                        out.println(body);
                        out.println(".");
                        out.flush();
                        response = in.readLine();
                        checkIfEmailSent(response);
                    }
                } else {
                    System.out.println("Server does not support STARTTLS.");
                }
            }

        } catch (Exception e) {
            // Handle exceptions, e.g., failed to connect to the server
            System.out.println("Error connecting to server: " + e.getMessage());
        }
    }

    private static void checkIfEmailSent(String line) {
        if (line.contains("Ok"))
            System.out.println("Email sent successfully!\n" + line.split(":")[1]);
        else
            System.out.println("Error sending email!\n" + line);
    }

    private static void printResponse(String response) {
        System.out.println(SERVER + response);
    }

    private static void printResponse(BufferedReader in) throws IOException {
        System.out.println(SERVER + in.readLine());
    }

    private static boolean checkStartTLS(BufferedReader in, boolean supportsStartTLS) throws IOException {
        String line;
        while ((line = in.readLine()) != null) {
            System.out.println(SERVER + line);
            if (line.startsWith("250-STARTTLS")) {
                supportsStartTLS = true;
            }
            if (line.startsWith("250 ") && !line.startsWith("250-")) {
                break;
            }
        }
        return supportsStartTLS;
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