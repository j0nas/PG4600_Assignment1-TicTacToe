package no.wact.jenjon13.TicTacToe.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import no.wact.jenjon13.TicTacToe.R;
import no.wact.jenjon13.TicTacToe.statics.IntentStrings;
import no.wact.jenjon13.TicTacToe.statics.ResourceStrings;

public class GetPlayerNamesActivity extends Activity implements View.OnClickListener {
    private String difficultyLevel = null;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.getplayernames);

        int[] playerFields = {R.id.txtPlayer1Name, R.id.txtPlayer2Name},
                savedPlayerNames = {R.string.getplayernames_player1name, R.string.getplayernames_player2name};

        final Bundle extras = getIntent().getExtras();
        if (extras != null && (difficultyLevel = extras.getString(getResources().getString(R.string.selectaidifficulty_difficulty))) != null) {
            findViewById(playerFields[1]).setVisibility(View.INVISIBLE);
        }

        for (int i = 0; i < playerFields.length; i++) {
            ((TextView) findViewById(playerFields[i])).setText(
                    getSharedPreferences(ResourceStrings.sharedPrefs, MODE_PRIVATE)
                            .getString("player" + (i + 1) + "name", getResources().getString(savedPlayerNames[i])));
        }

        findViewById(R.id.btnEnterPlayerNames_Back).setOnClickListener(this);
        findViewById(R.id.btnEnterPlayerNames_Start).setOnClickListener(this);
    }

    @Override
    public void onClick(final View v) {
        Log.v("onClick", "onClick called");
        switch (v.getId()) {
            case R.id.btnEnterPlayerNames_Back:
                startActivity(new Intent(GetPlayerNamesActivity.this, MainMenuActivity.class));
                return;
            case R.id.btnEnterPlayerNames_Start:
                getSharedPreferences(ResourceStrings.sharedPrefs, MODE_PRIVATE)
                        .edit()
                        .putString(ResourceStrings.player1name, ((TextView) findViewById(R.id.txtPlayer1Name)).getText().toString())
                        .putString(ResourceStrings.player2name, ((TextView) findViewById(R.id.txtPlayer2Name)).getText().toString())
                        .commit();

                final Intent intent = new Intent(GetPlayerNamesActivity.this, GameScreenActivity.class);
                if (difficultyLevel != null) {
                    intent.putExtra(IntentStrings.difficulty, difficultyLevel);
                }

                startActivity(intent);
        }
    }
}
