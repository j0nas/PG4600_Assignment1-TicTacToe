package no.wact.jenjon13.TicTacToe;

public class Board {

    public Cell[][] cells;

    public Board(final int gridWidth, final int gridHeight) {
        cells = new Cell[gridWidth][gridHeight];

        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[i].length; j++) {
                cells[i][j] = new Cell();
                cells[i][j].content = Sign.EMPTY;
            }
        }
    }

    public Cell getCellByNumber(int number) {
        return cells[number / cells.length][number % cells.length];
    }

    public void disableAllButtons() {
        for (final Cell[] cell : cells) {
            for (final Cell aCell : cell) {
                aCell.button.setOnClickListener(null);
            }
        }
    }
}
