package no.wact.jenjon13.TicTacToe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class GameActivity extends Activity implements View.OnClickListener {
    private final int GRID_SIZE = 9;
    private boolean crossTurn = true;

    private boolean playingVsAI = false;
    private int aiDifficulty = -1;
    private Board board = new Board(3, 3);
    private MiniMaxAI miniMaxAI = new MiniMaxAI(board);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gamemainscreen);

        miniMaxAI.setSeed(Seed.NOUGHT);
        int i = 1;
        for (final Cell[] rows : board.cells) {
            for (Cell cell : rows) {
                cell.button = getButtonByNumber(i++);
            }
        }

        resetUi();

        if (getIntent().getExtras() != null) {
            aiDifficulty = getIntent().getExtras().getInt(SelectAIDifficultyActivity.difficultyExtraName);
            playingVsAI = aiDifficulty > 0;
        }

        Log.v("onCreate", "Playing with " + aiDifficulty + " difficulty.");
    }

    private void setAllOnClickListeners() {
        final ViewGroup parentLayout = (ViewGroup) findViewById(R.id.layoutGameScreen);
        for (int i = 0; i < parentLayout.getChildCount(); i++) {
            parentLayout.getChildAt(i).setOnClickListener(this);
        }

        for (int i = 0; i < 9; i++) {
            board.getCellByNumber(i).button.setOnClickListener(this);
        }
    }

    private void resetUi() {
        setGameStatus(null);
        findViewById(R.id.btnRematch).setVisibility(View.GONE);
        crossTurn = true;

        for (final Cell[] rows : board.cells) {
            for (final Cell cell : rows) {
                cell.button.setImageBitmap(null);
                cell.content = Seed.EMPTY;
            }
        }

        setAllOnClickListeners();
    }

    @Override
    public void onClick(final View v) {
        Log.v("onClick", "onClick called.");
        final View foundView = findViewById(v.getId());
        if (foundView instanceof Button) {
            switch (v.getId()) {
                case R.id.btnRematch:
                    Log.v("onClick", "Clicked btnRematch");
                    resetUi();
                    return;
                case R.id.btnMainMenu:
                    Log.v("onClick", "Clicked btnMainMenu");
                    resetUi();
                    startActivity(new Intent(GameActivity.this, MainMenuActivity.class));
                    return;
            }
        }

        if (!(foundView instanceof ImageButton)) {
            Log.e("onClick", "View with id " + v.getId() + " not handled!");
            return;
        }

        final ImageButton pressedButton = (ImageButton) foundView;
        pressedButton.setImageBitmap(Utilities.decodeSampledBitmapFromResource(getResources(),
                crossTurn ? R.drawable.cross : R.drawable.circle, 15, 15));

        board.getCellByNumber(getButtonNumberById(v.getId()) - 1).content = (crossTurn ? Seed.CROSS : Seed.NOUGHT);
        pressedButton.setOnClickListener(null);
        crossTurn = !crossTurn;

        if (!checkForGameEndingEvents() && (playingVsAI && !crossTurn)) {
            cpuMove();
        }
    }

    private boolean checkForGameEndingEvents() {
        final Seed winner = miniMaxAI.hasWon(Seed.CROSS) ? Seed.CROSS : (miniMaxAI.hasWon(Seed.NOUGHT) ? Seed.NOUGHT : Seed.EMPTY);
        final boolean tied = (winner == Seed.EMPTY) && checkTie();

        if (tied || winner != Seed.EMPTY) {
            setGameStatus(winner);
            findViewById(R.id.btnRematch).setVisibility(View.VISIBLE);
            board.disableAllButtons();
            return true;
        }

        return false;
    }

    private void setGameStatus(Seed winner) {
        final TextView txtDeclareWinner = (TextView) findViewById(R.id.txtDeclareWinner);
        if (winner == null) {
            txtDeclareWinner.setText("");
        } else {
            txtDeclareWinner.setText(winner == Seed.EMPTY ? R.string.tie : winner == Seed.CROSS ? R.string.cross_won : R.string.circle_won);
        }
    }

    private void cpuMove() {
        switch (aiDifficulty) {
            case SelectAIDifficultyActivity.DIFFICULTY_EASY:
                while (true) {
                    final ImageButton buttonById = getButtonByNumber(1 + (int) (Math.random() * GRID_SIZE));
                    if (buttonById.hasOnClickListeners()) {
                        onClick(buttonById);
                        return;
                    }
                }

            case SelectAIDifficultyActivity.DIFFICULTY_MEDIUM: // FIXME
            case SelectAIDifficultyActivity.DIFFICULTY_HARD:
                final int[] move = miniMaxAI.move();
                onClick(board.cells[move[0]][move[1]].button);
        }
    }

    private ImageButton getButtonByNumber(final int i) {
        return (ImageButton) findViewById(getResources().getIdentifier("imageButton" + i, "id", getPackageName()));
    }

    private int getButtonNumberById(final int id) {
        for (int i = 1; i < GRID_SIZE + 1; i++) {
            if (id == getResources().getIdentifier("imageButton" + i, "id", getPackageName())) {
                return i;
            }
        }

        Log.e("getButtonByNumber", "Didn't find id: " + id);
        return -1;
    }

    private boolean checkTie() {
        for (int i = 0; i < board.cells.length; i++) {
            for (int j = 0; j < board.cells[i].length; j++) {
                if (board.cells[i][j].content == Seed.EMPTY) {
                    return false;
                }
            }
        }

        return true;
    }

//    private TileState getWinningPlayer() { // TODO. make this method only return winner, not take care of anything else
//        int[][] winPositions = {
//                {1, 2, 3}, {1, 5, 9}, {1, 4, 7}, {4, 5, 6}, {7, 8, 9}, {2, 5, 8}, {3, 6, 9}, {3, 5, 7}
//        };
//
//        boolean somebodyWon = false;
//        for (final int[] winPos : winPositions) {
//            somebodyWon = xoPos[winPos[0] - 1] != null && xoPos[winPos[1] - 1] != null && xoPos[winPos[2] - 1] != null;
//            somebodyWon = somebodyWon && xoPos[winPos[0] - 1].equals(xoPos[winPos[1] - 1]) && xoPos[winPos[1] - 1].equals(xoPos[winPos[2] - 1]);
//
//            if (somebodyWon) {
//                return xoPos[winPos[0] - 1];
//            }
//        }
//
//        return TileState.NOT_SET;
//    }
}