package no.wact.jenjon13.TicTacToe.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import no.wact.jenjon13.TicTacToe.R;

public class MainMenuActivity extends Activity implements View.OnClickListener {
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainmenu);

        final ViewGroup parentLayout = (ViewGroup) findViewById(R.id.layoutMainMenu);
        for (int i = 0; i < parentLayout.getChildCount(); i++) {
            parentLayout.getChildAt(i).setOnClickListener(this);
        }
    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.btnStart1pGame:
                startActivity(new Intent(MainMenuActivity.this, SelectAIDifficultyActivity.class));
                break;

            case R.id.btnStart2pGame:
                startActivity(new Intent(MainMenuActivity.this, GetPlayerNamesActivity.class));
                break;
            case R.id.btnAboutGame:
                startActivity(new Intent(MainMenuActivity.this, AboutActivity.class));
                break;
            case R.id.btnHighScores:
                startActivity(new Intent(MainMenuActivity.this, HighscoreActivity.class));
            default:
                Log.e("onClick", "Unhandled onClick on view with id: " + v.getId());
        }
    }
}

// TODO remove unused resources
