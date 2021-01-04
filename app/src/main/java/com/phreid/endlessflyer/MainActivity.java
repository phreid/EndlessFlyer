package com.phreid.endlessflyer;

import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {
    private int highScore = 0;
    private static final int SCORE_REQ_CODE = 123;
    private static final String PREFS = "EF_SCORE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView view = findViewById(R.id.high_score_text);
        view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                | View.SYSTEM_UI_FLAG_IMMERSIVE);

        SharedPreferences prefs = getPreferences(Context.MODE_PRIVATE);
        highScore = prefs.getInt(PREFS, 0);
        view.setText("High Score: " + highScore);
    }

    public void startGame(View view) {
        Intent intent = new Intent(this, GameActivity.class);
        startActivityForResult(intent, SCORE_REQ_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        TextView scoreTextView = (TextView) findViewById(R.id.high_score_text);

        if (requestCode == SCORE_REQ_CODE) {
            if (resultCode == RESULT_OK) {
                int newScore = data.getIntExtra("score", 0);
                Log.d("test", String.valueOf(newScore));
                if (newScore > highScore) {
                    scoreTextView.setText("High Score: " + newScore);
                    highScore = newScore;
                }
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        SharedPreferences prefs = getPreferences(Context.MODE_PRIVATE);
        prefs.edit().putInt(PREFS, highScore).apply();
    }
}
