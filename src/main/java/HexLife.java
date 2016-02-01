/**
 * Created by feeling on 1/10/16.
 */

import com.beust.jcommander.*;

import java.io.BufferedReader;
import java.io.FileReader;

public class HexLife {
    @Parameter(names = "-12", description = "Activate 12-neighbor mode")
    private static boolean neighbor12 = false;

    @Parameter(names = "-size", description = "Size of board")
    private static int dimension = 100;

    @Parameter(names = "-f", description = "Load the initial configuration from the specified file")
    private static String fileName;

    @Parameter(names = "-g", description = "Number of generations to simulate (default is 10)")
    private static int numOfGeneration = 10;

    @Parameter(names = "-p", description = "Every nth generation would be printed (default is 1)")
    private static int printGap = 1;

    @Parameter(names = "-i", description = "Probability of a cell being alive in the initial configuration if no file is provided (default is .5)")
    private static double probability = 0.5;

    private static char[][] board;

    private void nextGeneration() {
        char[][] newBoard = new char[dimension][dimension];
        copyMatrix(board, newBoard);

        if (!neighbor12) {
            for (int i = 0; i < dimension; i++) {
                for (int j = 0; j < dimension; j++) {
                    int aliveNeighbor = getAliveNeighbors6(i, j);

                    if (aliveNeighbor < 2 || aliveNeighbor > 3) {
                        newBoard[i][j] = '.';
                    } else if (aliveNeighbor == 3) {
                        newBoard[i][j] = 'x';
                    }
                }
            }
        } else {
            for (int i = 0; i < dimension; i++) {
                for (int j = 0; j < dimension; j++) {
                    double aliveNeighbor = getAliveNeighbors12(i, j);

                    if (aliveNeighbor < 2.0 || aliveNeighbor > 3.3) {
                        newBoard[i][j] = '.';
                    } else if (2.3 < aliveNeighbor && aliveNeighbor < 2.9) {
                        newBoard[i][j] = 'x';
                    }
                }
            }
        }

        copyMatrix(newBoard, board);
    }

    /**
     * Copy the elements in "original" matrix to "copy".
     *
     * @param original
     * @param copy
     */
    private void copyMatrix(char[][] original, char[][] copy) {
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                copy[i][j] = original[i][j];
            }
        }
    }

    /**
     * Count the number of neighbors in 6-neighbor mode.
     *
     * In even row, the neighbors of (row, col) are those cells centering at (row, col)
     * except (row - 1, col + 1) and (row + 1, col + 1).
     *
     * In odd row, the neighbors of (row, col) are those cells centering at (row, col)
     * except (row - 1, col - 1) and (row + 1, col - 1).
     *
     * @param row
     * @param col
     * @return
     */
    private int getAliveNeighbors6(int row, int col) {
        int count = 0;

        for (int i = row - 1; i <= row + 1; i++) {
            for (int j = col - 1; j <= col + 1; j++) {
                if (i < 0 || i > dimension - 1 || j < 0 || j > dimension - 1 || (i == row && j == col)) continue;

                if (row % 2 == 0) {
                    if (!(i == row - 1 && j == col + 1) && !(i == row + 1 && j == col + 1) && board[i][j] == 'x') {
                        count++;
                    }
                } else {
                    if (!(i == row - 1 && j == col - 1) && !(i == row + 1 && j == col - 1) && board[i][j] == 'x') {
                        count++;
                    }
                }
            }
        }

        return count;
    }

    /**
     * Count the number of neighbors in 12-neighbor mode.
     *
     * For 1st tier neighbors:
     * In even row, the neighbors of (row, col) are those cells centering at (row, col)
     * except (row - 1, col + 1) and (row + 1, col + 1).
     *
     * In odd row, the neighbors of (row, col) are those cells centering at (row, col)
     * except (row - 1, col - 1) and (row + 1, col - 1).
     *
     * For 2nd tier neighbors:
     * In even row, the neighbors of (row, col) are (row - 2, col), (row + 2, col),
     * (row - 1, col - 2), (row - 1, col + 1), (row + 1, col - 2), (row + 1, col + 1).
     *
     * In odd row, the neighbors of (row, col) are (row - 2, col), (row + 2, col),
     * (row - 1, col - 1), (row - 1, col + 2), (row + 1, col - 1), (row + 1, col + 2).
     *
     * @param row
     * @param col
     * @return
     */
    private double getAliveNeighbors12(int row, int col) {
        int count1 = 0;
        int count2 = 0;

        // Count 1st tier neighbors
        for (int i = row - 1; i <= row + 1; i++) {
            for (int j = col - 1; j <= col + 1; j++) {
                if (i < 0 || i > dimension - 1 || j < 0 || j > dimension - 1 || (i == row && j == col)) continue;

                if (row % 2 == 0) {
                    if (!(i == row - 1 && j == col + 1) && !(i == row + 1 && j == col + 1) && board[i][j] == 'x') {
                        count1++;
                    }
                } else {
                    if (!(i == row - 1 && j == col - 1) && !(i == row + 1 && j == col - 1) && board[i][j] == 'x') {
                        count1++;
                    }
                }
            }
        }

        // Count 2nd tier neighbors
        for (int i = row - 2; i <= row + 2; i++) {
            for (int j = col - 2; j <= col + 2; j++) {
                if (i < 0 || i > dimension - 1 || j < 0 || j > dimension - 1 || (i == row && j == col)) continue;

                if (row % 2 == 0) {
                    if (((i == row - 2 && j == col) || (i == row + 2 && j == col) || (i == row - 1 && j == col - 2) || (i == row - 1 && j == col + 1) || (i == row + 1 && j == col - 2) || (i == row + 1 && j == col + 1)) && board[i][j] == 'x') {
                        count2++;
                    }
                } else {
                    if (((i == row - 2 && j == col) || (i == row + 2 && j == col) || (i == row - 1 && j == col - 1) || (i == row - 1 && j == col + 2) || (i == row + 1 && j == col - 1) || (i == row + 1 && j == col + 2)) && board[i][j] == 'x') {
                        count2++;
                    }
                }
            }
        }

        return count1 + count2 * 0.3;
    }

    // When there is no file loaded, generate a board randomly according to probability
    private void initBoard() {
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                if (Math.random() < probability) {
                    board[i][j] = 'x';
                } else {
                    board[i][j] = '.';
                }
            }
        }
    }

    private void printBoard() {
        for (int i = 0; i < dimension; i++) {
            if (i % 2 != 0) System.out.print(" ");

            for (int j = 0; j < dimension; j++) {
                System.out.print(board[i][j] + " ");
            }

            System.out.println();
        }
    }

    // Load the initial board from the specified file
    private void loadBoard() {
        BufferedReader br;

        try {
            br = new BufferedReader(new FileReader(fileName));
            String line;

            int i = 0;
            while ((line = br.readLine()) != null) {
                char[] arr = line.toCharArray();
                if (arr.length < dimension) {
                    throw new Exception("The board doesn't have enough columns in the specified file. Please use a larger board.");
                }

                for (int j = 0; j < dimension; j++) {
                    board[i][j] = arr[j];
                }

                i++;
                if (i == dimension) break;
            }

            if (i < dimension) {
                throw new Exception("The board doesn't have enough rows in the specified file. Please use a larger board.");
            }

            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        System.out.println("Welcome to HexLife!\n");

        HexLife hl = new HexLife();
        new JCommander(hl, args);

        board = new char[dimension][dimension];

        if (fileName == null) {
            hl.initBoard();

            System.out.println("The randomly generated board:");
            hl.printBoard();
            System.out.println();
        } else {
            hl.loadBoard();

            System.out.println("The loaded board:");
            hl.printBoard();
            System.out.println();
        }

        for (int num = 1; num <= numOfGeneration; num++) {
            hl.nextGeneration();

            if (num % printGap == 0) {
                if (num == 1) {
                    System.out.println("The 1st generation:");
                } else if (num == 2) {
                    System.out.println("The 2nd generation:");
                } else if (num == 3) {
                    System.out.println("The 3rd generation:");
                } else {
                    System.out.println("The " + num + "th generation:");
                }

                hl.printBoard();
                System.out.println();
            }
        }
    }
}