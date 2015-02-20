package no.wact.jenjon13.TicTacToe.activities;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ListView;
import no.wact.jenjon13.TicTacToe.R;
import no.wact.jenjon13.TicTacToe.statics.ResourceStrings;

import java.util.Set;

public class HighscoreActivity extends Activity {
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.highscores);

        final SharedPreferences sharedPreferences = getSharedPreferences(ResourceStrings.sharedPrefs, MODE_PRIVATE);
        final Set<String> highscores = sharedPreferences.getStringSet(ResourceStrings.highscoreValues, null);

        final ListView highScoresList = (ListView) findViewById(R.id.scoresList);

//        final ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<String>();
    }
}
