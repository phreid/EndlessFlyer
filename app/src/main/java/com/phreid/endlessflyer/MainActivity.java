package com.phreid.endlessflyer;

import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {
    private int highScore = 0;
    private static final int SCORE_REQ_CODE = 123;
    private static final String PREFS = "EF_SCORE";
    private static final String DIALOG_HOWTO = "DialogHowTo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        hideUI();

        updateHighScore();
    }

    private void hideUI() {
        View view = getWindow().getDecorView();
        view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    private void updateHighScore() {
        SharedPreferences prefs = getPreferences(Context.MODE_PRIVATE);
        highScore = prefs.getInt(PREFS, 0);

        TextView view = findViewById(R.id.high_score_text);
        String scoreText = getString(R.string.main_score, highScore);
        view.setText(scoreText);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateHighScore();
        hideUI();
    }

    public void startGame(View view) {
        Intent intent = new Intent(this, GameActivity.class);
        startActivityForResult(intent, SCORE_REQ_CODE);
    }

    public void showHowToDialog(View view) {
        FragmentManager manager = getSupportFragmentManager();
        HowToFragment dialog = new HowToFragment();
        dialog.show(manager, DIALOG_HOWTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        TextView scoreTextView = (TextView) findViewById(R.id.high_score_text);

        if (requestCode == SCORE_REQ_CODE) {
            if (resultCode == RESULT_OK) {
                int newScore = data.getIntExtra("score", 0);
                if (newScore > highScore) {
                    String scoreText = getString(R.string.main_score, newScore);
                    scoreTextView.setText(scoreText);
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
