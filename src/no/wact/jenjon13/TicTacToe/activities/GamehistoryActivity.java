package no.wact.jenjon13.TicTacToe.activities;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import no.wact.jenjon13.TicTacToe.R;

import java.util.ArrayList;
import java.util.Date;

public class GameHistoryActivity extends ListActivity {
    static ArrayList<String> highscoreValues = new ArrayList<>();
    static ArrayAdapter<String> adapter;

    public static void addNewScore(String playerName, int timeTakenInMillis, String aiDifficulty) {
        highscoreValues.add(String.format("%s: %s - %dms" +
                        (aiDifficulty != null ? " (vs CPU @ " + aiDifficulty + ")" + "" : ""),
                android.text.format.DateFormat.format("dd.MM.yy HH:MM", new Date()), playerName, timeTakenInMillis));
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gamehistory);

        if (savedInstanceState != null) {
            adapter.notifyDataSetChanged();
            return;
        }

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, highscoreValues);
        setListAdapter(adapter);
    }
}
