package no.wact.jenjon13.TicTacToe.activities;

import android.app.Activity;
import android.os.Bundle;
import no.wact.jenjon13.TicTacToe.R;

/**
 * Class for the 'About' screen, accessible from the main menu.
 */
public class AboutActivity extends Activity {
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);
    }
}
