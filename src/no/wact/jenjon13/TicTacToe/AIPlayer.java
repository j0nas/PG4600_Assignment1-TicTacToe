package no.wact.jenjon13.TicTacToe;

/**
 * Abstract superclass for all AI players with different strategies.
 * To construct an AI player:
 * 1. Construct an instance (of its subclass) with the game Board
 * 2. Call setSign() to set the computer's seed
 * 3. Call move() which returns the next move in an int[2] array of {row, col}.
 * <p>
 * The implementation subclasses need to override abstract method move().
 * They shall not modify Cell[][], i.e., no side effect expected.
 * Assume that next move is available, i.e., not game-over yet.
 */
public abstract class AIPlayer {
    protected int ROWS = 3; // TODO get from Board -- also, make variable
    protected int COLS = 3;

    protected Cell[][] cells;
    protected Sign mySign;
    protected Sign oppSign;

    /**
     * Constructor with reference to game board
     */
    public AIPlayer(Board board, Sign sign) {
        cells = board.cells;
        setSign(sign);
    }

    public void setSign(Sign sign) {
        this.mySign = sign;
        oppSign = (mySign == Sign.CROSS) ? Sign.NOUGHT : Sign.CROSS;
    }

    /**
     * Abstract method to get next move. Return int[2] of {row, col}
     */
    public abstract int[] move();
}
