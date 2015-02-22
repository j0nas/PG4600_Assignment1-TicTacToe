package no.wact.jenjon13.TicTacToe.activities;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import no.wact.jenjon13.TicTacToe.R;
import no.wact.jenjon13.TicTacToe.fragments.GameFragment;

public class GameScreenActivity extends Activity {
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gamescreencontainer);

        if (savedInstanceState == null) {
            Fragment gameFragment = new GameFragment();

            final Bundle extras = getIntent().getExtras();
            final String difficulty;
            if (extras != null && (difficulty = extras.getString(getResources().getString(R.string.difficulty))) !=
                    null) {
                final Bundle bundle = new Bundle();
                bundle.putString(getResources().getString(R.string.difficulty), difficulty);
                gameFragment.setArguments(bundle);
            }

            getFragmentManager()
                    .beginTransaction()
                    .add(R.id.gamescreenFragmentLayout, gameFragment)
                    .commit();
        }
    }
}
