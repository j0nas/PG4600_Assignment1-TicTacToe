package no.wact.jenjon13.TicTacToe.activities;

import android.app.ListActivity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import no.wact.jenjon13.TicTacToe.R;

import java.util.ArrayList;

/**
 * Class for the 'Game Log' screen, accessible from the main menu.
 */
public class GameHistoryActivity extends ListActivity {
    private static ArrayList<String> highscoreValues = new ArrayList<>();
    private static ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gamehistory);

        highscoreValues.clear();
//        highscoreValues.addAll((Collection<? extends String>) getSharedPreferences(getResources().getString(R
// .string.highscores), MODE_PRIVATE).getAll().values());
        final SharedPreferences sharedPrefs =
                getSharedPreferences(getResources().getString(R.string.highscores), MODE_PRIVATE);
        for (int i = 0; i < 5; i++) {
            highscoreValues.add(sharedPrefs.getString("highscore_" + i, "N/A"));
        }

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, highscoreValues);
        setListAdapter(adapter);
    }
}
