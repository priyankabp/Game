/*
* This is the first display page of the game.
* */
package com.example.android.game;

import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;


public class MainActivity extends AppCompatActivity {

    MediaPlayer waves;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //starts the background music for the game
        waves = MediaPlayer.create(MainActivity.this, R.raw.gametrack);
        waves.start();
    }

    public void onPlayClick(View view) {
        Intent intent = new Intent(this, StagesActivity.class);
        startActivity(intent);
    }
}
