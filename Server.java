import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    private static final int PORT = 5000;

    // CHANGED: GameLogic now owns board, turns, winner, and gameOver
    public static GameLogic game = new GameLogic();

    static List<ClientHandler> clients = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println("Server started...");

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {

            while (clients.size() < 2) {
                Socket socket = serverSocket.accept();

                // CHANGED: String -> char
                char symbol = clients.size() == 0 ? 'X' : 'O';

                ClientHandler handler = new ClientHandler(socket, symbol);
                clients.add(handler);
                new Thread(handler).start();

                handler.sendMessage("You are player " + symbol);
                System.out.println("Player " + symbol + " connected.");
            }

            broadcast("Both players connected. Game starting!");
            broadcast("\nCurrent Board:");
            broadcast(game.getFormattedBoard());
            broadcast("Player " + game.getCurrentPlayer() + " goes first.");

        } catch (IOException e) {
            System.out.println("Server error: " + e.getMessage());
        }
    }

    public static synchronized void handleMove(ClientHandler player, int move) {

        if (game.isGameOver()) {
            player.sendMessage("Game is already over.");
            return;
        }

        if (player.getSymbol() != game.getCurrentPlayer()) {
            player.sendMessage("Not your turn. It is Player " + game.getCurrentPlayer() + "'s turn.");
            return;
        }

        boolean validMove = game.makeMove(move);

        if (!validMove) {
            player.sendMessage("Invalid move. Choose an open number from 1 to 9.");
            return;
        }

        broadcast("Player " + player.getSymbol() + " chose " + move);
        broadcast("Board: " + game.getBoardState());

        if (game.isGameOver()) {
            if (game.getWinner() != ' ') {
                broadcast("Player " + game.getWinner() + " wins!");
            } else {
                broadcast("Game ended in a draw.");
            }
            return;
        }

        broadcast("Player " + game.getCurrentPlayer() + "'s turn.");
    }

    public static void broadcast(String message) {
        for (ClientHandler client : clients) {
            client.sendMessage(message);
        }
    }
}