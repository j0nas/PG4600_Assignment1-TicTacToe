package no.wact.jenjon13.TicTacToe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainMenuActivity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.startmenu);
        findViewById(R.id.btnStart2pGame).setOnClickListener(this);
        findViewById(R.id.btnStart1pGame).setOnClickListener(this);
    }

    @Override
    public void onClick(final View v) {
        boolean addAI = false;

        switch (v.getId()) {
            case R.id.btnStart1pGame:
                addAI = true;
            case R.id.btnStart2pGame:
                final Intent intent = new Intent(MainMenuActivity.this, GameActivity.class);
                intent.putExtra("vsCPU", addAI);
                startActivity(intent);
                break;
        }
    }
}
