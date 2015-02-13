package no.wact.jenjon13.TicTacToe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

public class SelectAIDifficultyActivity extends Activity implements View.OnClickListener {
    public static final int DIFFICULTY_EASY = 1;
    public static final int DIFFICULTY_MEDIUM = 2;
    public static final int DIFFICULTY_HARD = 3;
    public static final String difficultyExtraName = "difficulty";

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_ai_difficulty);

        ViewGroup parentLayout = (ViewGroup) findViewById(R.id.layoutSelectDifficulty);
        for (int i = 0; i < parentLayout.getChildCount(); i++) {
            parentLayout.getChildAt(i).setOnClickListener(this);
        }
    }

    @Override
    public void onClick(final View v) {
        final Intent intent = new Intent(SelectAIDifficultyActivity.this, GameActivity.class);

        switch (v.getId()) {
            case R.id.btnDifficultyEasy:
                intent.putExtra(difficultyExtraName, DIFFICULTY_EASY);
                break;
            case R.id.btnDifficultyMedium:
                intent.putExtra(difficultyExtraName, DIFFICULTY_MEDIUM);
                break;
            case R.id.btnDifficultyHard:
                intent.putExtra(difficultyExtraName, DIFFICULTY_HARD);
                break;
            default:
                Log.e("onClick", "Unhandled onClick on view with id: " + v.getId());
                return;
        }

        startActivity(intent);
    }
}