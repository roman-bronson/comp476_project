import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) {

        //Server IP address (localhost means same computer)
        String host = "localhost";

        //int must match Server.java port
        int port = 1234;

        try (

                //Connect to the server
                Socket socket = new Socket(host, port);

                //Stream to read messages from server
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream())
                );

                //Stream to send messages to server
                PrintWriter out = new PrintWriter(
                        socket.getOutputStream(), true
                );

                //Read user input (your moves)
                Scanner scanner = new Scanner(System.in)
        ) {

            System.out.println("Connected. Waiting for messages...");

            String message;

            //Continuously read messages from the server
            while ((message = in.readLine()) != null) {

                System.out.println(message);

                //If server asks for a move, read input and send it
                if (message.contains("Your move")) {
                    String move = scanner.nextLine().trim();
                    out.println(move);
                }
            }

        } catch (IOException e) {

            // Handles connection errors
            System.out.println("Connection error: " + e.getMessage());
        }

        System.out.println("Client closed.");
    }
}
