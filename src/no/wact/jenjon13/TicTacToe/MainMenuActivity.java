package no.wact.jenjon13.TicTacToe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

public class MainMenuActivity extends Activity implements View.OnClickListener {
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainmenu);

        ViewGroup parentLayout = (ViewGroup) findViewById(R.id.layoutMainMenu);
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
                startActivity(new Intent(MainMenuActivity.this, GameActivity.class));
                break;
//            case R.id.btnAboutGame: TODO
//            case R.id.btnHighScores: TODO

            default:
                Log.e("onClick", "Unhandled onClick on view with id: " + v.getId());
        }
    }
}
