import java.util.*;

public class TicTacToe {
	
	static void printBoard(String[][] mat) {
		int x, y;
		
		for (x = 0; x < mat.length; x++) {
			System.out.println("-------------");
			System.out.print("| ");
			for (y = 0; y < mat.length; y++) {
				System.out.print(mat[x][y] + " | ");
			}
			System.out.println();
		}
		System.out.println("-------------");
	}
	
	static boolean check(int[] arr, int num) {
		int i;
		for (i = 0; i < arr.length; i++) {
			if (num == arr[i]) {
				arr[i] = 0;
				return true;
			}
		}
		return false;
	}
	
	static boolean checkWinner(String[][] mat, String player) {
		if (mat[0][0].equals(mat[0][1]) && mat[0][0].equals(mat[0][2])) {
			System.out.println(player + " Wins!");
			return true;
		}
		else if (mat[1][0].equals(mat[1][1]) && mat[1][0].equals(mat[1][2])) {
			System.out.println(player + " Wins!");
			return true;
		}
		else if (mat[2][0].equals(mat[2][1]) && mat[2][0].equals(mat[2][2])) {
			System.out.println(player + " Wins!");
			return true;
		}
		else if (mat[0][0].equals(mat[1][0]) && mat[1][0].equals(mat[2][0])) {
			System.out.println(player + " Wins!");
			return true;
		}
		else if (mat[0][1].equals(mat[1][1]) && mat[0][1].equals(mat[2][1])) {
			System.out.println(player + " Wins!");
			return true;
		}
		else if (mat[2][2].equals(mat[1][2]) && mat[2][2].equals(mat[0][2])) {
			System.out.println(player + " Wins!");
			return true;
		}
		else if (mat[0][0].equals(mat[1][1]) && mat[0][0].equals(mat[2][2])) {
			System.out.println(player + " Wins!");
			return true;
		}
		else if (mat[0][2].equals(mat[1][1]) && mat[0][2].equals(mat[2][0])) {
			System.out.println(player + " Wins!");
			return true;
		}
		
		return false;
	}
	
	static void modifyArray(int num, String turn, String[][] mat) {
		num--;
		if (num == 0) {
			mat[0][0] = turn;
		}
		else if (num == 1) {
			mat[0][1] = turn;
		}
		else if (num == 2) {
			mat[0][2] = turn;
		}
		else if (num == 3) {
			mat[1][0] = turn;
		}
		else if (num == 4) {
			mat[1][1] = turn;
		}
		else if (num == 5) {
			mat[1][2] = turn;
		}
		else if (num == 6) {
			mat[2][0] = turn;
		}
		else if (num == 7) {
			mat[2][1] = turn;
		}
		else if (num == 8) {
			mat[2][2] = turn;
		}
	}
	
	public static void main(String[] args) {
		System.out.println("Welcome to TicTacToe");
		System.out.println("--------------------\n");
		
		String[][] gameBoard = {{"1", "2", "3"}, 
							    {"4", "5", "6"},
							    {"7", "8", "9"}};
		
		int[] possibleNum = {1, 2, 3, 4, 5, 6, 7, 8, 9};
		Scanner myScanner = new Scanner(System.in);
		Random rand = new Random();
		int turnCounter = 1;
		boolean leave = false;
		
		printBoard(gameBoard);
		while (leave == false) {			
			if (turnCounter % 2 == 1) {
				System.out.print("Choose a number for your choice: ");
				int choice = myScanner.nextInt();
				if (check(possibleNum, choice) == true) {
					modifyArray(choice, "X", gameBoard);
					leave = checkWinner(gameBoard, "X");
				}
				else {
					System.out.println("Invalid Choice");
					System.out.println();
					continue;
				}
			}
			else {
				while (true) {
					int compChoice = rand.nextInt(10);
					if (check(possibleNum, compChoice) == true) {
						System.out.print("CPU choice: " + compChoice);
						System.out.println();
						modifyArray(compChoice, "O", gameBoard);
						leave = checkWinner(gameBoard, "O");
						break;
					}
				}
			}
			System.out.println();
			printBoard(gameBoard);
			turnCounter++;
		}
		
	}
}

