package no.wact.jenjon13.TicTacToe.BETTER;

import android.app.Activity;
import android.os.Bundle;
import no.wact.jenjon13.TicTacToe.R;

public class JonasActivity extends Activity {
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gamescreen);

//        final Bundle bundle = new Bundle();
//        bundle.putString("ananas", SelectAIDifficultyActivity.DIFFICULTY_MEDIUM);
//
//        bundle.putString(getResources().getString((R.string.selectaidifficulty_difficulty)), "2");
//
//        final Fragment fragment = getFragmentManager().getFragment(savedInstanceState, "basdhasids");
//
//        Log.wtf("NO", fragment.toString());
//
//        fragment.setArguments(bundle);
    }
}
