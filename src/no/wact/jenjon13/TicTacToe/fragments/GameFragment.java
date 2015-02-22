package no.wact.jenjon13.TicTacToe.fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import no.wact.jenjon13.TicTacToe.R;
import no.wact.jenjon13.TicTacToe.activities.GameHistoryActivity;
import no.wact.jenjon13.TicTacToe.ai.MiniMaxAI;
import no.wact.jenjon13.TicTacToe.configs.Config;
import no.wact.jenjon13.TicTacToe.models.Board;
import no.wact.jenjon13.TicTacToe.models.Cell;
import no.wact.jenjon13.TicTacToe.models.Score;
import no.wact.jenjon13.TicTacToe.models.Sign;
import no.wact.jenjon13.TicTacToe.utilities.ImgUtils;

import java.util.Iterator;
import java.util.TreeSet;

public class GameFragment extends Fragment implements View.OnClickListener {
    private static final TreeSet<Score> scores = new TreeSet<>();
    private boolean crossTurn = true;
    private String aiDifficulty = null;
    private Board board = new Board(Config.GRID_WIDTH, Config.GRID_HEIGHT);
    private MiniMaxAI miniMaxAI = new MiniMaxAI(board, crossTurn ? Sign.NOUGHT : Sign.CROSS);
    private View containView;
    private long roundTime;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle
            savedInstanceState) {
        final View thisView = inflater.inflate(R.layout.boardfragment, container, false);
        containView = thisView;

        int i = 1;
        for (final Cell[] rows : board.cells) {
            for (Cell cell : rows) {
                cell.button = getButtonByNumber(i++);
            }
        }

        resetUi();

        // Parse all highscores and add them to the scores member.
        final SharedPreferences highscorePrefs = getActivity()
                .getSharedPreferences(getActivity().getResources()
                        .getString(R.string.highscores), Context.MODE_PRIVATE);

        for (int j = 0; j < 5; j++) {
            final String highscore = highscorePrefs.getString("highscore_" + j, null);
            if (highscore != null) {
                Log.v("onCreateView", "Loaded highscore: " + highscore);
                final String[] split = highscore.split(" ");
                scores.add(new Score(split[2], split.length > 8 ? split[8].charAt(0) + "" : null,
                        Integer.valueOf(split[4].substring(0, split[4].length() - 2))));
            }
        }

        final SharedPreferences sharedPrefs =
                getActivity()
                        .getSharedPreferences(getResources()
                                .getString(R.string.sharedPrefs), getActivity().MODE_PRIVATE);

        final TextView textView = (TextView) containView.findViewById(R.id.gamePlayer1Text);
        textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
        textView.setText(sharedPrefs.getString(getResources().getString(R.string.player1name),
                getResources().getString(R.string.game_player1)));

        ((TextView) containView.findViewById(R.id.gamePlayer2Text))
                .setText(sharedPrefs.getString(getResources().getString(R.string.player2name),
                        getResources().getString(R.string.game_player2)));

        final Bundle arguments = getArguments();
        if (arguments != null) {
            aiDifficulty = arguments.getString(getResources().getString(R.string.difficulty));
        }

        if (aiDifficulty != null) {
            Log.v("onCreate", "Playing with " + aiDifficulty + " difficulty.");
            ((TextView) containView.findViewById(R.id.gamePlayer2Text))
                    .setText(getResources().getString(R.string.vsCPU));
        }

        return thisView;
    }

    private void setAllOnClickListeners() {
        final ViewGroup parentLayout = (ViewGroup) containView.findViewById(R.id.layoutGameScreen);
        for (int i = 0; i < parentLayout.getChildCount(); i++) {
            parentLayout.getChildAt(i).setOnClickListener(this);
        }

        for (int i = 0; i < 9; i++) {
            board.getCellByNumber(i).button.setOnClickListener(this);
        }
    }

    private void resetUi() {
        setGameStatus(null);
        containView.findViewById(R.id.btnRematch).setVisibility(View.GONE);
        containView.findViewById(R.id.btnGamelog).setVisibility(View.GONE);
        crossTurn = true;

        final TextView player1txt = (TextView) containView.findViewById(R.id.gamePlayer1Text);
        player1txt.setTypeface(Typeface.create(player1txt.getTypeface(), Typeface.BOLD));
        final TextView player2txt = (TextView) containView.findViewById(R.id.gamePlayer2Text);
        player2txt.setTypeface(Typeface.create(player2txt.getTypeface(), Typeface.NORMAL));

        for (final Cell[] rows : board.cells) {
            for (final Cell cell : rows) {
                cell.button.setImageBitmap(null);
                cell.content = Sign.EMPTY;
            }
        }

        setAllOnClickListeners();
        roundTime = System.currentTimeMillis();
    }

    @Override
    public void onClick(final View v) {
        final View foundView = containView.findViewById(v.getId());
        if (foundView instanceof Button) {
            switch (v.getId()) {
                case R.id.btnRematch:
                    Log.v("onClick", "Clicked btnRematch");
                    resetUi();
                    return;
                case R.id.btnGamelog:
                    Log.v("onClick", "Clicked btnGamelog");
                    resetUi();
                    startActivity(new Intent(getActivity(), GameHistoryActivity.class));
                    return;
            }
        }

        if (!(foundView instanceof ImageButton)) {
            Log.e("onClick", "View with id " + v.getId() + " not handled!");
            return;
        }

        final ImageButton pressedButton = (ImageButton) foundView;
        pressedButton.setImageBitmap(ImgUtils.decodeSampledBitmapFromResource(getResources(),
                crossTurn ? R.drawable.cross : R.drawable.circle, 15, 15));
        board.getCellByNumber(getButtonNumberById(v.getId()) - 1).content = (crossTurn ? Sign.CROSS : Sign.NOUGHT);
        pressedButton.setOnClickListener(null);
        crossTurn = !crossTurn;

        final TextView lastPlayerTxt =
                (TextView) containView.findViewById(crossTurn ? R.id.gamePlayer2Text : R.id.gamePlayer1Text);
        lastPlayerTxt.setTypeface(Typeface.create(lastPlayerTxt.getTypeface(), Typeface.NORMAL));

        final TextView curPlayerTxt =
                (TextView) containView.findViewById(crossTurn ? R.id.gamePlayer1Text : R.id.gamePlayer2Text);
        curPlayerTxt.setTypeface(Typeface.create(lastPlayerTxt.getTypeface(), Typeface.BOLD));

        if (!checkForGameEndingEvents() && (aiDifficulty != null && !crossTurn)) {
            cpuMove();
        }
    }

    private boolean checkForGameEndingEvents() {
        final Sign winner = miniMaxAI.hasWon(Sign.CROSS) ?
                Sign.CROSS : (miniMaxAI.hasWon(Sign.NOUGHT) ? Sign.NOUGHT : Sign.EMPTY);
        final boolean tied = (winner == Sign.EMPTY) && checkTie();

        if (tied || winner != Sign.EMPTY) {
            final String playerNameId = getResources().getString(
                    (winner == Sign.CROSS) ? R.string.player1name : R.string.player2name);
            final String playerName = tied ? "Tie" :
                    winner == Sign.NOUGHT && aiDifficulty != null ? "CPU" :
                            getActivity()
                                    .getSharedPreferences(getResources().getString(R.string.sharedPrefs),
                                            getActivity().MODE_PRIVATE)
                                    .getString(playerNameId, "N/A");

            rewriteHighscores(playerName, (int) (System.currentTimeMillis() - roundTime));


            setGameStatus(winner);
            containView.findViewById(R.id.btnRematch).setVisibility(View.VISIBLE);
            containView.findViewById(R.id.btnGamelog).setVisibility(View.VISIBLE);
            board.disableAllButtons();
            return true;
        }

        return false;
    }

    private void rewriteHighscores(String playerName, int time) {
        scores.add(new Score(playerName, aiDifficulty, time));
        Log.v("rewriteHighscores", String.valueOf(scores.size()));
        final SharedPreferences.Editor edit = getActivity()
                .getSharedPreferences(getResources().getString(R.string.highscores), Context.MODE_PRIVATE)
                .edit();

        edit.clear();
        final Iterator<Score> iterator = scores.iterator();
        int i = 0;
        while (iterator.hasNext() && i < 5) {
            final String value = iterator.next().toString();
            Log.v("rewriteHighscores", "Putting: " + value + " @ highscore_" + i);
            edit.putString("highscore_" + i++, value);
        }

        edit.commit();
    }

    private void setGameStatus(Sign winner) {
        final TextView txtWinner = (TextView) containView.findViewById(R.id.txtDeclareWinner);
        if (winner == null) {
            txtWinner.setText("");
        } else if (winner == Sign.EMPTY) {
            txtWinner.setText(R.string.game_tie);
        } else {
            final SharedPreferences sharedPreferences = getActivity()
                    .getSharedPreferences(getResources().getString(R.string.sharedPrefs), getActivity().MODE_PRIVATE);

            txtWinner.setText(winner == Sign.CROSS ? sharedPreferences.getString(getResources().getString(R.string
                    .player1name), getResources()
                    .getString(R.id.txtPlayer1Name)) :
                    aiDifficulty == null ?
                            sharedPreferences.getString(getResources().getString(R.string.player2name),
                                    getResources().getString(R.id.txtPlayer2Name)) : getString(R.string.vsCPU));
            txtWinner.append(" " + getResources().getString(R.string.wonAppend));
        }
    }

    private void cpuMove() {
        if (aiDifficulty == null) {
            return;
        }

        if (aiDifficulty.equals(getResources().getString(R.string.aiDifficulty1))) {
            while (true) {
                final ImageButton button = getButtonByNumber(1 + (int) (Math.random() * (Config.GRID_SIZE)));
                if (button.hasOnClickListeners()) {
                    onClick(button);
                    return;
                }
            }
        }

        if (aiDifficulty.equals(getResources().getString(R.string.aiDifficulty2))) {
            final int[] move = miniMaxAI.move();
            onClick(board.cells[move[0]][move[1]].button);
        }
    }

    private ImageButton getButtonByNumber(final int i) {
        return (ImageButton) containView.findViewById(getResources().getIdentifier("imageButton" + i, "id",
                getActivity().getPackageName()));
    }

    private int getButtonNumberById(final int id) {
        for (int i = 1; i < ((Config.GRID_SIZE) + 1); i++) {
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
