package no.wact.jenjon13.TicTacToe.activities;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import no.wact.jenjon13.TicTacToe.R;

import java.util.ArrayList;
import java.util.Date;

public class GamehistoryActivity extends ListActivity implements View.OnClickListener {
    static ArrayList<String> highscoreValues = new ArrayList<>();
    static ArrayAdapter<String> adapter;

    public static void addNewScore(String playerName, int timeTakenInMillis, String aiDifficulty) {
        highscoreValues.add(String.format("%s: %s - %dms" + (aiDifficulty != null ? " (vs CPU @ " + aiDifficulty + ")" +
                        "" : ""),
                android.text.format.DateFormat.format("dd.MM.yy HH:MM", new Date()), playerName, timeTakenInMillis));
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gamehistory);
        findViewById(R.id.btnHighScores_back).setOnClickListener(this);

        if (savedInstanceState != null) {
            adapter.notifyDataSetChanged();
            return;
        }

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, highscoreValues);
        setListAdapter(adapter);
    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.btnHighScores_back:
                startActivity(new Intent(GamehistoryActivity.this, MainMenuActivity.class));
                break;
        }
    }
}
