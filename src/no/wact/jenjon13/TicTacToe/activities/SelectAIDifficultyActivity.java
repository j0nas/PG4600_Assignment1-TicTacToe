package no.wact.jenjon13.TicTacToe.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import no.wact.jenjon13.TicTacToe.R;

public class SelectAIDifficultyActivity extends Activity implements View.OnClickListener {
    public static final String DIFFICULTY_EASY = "1";
    public static final String DIFFICULTY_MEDIUM = "2";

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selectdifficulty);

        ViewGroup parentLayout = (ViewGroup) findViewById(R.id.layoutSelectDifficulty);
        for (int i = 0; i < parentLayout.getChildCount(); i++) {
            parentLayout.getChildAt(i).setOnClickListener(this);
        }
    }

    @Override
    public void onClick(final View v) {
        final Intent intent = new Intent(SelectAIDifficultyActivity.this, GetPlayerNamesActivity.class);
        switch (v.getId()) {
            case R.id.btnDifficultyEasy:
                intent.putExtra(getResources().getString(R.string.selectaidifficulty_difficulty), DIFFICULTY_EASY);
                break;
            case R.id.btnDifficultyMedium:
                intent.putExtra(getResources().getString(R.string.selectaidifficulty_difficulty), DIFFICULTY_MEDIUM);
                break;
            default:
                Log.e("onClick", "Unhandled onClick on view with id: " + v.getId());
                return;
        }

        startActivity(intent);
    }
}