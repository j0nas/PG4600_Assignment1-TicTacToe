package no.wact.jenjon13.TicTacToe;

public enum TileState {
    NOT_SET(-1),
    CROSS(R.string.cross_won),
    CIRCLE(R.string.circle_won);

    private final int stringId;

    TileState(final int stringId) {
        this.stringId = stringId;
    }

    public int getWinMessageId() {
        return stringId;
    }
}
