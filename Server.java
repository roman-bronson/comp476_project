import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    private static final int PORT = 5000;

    static String[][] board = {
            {"1", "2", "3"},
            {"4", "5", "6"},
            {"7", "8", "9"}
    };

    static List<ClientHandler> clients = new ArrayList<>();
    static String currentTurn = "X";
    static boolean gameOver = false;

    public static void main(String[] args) {
        System.out.println("Server started...");

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {

            while (clients.size() < 2) {
                Socket socket = serverSocket.accept();

                String symbol = clients.size() == 0 ? "X" : "O";

                ClientHandler handler = new ClientHandler(socket, symbol);
                clients.add(handler);
                new Thread(handler).start();

                handler.sendMessage("You are player " + symbol);
                System.out.println("Player " + symbol + " connected.");
            }

            broadcast("Both players connected. Game starting!");
            broadcast(boardToString());
            broadcast("Player X goes first.");

        } catch (IOException e) {
            System.out.println("Server error: " + e.getMessage());
        }
    }

    public static synchronized void handleMove(ClientHandler player, int move) {
        if (gameOver) {
            player.sendMessage("Game is already over.");
            return;
        }

        if (!player.getSymbol().equals(currentTurn)) {
            player.sendMessage("Not your turn.");
            return;
        }

        if (!isValidMove(move)) {
            player.sendMessage("Invalid move. Choose an open number 1-9.");
            return;
        }

        makeMove(move, player.getSymbol());

        broadcast("Player " + player.getSymbol() + " chose " + move);
        broadcast(boardToString());

        if (checkWinner(player.getSymbol())) {
            broadcast("Player " + player.getSymbol() + " wins!");
            gameOver = true;
            return;
        }

        if (isDraw()) {
            broadcast("Game ended in a draw.");
            gameOver = true;
            return;
        }

        currentTurn = currentTurn.equals("X") ? "O" : "X";
        broadcast("Player " + currentTurn + "'s turn.");
    }

    public static boolean isValidMove(int move) {
        if (move < 1 || move > 9) return false;

        int row = (move - 1) / 3;
        int col = (move - 1) % 3;

        return board[row][col].equals(String.valueOf(move));
    }

    public static void makeMove(int move, String symbol) {
        int row = (move - 1) / 3;
        int col = (move - 1) % 3;
        board[row][col] = symbol;
    }

    public static boolean checkWinner(String player) {
        return
                board[0][0].equals(player) && board[0][1].equals(player) && board[0][2].equals(player) ||
                        board[1][0].equals(player) && board[1][1].equals(player) && board[1][2].equals(player) ||
                        board[2][0].equals(player) && board[2][1].equals(player) && board[2][2].equals(player) ||

                        board[0][0].equals(player) && board[1][0].equals(player) && board[2][0].equals(player) ||
                        board[0][1].equals(player) && board[1][1].equals(player) && board[2][1].equals(player) ||
                        board[0][2].equals(player) && board[1][2].equals(player) && board[2][2].equals(player) ||

                        board[0][0].equals(player) && board[1][1].equals(player) && board[2][2].equals(player) ||
                        board[0][2].equals(player) && board[1][1].equals(player) && board[2][0].equals(player);
    }

    public static boolean isDraw() {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (!board[row][col].equals("X") && !board[row][col].equals("O")) {
                    return false;
                }
            }
        }
        return true;
    }

    public static String boardToString() {
        return "\n-------------\n| " + board[0][0] + " | " + board[0][1] + " | " + board[0][2] + " |\n" +
                "-------------\n| " + board[1][0] + " | " + board[1][1] + " | " + board[1][2] + " |\n" +
                "-------------\n| " + board[2][0] + " | " + board[2][1] + " | " + board[2][2] + " |\n" +
                "-------------";
    }

    public static void broadcast(String message) {
        for (ClientHandler client : clients) {
            client.sendMessage(message);
        }
    }
}