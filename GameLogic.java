public class GameLogic {
    private char[][] board;
    private int turn_count;
    private char winner;
    private boolean gameOver;
    private char currentPlayer;

    public GameLogic() {
        board = new char[][] {{'1', '2', '3'},
                             {'4','5', '6'},
                             {'7','8','9'}};
        turn_count = 0;
        winner = ' ';
        gameOver = false;
        currentPlayer = 'X';

    }

    public boolean checkWinner(int row, int col) {
        if (board[row][0] == currentPlayer && board[row][1] == currentPlayer && board[row][2] == currentPlayer) {
            return true;
        }
        if (board[0][col] == currentPlayer && board[1][col] == currentPlayer && board[2][col] == currentPlayer) {
            return true;
        }
        if (row == col) {
            if (board[0][0] == currentPlayer && board[1][1] == currentPlayer && board[2][2] == currentPlayer) {
                return true;
            }
        }
        if (row + col == 2) {
            if (board[0][2] == currentPlayer && board[1][1] == currentPlayer && board[2][0] == currentPlayer) {
                return true;
            }
        }

        return false;
    }

    public boolean makeMove(int position) {
        if (position < 1 || position > 9 || gameOver == true) {
            return false;
        }

        position--;
        int row = position / 3;
        int col = position % 3;

        if (board[row][col] == 'X' || board[row][col] == 'O') {
            return false;
        }

        board[row][col] = currentPlayer;
        turn_count++;

        if (checkWinner(row, col)) {
            winner = currentPlayer;
            gameOver = true;
        }
        else if (isDraw()) {
            gameOver = true;
        }
        else {
            switchPlayer();
        }

        return true;
    }

    public boolean isDraw() {
        return turn_count == 9 && winner == ' ';
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public char getWinner() {
        return winner;
    }

    public char getCurrentPlayer() {
        return currentPlayer;
    }

    public void switchPlayer() {
        if (currentPlayer == 'X') {
            currentPlayer = 'O';
        }
        else {
            currentPlayer = 'X';
        }
    }

    public String getBoardState() {
        StringBuilder payload = new StringBuilder();

        for (int r = 0; r < board.length; r++) {
            for (int c = 0; c < board[r].length; c++) {
                payload.append(board[r][c]);

                // only add comma if it's NOT the last element
                if (!(r == 2 && c == 2)) {
                    payload.append(",");
                }
            }
        }

        return payload.toString();
    }

}
