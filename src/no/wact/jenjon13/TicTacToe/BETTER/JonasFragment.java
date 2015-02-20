package no.wact.jenjon13.TicTacToe.BETTER;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import no.wact.jenjon13.TicTacToe.*;
import no.wact.jenjon13.TicTacToe.activities.MainMenuActivity;

public class JonasFragment extends Fragment implements View.OnClickListener {
    private int gridSize = 9;
    private boolean crossTurn = true;
    private String aiDifficulty = null;
    private Board board = new Board(3, 3);
    private MiniMaxAI miniMaxAI = new MiniMaxAI(board, crossTurn ? Sign.NOUGHT : Sign.CROSS);
    private View container;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {

        final View thisView = inflater.inflate(R.layout.boardfragment, container, false);
        this.container = thisView;

        gridSize = getResources().getInteger(R.integer.gridSize);
        miniMaxAI.setSign(Sign.NOUGHT);
        int i = 1;
        for (final Cell[] rows : board.cells) {
            for (Cell cell : rows) {
                cell.button = getButtonByNumber(i++);
            }
        }

        resetUi();

//        final TextView textView = (TextView) container.findViewById(R.id.gamePlayer1Text);
//        textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
//        textView.setText(getActivity().getSharedPreferences("TicTacToe_Preferences", getActivity().MODE_PRIVATE)
//                .getString("player1name", getResources().getString(R.id.txtPlayer1Name)));
//
//        ((TextView) container.findViewById(R.id.gamePlayer2Text)).setText(
//                getActivity().getSharedPreferences("TicTacToe_Preferences", getActivity().MODE_PRIVATE).getString("player2name",
//                        getResources().getString(R.id.txtPlayer1Name)));

        /*
        final Bundle arguments = getArguments();
        Log.e("onCreateView", String.valueOf(arguments == null));
        aiDifficulty = arguments.getString("ananas");
//        final Bundle extras = getActivity().getIntent().getExtras(); // TODO: get extras from container activity, not from external activity
//        if (extras != null) { // TODO somehow, magically, get the extra data originally passed by the intent, only.. not through an intent

        aiDifficulty = arguments.getString(getResources().getString(R.string.selectaidifficulty_difficulty));
        if (aiDifficulty != null) {
            Log.v("onCreate", "Playing with " + aiDifficulty + " difficulty.");
            ((TextView) container.findViewById(R.id.gamePlayer2Text)).setText("CPU"); // FIXME externalize to strings.xml
        }
*/
        return thisView;
    }

    private void setAllOnClickListeners() {
        final ViewGroup parentLayout = (ViewGroup) container.findViewById(R.id.layoutGameScreen);
        for (int i = 0; i < parentLayout.getChildCount(); i++) {
            parentLayout.getChildAt(i).setOnClickListener(this);
        }

        for (int i = 0; i < 9; i++) {
            board.getCellByNumber(i).button.setOnClickListener(this);
        }
    }

    private void resetUi() {
        setGameStatus(null);
        container.findViewById(R.id.btnRematch).setVisibility(View.GONE);
        crossTurn = true;

        final TextView player1txt = (TextView) container.findViewById(R.id.gamePlayer1Text);
        player1txt.setTypeface(Typeface.create(player1txt.getTypeface(), Typeface.BOLD));
        final TextView player2txt = (TextView) container.findViewById(R.id.gamePlayer2Text);
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
        final View foundView = container.findViewById(v.getId());
        if (foundView instanceof Button) {
            switch (v.getId()) {
                case R.id.btnRematch:
                    Log.v("onClick", "Clicked btnRematch");
                    resetUi();
                    return;
                case R.id.btnMainMenu:
                    Log.v("onClick", "Clicked btnMainMenu");
                    resetUi();
                    startActivity(new Intent(getActivity(), MainMenuActivity.class));
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

        final TextView lastPlayerTxt = (TextView) container.findViewById(crossTurn ? R.id.gamePlayer2Text : R.id.gamePlayer1Text);
        lastPlayerTxt.setTypeface(Typeface.create(lastPlayerTxt.getTypeface(), Typeface.NORMAL));

        final TextView curPlayerTxt = (TextView) container.findViewById(crossTurn ? R.id.gamePlayer1Text : R.id.gamePlayer2Text);
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
            container.findViewById(R.id.btnRematch).setVisibility(View.VISIBLE);
            board.disableAllButtons();
            return true;
        }

        return false;
    }

    private void setGameStatus(Sign winner) {
        final TextView txtDeclareWinner = (TextView) container.findViewById(R.id.txtDeclareWinner);
        if (winner == null) {
            txtDeclareWinner.setText("");
        } else if (winner == Sign.EMPTY) {
            txtDeclareWinner.setText(R.string.game_tie);
        } else {
            txtDeclareWinner.setText(winner == Sign.CROSS ?
                    getActivity().getSharedPreferences("TicTacToe_Preferences", getActivity().MODE_PRIVATE)
                            .getString("player1name", getResources().getString(R.id.txtPlayer1Name)) :
                    getActivity().getSharedPreferences("TicTacToe_Preferences", getActivity().MODE_PRIVATE)
                            .getString("player2name", getResources().getString(R.id.txtPlayer2Name)));
            txtDeclareWinner.append(" won!");
        }

    }

    private void cpuMove() {
        switch (aiDifficulty) {
            case "1": // getResources().getString(R.string.): FIXME
                while (true) {
                    final ImageButton button = getButtonByNumber(1 + (int) (Math.random() * gridSize));
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
        return (ImageButton) container.findViewById(getResources().getIdentifier("imageButton" + i, "id", getActivity().getPackageName()));
    }

    private int getButtonNumberById(final int id) {
        for (int i = 1; i < gridSize + 1; i++) {
            if (id == getResources().getIdentifier("imageButton" + i, "id", getActivity().getPackageName())) {
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
