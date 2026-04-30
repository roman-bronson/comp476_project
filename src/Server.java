import java.io.*;
import java.net.*;

public class Server {

    //Tic Tac Toe board
    private static char[][] board = {
            {' ', ' ', ' '},
            {' ', ' ', ' '},
            {' ', ' ', ' '}
    };

    //Current player symbol
    private static char currentPlayer = 'X';

    public static void main(String[] args) {

        //Port the server will listen on
        int port = 1234;

        try (
                // Create ServerSocket to accept connections
                ServerSocket serverSocket = new ServerSocket(port)
        ) {

            System.out.println("Tic Tac Toe Server started on port " + port);

            //Accept Player X
            System.out.println("Waiting for Player X...");
            Socket player1 = serverSocket.accept();
            System.out.println("Player X connected.");

            //Accept Player O
            System.out.println("Waiting for Player O...");
            Socket player2 = serverSocket.accept();
            System.out.println("Player O connected.");

            //Start the game once both players are connected
            handleGame(player1, player2);

        } catch (IOException e) {

            //Handle server errors
            System.out.println("Server error: " + e.getMessage());
        }
    }

    private static void handleGame(Socket p1, Socket p2) throws IOException {

        //Input/output streams for Player X
        BufferedReader in1 = new BufferedReader(
                new InputStreamReader(p1.getInputStream())
        );
        PrintWriter out1 = new PrintWriter(
                p1.getOutputStream(), true
        );

        //Input/output streams for Player O
        BufferedReader in2 = new BufferedReader(
                new InputStreamReader(p2.getInputStream())
        );
        PrintWriter out2 = new PrintWriter(
                p2.getOutputStream(), true
        );

        //Tell each client their role
        out1.println("You are Player X");
        out2.println("You are Player O");

        //Send initial board to both players
        sendBoard(out1);
        sendBoard(out2);

        while (true) {

            //Choose current player's streams
            BufferedReader currentIn = (currentPlayer == 'X') ? in1 : in2;
            PrintWriter currentOut = (currentPlayer == 'X') ? out1 : out2;

            //Choose opponent output (so both get messages)
            PrintWriter otherOut = (currentPlayer == 'X') ? out2 : out1;

            //Notifies turns
            currentOut.println("Your move (row col) e.g., 0 2:");
            otherOut.println("Waiting for Player " + currentPlayer + " to move...");

            //Read move from current player
            String input = currentIn.readLine();

            //If client disconnects
            if (input == null) {
                out1.println("A player disconnected. Game over.");
                out2.println("A player disconnected. Game over.");
                break;
            }

            //Parse and validate input format
            String[] parts = input.trim().split("\\s+");
            if (parts.length != 2) {
                currentOut.println("Invalid format. Enter: row col (example: 1 2)");
                continue;
            }

            int row, col;

            try {
                row = Integer.parseInt(parts[0]);
                col = Integer.parseInt(parts[1]);
            } catch (NumberFormatException e) {
                currentOut.println("Invalid numbers. Enter integers like: 0 2");
                continue;
            }

            //Validate bounds
            if (row < 0 || row > 2 || col < 0 || col > 2) {
                currentOut.println("Out of bounds. Row/Col must be 0, 1, or 2.");
                continue;
            }

            //Validate cell is empty
            if (board[row][col] != ' ') {
                currentOut.println("Cell already taken. Try again.");
                continue;
            }

            //Apply move
            board[row][col] = currentPlayer;

            //Send updated board to both players
            sendBoard(out1);
            sendBoard(out2);

            //Check win
            if (checkWin(currentPlayer)) {
                out1.println("Player " + currentPlayer + " wins!");
                out2.println("Player " + currentPlayer + " wins!");
                break;
            }

            //Check draw
            if (isDraw()) {
                out1.println("Draw game!");
                out2.println("Draw game!");
                break;
            }

            //Switch player turn
            currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
        }

        //Close sockets after game ends
        p1.close();
        p2.close();
    }

    private static void sendBoard(PrintWriter out) {

        //Print board lines with separators
        out.println("Board:");
        out.println(board[0][0] + "|" + board[0][1] + "|" + board[0][2]);
        out.println("-+-+-");
        out.println(board[1][0] + "|" + board[1][1] + "|" + board[1][2]);
        out.println("-+-+-");
        out.println(board[2][0] + "|" + board[2][1] + "|" + board[2][2]);
    }

    private static boolean checkWin(char player) {

        //Checks rows
        for (int i = 0; i < 3; i++) {
            if (board[i][0] == player && board[i][1] == player && board[i][2] == player) {
                return true;
            }
        }

        //Checks columns
        for (int i = 0; i < 3; i++) {
            if (board[0][i] == player && board[1][i] == player && board[2][i] == player) {
                return true;
            }
        }

        //Checks diagonals
        if (board[0][0] == player && board[1][1] == player && board[2][2] == player) {
            return true;
        }

        if (board[0][2] == player && board[1][1] == player && board[2][0] == player) {
            return true;
        }

        return false;
    }

    private static boolean isDraw() {

        // If any cell is empty, game is not a draw
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                if (board[r][c] == ' ') {
                    return false;
                }
            }
        }

        return true;
    }
}
