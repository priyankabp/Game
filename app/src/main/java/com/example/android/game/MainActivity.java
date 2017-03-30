/*
* This is the first display page of the game.
* */
package com.example.android.game;

import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {

    private Button playButton;
    private Button registerButton;
    MediaPlayer waves;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById((R.id.playButton)).setOnClickListener(new MyLysnr());
        findViewById((R.id.registerButton)).setOnClickListener(new MyLysnr());

        //starts the background music for the game
        waves = MediaPlayer.create(MainActivity.this, R.raw.gametrack);
        waves.start();
    }


    public void onPlayClick(View view) {
        Intent intent = new Intent(this, StagesActivity.class);
        startActivity(intent);
    }

    private class MyLysnr implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if(v.getId()==R.id.playButton){
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
            if(v.getId()==R.id.registerButton){

            }
        }
    }
}
