package no.wact.jenjon13.TicTacToe;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class GameActivity extends Activity implements View.OnClickListener {
    public static final int GRID_SIZE = 9;
    private static boolean playingVsAI = false;
    private static boolean crossTurn = true;
    final TileState[] xoPos = new TileState[GRID_SIZE];

    /**
     * http://developer.android.com/training/displaying-bitmaps/load-bitmap.html
     */
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight, width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2, halfWidth = width / 2;
            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);
        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        findViewById(R.id.btnRematch).setOnClickListener(this);
        resetUi();

        playingVsAI = getIntent().getExtras() != null && getIntent().getExtras().getBoolean("vsCPU");
    }

    private void resetUi() {
        ((TextView) findViewById(R.id.txtDeclareWinner)).setText("");
        findViewById(R.id.btnRematch).setVisibility(View.GONE);
        crossTurn = true;

        for (int i = 1; i < 10; i++) {
            final ImageButton button = (ImageButton) findViewById(getResources().getIdentifier("imageButton" + i, "id", getPackageName()));
            button.setImageBitmap(null);
            button.setOnClickListener(this);
        }

        for (int i = 0; i < xoPos.length; i++) {
            xoPos[i] = TileState.NOT_SET;
        }
    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.btnRematch:
                Log.v("onClick", "Clicked btnRematch");
                resetUi();
                return;
        }

        final ImageButton pressedButton = (ImageButton) findViewById(v.getId());
        pressedButton.setImageBitmap(decodeSampledBitmapFromResource(getResources(),
                crossTurn ? R.drawable.cross : R.drawable.circle, 15, 15));

        xoPos[getButtonNumberById(v.getId()) - 1] = (crossTurn ? TileState.CROSS : TileState.CIRCLE);
        pressedButton.setOnClickListener(null);
        crossTurn = !crossTurn;

        if (!checkForGameEndingEvents() && (playingVsAI && !crossTurn)) {
            cpuMove();
        }
    }

    private boolean checkForGameEndingEvents() {
        boolean someoneWon = false;
        TileState winingPlayer = getWinningPlayer();
        if (someoneWon = !winingPlayer.equals(TileState.NOT_SET)) {
            ((TextView) findViewById(R.id.txtDeclareWinner)).setText(winingPlayer.equals(TileState.CROSS) ? R.string.cross_won : R.string.circle_won);
        }

        final boolean tied = !someoneWon && checkTie();
        if (tied) {
            ((TextView) findViewById(R.id.txtDeclareWinner)).setText(R.string.tie);
        }

        if (tied || someoneWon) {
            findViewById(R.id.btnRematch).setVisibility(View.VISIBLE);
            for (int i = 0; i < GRID_SIZE; i++) {
                getButtonByNumber(i + 1).setOnClickListener(null);
            }
        }

        return tied || someoneWon;
    }

    private void cpuMove() {
        while (true) {
            final ImageButton buttonById = getButtonByNumber(1 + (int) (Math.random() * GRID_SIZE));
            if (buttonById.hasOnClickListeners()) {
                onClick(buttonById);
                return;
            }
        }
    }

    private ImageButton getButtonByNumber(final int i) {
        return (ImageButton) findViewById(getResources().getIdentifier("imageButton" + i, "id", getPackageName()));
    }

    private int getButtonNumberById(final int id) {
        for (int i = 1; i < GRID_SIZE + 1; i++) {
            if (id == getResources().getIdentifier("imageButton" + i, "id", getPackageName())) {
                return i;
            }
        }

        Log.e("getButtonByNumber", "Didn't find id: " + id);
        return -1;
    }

    private boolean checkTie() {
        for (final TileState pos : xoPos) {
            if (pos == null || pos.equals(TileState.NOT_SET)) {
                return false;
            }
        }

        return true;
    }

    private TileState getWinningPlayer() { // TODO. make this method only return winner, not take care of anything else
        int[][] winPositions = {
                {1, 2, 3}, {1, 5, 9}, {1, 4, 7}, {4, 5, 6}, {7, 8, 9}, {2, 5, 8}, {3, 6, 9}, {3, 5, 7}
        };

        boolean somebodyWon = false;
        for (final int[] winPos : winPositions) {
            somebodyWon = xoPos[winPos[0] - 1] != null && xoPos[winPos[1] - 1] != null && xoPos[winPos[2] - 1] != null;
            somebodyWon = somebodyWon && xoPos[winPos[0] - 1].equals(xoPos[winPos[1] - 1]) && xoPos[winPos[1] - 1].equals(xoPos[winPos[2] - 1]);

            if (somebodyWon) {
//                for (final int i : winPos) { TODO: implement background color highlighting
//                    ((ImageButton) findViewById(getResources().getIdentifier("imageButton" + i, "id", getPackageName()))).setBackgroundColor(223232);
//                }
                return xoPos[winPos[0] - 1];
            }
        }

        return TileState.NOT_SET;
    }
}