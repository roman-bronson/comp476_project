import java.io.*;
import java.net.*;

public class ClientHandler implements Runnable {
    private Socket socket;
    private BufferedReader input;
    private PrintWriter output;
    private char symbol; // CHANGED: String -> char

    public ClientHandler(Socket socket, char symbol) {
        this.socket = socket;
        this.symbol = symbol;

        try {
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            System.out.println("Error setting up client: " + e.getMessage());
        }
    }

    public char getSymbol() {
        return symbol;
    }

    public void sendMessage(String message) {
        output.println(message);
    }

    @Override
    public void run() {
        try {
            String message;

            while ((message = input.readLine()) != null) {
                //prevents messages from being sent after game ends
                if (Server.game.isGameOver()) {
                    break;
                }
                try {
                    int move = Integer.parseInt(message);
                    Server.handleMove(this, move);
                } catch (NumberFormatException e) {
                    sendMessage("Please enter a number from 1 to 9.");
                }
            }

        } catch (IOException e) {
            System.out.println("Player " + symbol + " disconnected.");
            Server.broadcast("Player " + symbol + " disconnected. Game over.");
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                System.out.println("Could not close socket.");
            }
        }
    }
}