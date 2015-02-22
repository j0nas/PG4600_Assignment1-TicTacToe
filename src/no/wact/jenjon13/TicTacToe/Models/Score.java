package no.wact.jenjon13.TicTacToe.models;

import java.util.Date;

public class Score implements Comparable {
    private CharSequence timeStamp;
    private String winnerName;
    private String aiDifficulty;
    private int matchLength;

    public Score(String winnerName, int matchLength) {
        this(winnerName, null, matchLength);
    }

    public Score(String winnerName, String aiDifficulty, int matchLength) {
        this.winnerName = winnerName;
        this.aiDifficulty = aiDifficulty;
        this.matchLength = matchLength;

        timeStamp = android.text.format.DateFormat.format("dd.MM.yy HH:MM", new Date());
    }

    @Override
    public String toString() {
        return String.format("%s: %s - %dms" + (aiDifficulty != null ? " (vs CPU @ " + aiDifficulty + ")" + "" : ""),
                timeStamp, winnerName, matchLength);
    }

    @Override
    public int compareTo(Object another) {
        if (another == null || getClass() != another.getClass()) {
            return 1;
        }

        final int otherLen = ((Score) another).matchLength;
        return otherLen == this.matchLength ? 0 : otherLen > this.matchLength ? -1 : 1;
    }
}
