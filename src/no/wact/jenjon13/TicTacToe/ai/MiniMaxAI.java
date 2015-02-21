package no.wact.jenjon13.TicTacToe.ai;

import no.wact.jenjon13.TicTacToe.abstracts.AIPlayer;
import no.wact.jenjon13.TicTacToe.models.Board;
import no.wact.jenjon13.TicTacToe.models.Sign;

import java.util.ArrayList;
import java.util.List;

public class MiniMaxAI extends AIPlayer {
    /**
     * AIPlayer using Minimax algorithm
     */
    private int[] winningPatterns = {
            0b111000000, 0b000111000, 0b000000111, // rows
            0b100100100, 0b010010010, 0b001001001, // cols
            0b100010001, 0b001010100               // diagonals
    };

    /**
     * Constructor with the given game board
     */
    public MiniMaxAI(Board board, Sign sign) {
        super(board, sign);
    }

    /**
     * Get next best move for computer. Return int[2] of {row, col}
     */
    @Override
    public int[] move() {
        int[] result = minimax(2, mySign, Integer.MIN_VALUE, Integer.MAX_VALUE);
        return new int[]{result[1], result[2]};
    }

    /**
     * Recursive minimax at level of depth for either maximizing or minimizing player.
     * Return int[3] of {score, row, col}
     */
    private int[] minimax(int depth, Sign player, int alpha, int beta) {
        // Generate possible next moves in a list of int[2] of {row, col}.
        List<int[]> nextMoves = generateMoves();

        // mySign is maximizing; while oppSign is minimizing
        int score, bestRow = -1, bestCol = -1;

        if (nextMoves.isEmpty() || depth == 0) {
            // Gameover or depth reached, evaluate score
            score = evaluate();
            return new int[]{score, bestRow, bestCol};
        } else {
            for (int[] move : nextMoves) {
                // try this move for the current "player"
                cells[move[0]][move[1]].content = player;
                if (player == mySign) {  // mySign (computer) is maximizing player
                    score = minimax(depth - 1, oppSign, alpha, beta)[0];
                    if (score > alpha) {
                        alpha = score;
                        bestRow = move[0];
                        bestCol = move[1];
                    }
                } else {  // oppSign is minimizing player
                    score = minimax(depth - 1, mySign, alpha, beta)[0];
                    if (score < beta) {
                        beta = score;
                        bestRow = move[0];
                        bestCol = move[1];
                    }
                }
                // undo move
                cells[move[0]][move[1]].content = Sign.EMPTY;
                // cut-off
                if (alpha >= beta) {
                    break;
                }
            }
            return new int[]{(player == mySign) ? alpha : beta, bestRow, bestCol};
        }
    }

    /**
     * Find all valid next moves.
     * Return List of moves in int[2] of {row, col} or empty list if gameover
     */
    private List<int[]> generateMoves() {
        List<int[]> nextMoves = new ArrayList<int[]>(); // allocate List

        // If gameover, i.e., no next move
        if (hasWon(mySign) || hasWon(oppSign)) {
            return nextMoves;   // return empty list
        }

        // Search for empty cells and add to the List
        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                if (cells[row][col].content == Sign.EMPTY) {
                    nextMoves.add(new int[]{row, col});
                }
            }
        }

        return nextMoves;
    }

    /**
     * The heuristic evaluation function for the current board
     *
     * @return +100, +10, +1 for EACH 3-, 2-, 1-in-a-line for computer.
     * -100, -10, -1 for EACH 3-, 2-, 1-in-a-line for opponent.
     * 0 otherwise
     */
    private int evaluate() {
        int score = 0;
        // Evaluate score for each of the 8 lines (3 rows, 3 columns, 2 diagonals)
        score += evaluateLine(0, 0, 0, 1, 0, 2);  // row 0
        score += evaluateLine(1, 0, 1, 1, 1, 2);  // row 1
        score += evaluateLine(2, 0, 2, 1, 2, 2);  // row 2
        score += evaluateLine(0, 0, 1, 0, 2, 0);  // col 0
        score += evaluateLine(0, 1, 1, 1, 2, 1);  // col 1
        score += evaluateLine(0, 2, 1, 2, 2, 2);  // col 2
        score += evaluateLine(0, 0, 1, 1, 2, 2);  // diagonal
        score += evaluateLine(0, 2, 1, 1, 2, 0);  // alternate diagonal
        return score;
    }

    /**
     * The heuristic evaluation function for the given line of 3 cells
     *
     * @return +100, +10, +1 for 3-, 2-, 1-in-a-line for computer.
     * -100, -10, -1 for 3-, 2-, 1-in-a-line for opponent.
     * 0 otherwise
     */
    private int evaluateLine(int row1, int col1, int row2, int col2, int row3, int col3) {
        int score = 0;

        // First cell
        if (cells[row1][col1].content == mySign) {
            score = 1;
        } else if (cells[row1][col1].content == oppSign) {
            score = -1;
        }

        // Second cell
        if (cells[row2][col2].content == mySign) {
            if (score == 1) {   // cell1 is mySign
                score = 10;
            } else if (score == -1) {  // cell1 is oppSign
                return 0;
            } else {  // cell1 is empty
                score = 1;
            }
        } else if (cells[row2][col2].content == oppSign) {
            if (score == -1) { // cell1 is oppSign
                score = -10;
            } else if (score == 1) { // cell1 is mySign
                return 0;
            } else {  // cell1 is empty
                score = -1;
            }
        }

        // Third cell
        if (cells[row3][col3].content == mySign) {
            if (score > 0) {  // cell1 and/or cell2 is mySign
                score *= 10;
            } else if (score < 0) {  // cell1 and/or cell2 is oppSign
                return 0;
            } else {  // cell1 and cell2 are empty
                score = 1;
            }
        } else if (cells[row3][col3].content == oppSign) {
            if (score < 0) {  // cell1 and/or cell2 is oppSign
                score *= 10;
            } else if (score > 1) {  // cell1 and/or cell2 is mySign
                return 0;
            } else {  // cell1 and cell2 are empty
                score = -1;
            }
        }
        return score;
    }

    /**
     * Returns true if thePlayer wins
     */
    public boolean hasWon(Sign thePlayer) {
        int pattern = 0b000000000;  // 9-bit pattern for the 9 cells
        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                if (cells[row][col].content == thePlayer) {
                    pattern |= (1 << (row * COLS + col));
                }
            }
        }

        for (int winningPattern : winningPatterns) {
            if ((pattern & winningPattern) == winningPattern) {
                return true;
            }
        }

        return false;
    }
}
