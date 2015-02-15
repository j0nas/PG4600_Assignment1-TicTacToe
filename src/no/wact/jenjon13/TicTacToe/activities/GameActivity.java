package no.wact.jenjon13.TicTacToe.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import no.wact.jenjon13.TicTacToe.*;

public class GameActivity extends Activity implements View.OnClickListener {
    private final int GRID_SIZE = 9;
    private boolean crossTurn = true;
    private String aiDifficulty = null;
    private Board board = new Board(3, 3);
    private MiniMaxAI miniMaxAI = new MiniMaxAI(board, crossTurn ? Sign.NOUGHT : Sign.CROSS);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);

        miniMaxAI.setSign(Sign.NOUGHT);
        int i = 1;
        for (final Cell[] rows : board.cells) {
            for (Cell cell : rows) {
                cell.button = getButtonByNumber(i++);
            }
        }

        resetUi();

        final TextView textView = (TextView) findViewById(R.id.gamePlayer1Text);
        textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
        textView.setText(getSharedPreferences("TicTacToe_Preferences", MODE_PRIVATE)
                .getString("player1name", getResources().getString(R.id.txtPlayer1Name)));

        ((TextView) findViewById(R.id.gamePlayer2Text)).setText(
                getSharedPreferences("TicTacToe_Preferences", MODE_PRIVATE).getString("player2name",
                        getResources().getString(R.id.txtPlayer1Name)));

        final Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if ((aiDifficulty = extras.getString(getResources().getString(R.string.selectaidifficulty_difficulty))) != null) {
                Log.v("onCreate", "Playing with " + aiDifficulty + " difficulty.");
                ((TextView) findViewById(R.id.gamePlayer2Text)).setText("CPU"); // FIXME externalize to strings.xml
            }
        }
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

        final TextView player1txt = (TextView) findViewById(R.id.gamePlayer1Text);
        player1txt.setTypeface(Typeface.create(player1txt.getTypeface(), Typeface.BOLD));
        final TextView player2txt = (TextView) findViewById(R.id.gamePlayer2Text);
        player2txt.setTypeface(Typeface.create(player2txt.getTypeface(), Typeface.NORMAL));

        for (final Cell[] rows : board.cells) {
            for (final Cell cell : rows) {
                cell.button.setImageBitmap(null);
                cell.content = Sign.EMPTY;
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

        board.getCellByNumber(getButtonNumberById(v.getId()) - 1).content = (crossTurn ? Sign.CROSS : Sign.NOUGHT);
        pressedButton.setOnClickListener(null);
        crossTurn = !crossTurn;

        final TextView lastPlayerTxt = (TextView) findViewById(crossTurn ? R.id.gamePlayer2Text : R.id.gamePlayer1Text);
        lastPlayerTxt.setTypeface(Typeface.create(lastPlayerTxt.getTypeface(), Typeface.NORMAL));

        final TextView curPlayerTxt = (TextView) findViewById(crossTurn ? R.id.gamePlayer1Text : R.id.gamePlayer2Text);
        curPlayerTxt.setTypeface(Typeface.create(lastPlayerTxt.getTypeface(), Typeface.BOLD));

        if (!checkForGameEndingEvents() && (aiDifficulty != null && !crossTurn)) {
            cpuMove();
        }
    }

    private boolean checkForGameEndingEvents() {
        final Sign winner = miniMaxAI.hasWon(Sign.CROSS) ? Sign.CROSS : (miniMaxAI.hasWon(Sign.NOUGHT) ? Sign.NOUGHT : Sign.EMPTY);
        final boolean tied = (winner == Sign.EMPTY) && checkTie();

        if (tied || winner != Sign.EMPTY) {
            setGameStatus(winner);
            findViewById(R.id.btnRematch).setVisibility(View.VISIBLE);
            board.disableAllButtons();
            return true;
        }

        return false;
    }

    private void setGameStatus(Sign winner) {
        final TextView txtDeclareWinner = (TextView) findViewById(R.id.txtDeclareWinner);
        if (winner == null) {
            txtDeclareWinner.setText("");
        } else if (winner == Sign.EMPTY) {
            txtDeclareWinner.setText(R.string.game_tie);
        } else {
            txtDeclareWinner.setText(winner == Sign.CROSS ?
                    getSharedPreferences("TicTacToe_Preferences", MODE_PRIVATE)
                            .getString("player1name", getResources().getString(R.id.txtPlayer1Name)) :
                    getSharedPreferences("TicTacToe_Preferences", MODE_PRIVATE)
                            .getString("player2name", getResources().getString(R.id.txtPlayer2Name)));
            txtDeclareWinner.append(" won!");
        }

    }

    private void cpuMove() {
        switch (aiDifficulty) {
            case "1": // getResources().getString(R.string.): FIXME
                while (true) {
                    final ImageButton button = getButtonByNumber(1 + (int) (Math.random() * GRID_SIZE));
                    if (button.hasOnClickListeners()) {
                        onClick(button);
                        return;
                    }
                }

            case "2": // FIXMESelectAIDifficultyActivity.DIFFICULTY_MEDIUM: FIXME
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
                if (board.cells[i][j].content == Sign.EMPTY) {
                    return false;
                }
            }
        }

        return true;
    }
}