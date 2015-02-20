package no.wact.jenjon13.TicTacToe.activities;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import no.wact.jenjon13.TicTacToe.R;
import no.wact.jenjon13.TicTacToe.fragments.GameFragment;
import no.wact.jenjon13.TicTacToe.statics.IntentStrings;

public class GameScreenActivity extends Activity {
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gamescreencontainer);

        if (savedInstanceState == null) {
            Fragment gameFragment = new GameFragment();

            final Bundle extras = getIntent().getExtras();
            final String difficulty;
            if (extras != null && (difficulty = extras.getString(IntentStrings.difficulty)) != null) {
                final Bundle bundle = new Bundle();
                bundle.putString(IntentStrings.difficulty, difficulty);
                gameFragment.setArguments(bundle);
            }

            getFragmentManager()
                    .beginTransaction()
                    .add(R.id.gamescreenFragmentLayout, gameFragment)
                    .commit();
        }
    }
}
