package no.wact.jenjon13.TicTacToe.ai;

import no.wact.jenjon13.TicTacToe.models.Board;
import no.wact.jenjon13.TicTacToe.models.Sign;

import java.util.ArrayList;
import java.util.List;

/**
 * This class and its contained functionaliy is heavily based on the following sources:
 * - Nanyang Technological University: Java Graphics Tutorial - Case Study on Tic-Tac-Toe Part 2: With AI
 * > https://www3.ntu.edu.sg/home/ehchua/programming/java/JavaGame_TicTacToe_AI.html last visited 22.02.2015
 * - Wikipedia: Minimax
 * > http://en.wikipedia.org/wiki/Minimax last visited @ 22.02.2015
 * <p>
 * Please refer to the documentation for further notes.
 */
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
     * Constructor for the class.
     *
     * @param board The Board which the AI will refer to.
     * @param sign  The Sign which the AI will play.
     */
    public MiniMaxAI(Board board, Sign sign) {
        super(board, sign);
    }

    /**
     * Finds the best available move for the AI.
     *
     * @return Returns int[2] of {row, column} for the best move
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
        List<int[]> nextMoves = generateMoves();

        int score, bestRow = -1, bestCol = -1;

        if (nextMoves.isEmpty() || depth == 0) {
            score = evaluate();
            return new int[]{score, bestRow, bestCol};
        } else {
            for (int[] move : nextMoves) {
                cells[move[0]][move[1]].content = player;
                if (player == mySign) {
                    score = minimax(depth - 1, oppSign, alpha, beta)[0];
                    if (score > alpha) {
                        alpha = score;
                        bestRow = move[0];
                        bestCol = move[1];
                    }
                } else {
                    score = minimax(depth - 1, mySign, alpha, beta)[0];
                    if (score < beta) {
                        beta = score;
                        bestRow = move[0];
                        bestCol = move[1];
                    }
                }

                cells[move[0]][move[1]].content = Sign.EMPTY;
                if (alpha >= beta) {
                    break;
                }
            }

            return new int[]{(player == mySign) ? alpha : beta, bestRow, bestCol};
        }
    }

    /**
     * Find all valid next moves.
     *
     * @return List of moves in int[2] of {row, col} or empty list if gameover
     */
    private List<int[]> generateMoves() {
        List<int[]> nextMoves = new ArrayList<int[]>();
        if (hasWon(mySign) || hasWon(oppSign)) {
            return nextMoves;
        }

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
     * 0 otherwise.
     */
    private int evaluate() {
        int score = evaluateLine(0, 0, 0, 1, 0, 2);  // row 0
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

        if (cells[row1][col1].content == mySign) {
            score = 1;
        } else if (cells[row1][col1].content == oppSign) {
            score = -1;
        }

        if (cells[row2][col2].content == mySign) {
            if (score == 1) {
                score = 10;
            } else if (score == -1) {
                return 0;
            } else {
                score = 1;
            }
        } else if (cells[row2][col2].content == oppSign) {
            if (score == -1) {
                score = -10;
            } else if (score == 1) {
                return 0;
            } else {
                score = -1;
            }
        }

        if (cells[row3][col3].content == mySign) {
            if (score > 0) {
                score *= 10;
            } else if (score < 0) {
                return 0;
            } else {
                score = 1;
            }
        } else if (cells[row3][col3].content == oppSign) {
            if (score < 0) {
                score *= 10;
            } else if (score > 1) {
                return 0;
            } else {
                score = -1;
            }
        }

        return score;
    }

    /**
     * Checks if the given Sign has won.
     *
     * @param sign The Sign to check win for.
     * @return True if a win was detected for the Sign, false otherwise.
     */
    public boolean hasWon(Sign sign) {
        int pattern = 0b000000000;
        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                if (cells[row][col].content == sign) {
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
