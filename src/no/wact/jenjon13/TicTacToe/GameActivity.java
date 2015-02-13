package no.wact.jenjon13.TicTacToe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class GameActivity extends Activity implements View.OnClickListener {
    private final int GRID_SIZE = 9;
    private final TileState[] xoPos = new TileState[GRID_SIZE];
    private boolean playingVsAI = false;
    private boolean crossTurn = true;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        setNonXOOnClickListeners();
        resetUi();

        playingVsAI = getIntent().getExtras() != null && getIntent().getExtras().getBoolean("vsCPU");
    }

    private void setNonXOOnClickListeners() {
        int[] buttons = {R.id.btnMainMenu, R.id.btnRematch};
        for (final int button : buttons) {
            findViewById(button).setOnClickListener(this);
        }
    }

    private void resetUi() {
        ((TextView) findViewById(R.id.txtDeclareWinner)).setText("");
        findViewById(R.id.btnRematch).setVisibility(View.GONE);
        crossTurn = true;

        for (int i = 1; i < 10; i++) {
            final ImageButton button = (ImageButton) findViewById(getResources().getIdentifier("imageButton" + i, "id", getPackageName()));
            button.setImageBitmap(null);
            button.setOnClickListener(this);
        }

        for (int i = 0; i < xoPos.length; i++) {
            xoPos[i] = TileState.NOT_SET;
        }
    }

    @Override
    public void onClick(final View v) {
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

        xoPos[getButtonNumberById(v.getId()) - 1] = (crossTurn ? TileState.CROSS : TileState.CIRCLE);
        pressedButton.setOnClickListener(null);
        crossTurn = !crossTurn;

        if (!checkForGameEndingEvents() && (playingVsAI && !crossTurn)) {
            cpuMove();
        }
    }

    private boolean checkForGameEndingEvents() {
        boolean someoneWon = false;
        TileState winingPlayer = getWinningPlayer();
        if (someoneWon = !winingPlayer.equals(TileState.NOT_SET)) {
            ((TextView) findViewById(R.id.txtDeclareWinner)).setText(winingPlayer.equals(TileState.CROSS) ? R.string.cross_won : R.string.circle_won);
        }

        final boolean tied = !someoneWon && checkTie();
        if (tied) {
            ((TextView) findViewById(R.id.txtDeclareWinner)).setText(R.string.tie);
        }

        if (tied || someoneWon) {
            findViewById(R.id.btnRematch).setVisibility(View.VISIBLE);
            for (int i = 0; i < GRID_SIZE; i++) {
                getButtonByNumber(i + 1).setOnClickListener(null);
            }
        }

        return tied || someoneWon;
    }

    private void cpuMove() {
        while (true) {
            final ImageButton buttonById = getButtonByNumber(1 + (int) (Math.random() * GRID_SIZE));
            if (buttonById.hasOnClickListeners()) {
                onClick(buttonById);
                return;
            }
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
        for (final TileState pos : xoPos) {
            if (pos == null || pos.equals(TileState.NOT_SET)) {
                return false;
            }
        }

        return true;
    }

    private TileState getWinningPlayer() { // TODO. make this method only return winner, not take care of anything else
        int[][] winPositions = {
                {1, 2, 3}, {1, 5, 9}, {1, 4, 7}, {4, 5, 6}, {7, 8, 9}, {2, 5, 8}, {3, 6, 9}, {3, 5, 7}
        };

        boolean somebodyWon = false;
        for (final int[] winPos : winPositions) {
            somebodyWon = xoPos[winPos[0] - 1] != null && xoPos[winPos[1] - 1] != null && xoPos[winPos[2] - 1] != null;
            somebodyWon = somebodyWon && xoPos[winPos[0] - 1].equals(xoPos[winPos[1] - 1]) && xoPos[winPos[1] - 1].equals(xoPos[winPos[2] - 1]);

            if (somebodyWon) {
                return xoPos[winPos[0] - 1];
            }
        }

        return TileState.NOT_SET;
    }
}