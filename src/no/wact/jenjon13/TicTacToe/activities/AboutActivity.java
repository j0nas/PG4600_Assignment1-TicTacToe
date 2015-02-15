package no.wact.jenjon13.TicTacToe.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import no.wact.jenjon13.TicTacToe.R;

public class AboutActivity extends Activity implements OnClickListener {
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);

        findViewById(R.id.about_back).setOnClickListener(this);
    }

    @Override
    public void onClick(final View v) {
        startActivity(new Intent(AboutActivity.this, MainMenuActivity.class));
    }
}
