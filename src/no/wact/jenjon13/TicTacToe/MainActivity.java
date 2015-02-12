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

import java.util.HashMap;
import java.util.Map;


// TODO: add 1-player vs. AI mode
public class MainActivity extends Activity implements View.OnClickListener {
    private static boolean crossTurn = true;
    private static Map<ImageButton, Integer> buttonMap = new HashMap<>(); // Button and its respective id
    Boolean[] xoPos = new Boolean[9]; // Not set = null, cross = true, circle = false

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

        for (int i = 1; i < 10; i++) {
            buttonMap.put((ImageButton) findViewById(getResources().getIdentifier("imageButton" + i, "id", getPackageName())), i);
        }

        findViewById(R.id.btnRematch).setOnClickListener(this);
        resetUi();
    }

    private void resetUi() {
        ((TextView) findViewById(R.id.txtDeclareWinner)).setText("");
        findViewById(R.id.btnRematch).setVisibility(View.GONE);

        for (int i = 1; i < 10; i++) {
            final ImageButton button = (ImageButton) findViewById(getResources().getIdentifier("imageButton" + i, "id", getPackageName()));
            button.setImageBitmap(null);
            button.setOnClickListener(this);
        }

        for (int i = 0; i < xoPos.length; i++) {
            xoPos[i] = null;
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
        xoPos[buttonMap.get(pressedButton) - 1] = crossTurn;
        pressedButton.setOnClickListener(null);

        if (checkWin() || checkTie()) {
            findViewById(R.id.btnRematch).setVisibility(View.VISIBLE);
            // TODO remove listeners from buttons
        }

        crossTurn = !crossTurn;
    }

    private boolean checkTie() {
        for (final Boolean pos : xoPos) {
            if (pos == null) {
                return false;
            }
        }

        ((TextView) findViewById(R.id.txtDeclareWinner)).setText(R.string.tie);
        return true;
    }

    private boolean checkWin() { // TODO. make this method only return winner, not take care of anything else
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

                ((TextView) findViewById(R.id.txtDeclareWinner))
                        .setText(xoPos[winPos[0] - 1] ? R.string.cross_won : R.string.circle_won);
                return true;
            }
        }

        return false;
    }
}