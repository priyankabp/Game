/*
 * This is the level one of the game.
 * the simplest level of the game.
 **/

package com.example.android.game;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class StageOneActivity extends AppCompatActivity {


    StartDraggingLsntr myStartDraggingLsntr;
    EndDraggingLsntr myEndDraggingLsntr;
    Button playButton;
    ImageView player;
    MediaPlayer waves;
    TextView coinsCollected, totalScore;
    int score;
    int coins;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stage_one);

        //listeners for drag and drop
        myStartDraggingLsntr = new StartDraggingLsntr();
        myEndDraggingLsntr = new EndDraggingLsntr();

        //buttons binding to the activity
        player = (ImageView) findViewById(R.id.player);
        playButton = (Button) findViewById(R.id.playButton);
        findViewById(R.id.playButton).setOnLongClickListener(myStartDraggingLsntr);
        findViewById(R.id.moveUp).setOnLongClickListener(myStartDraggingLsntr);
        findViewById(R.id.moveDown).setOnLongClickListener(myStartDraggingLsntr);
        findViewById(R.id.moveLeft).setOnLongClickListener(myStartDraggingLsntr);
        findViewById(R.id.moveRight).setOnLongClickListener(myStartDraggingLsntr);

        // selected moves
        findViewById(R.id.firstAction).setOnDragListener(myEndDraggingLsntr);
        findViewById(R.id.secondAction).setOnDragListener(myEndDraggingLsntr);
        findViewById(R.id.thirdAction).setOnDragListener(myEndDraggingLsntr);

        player.bringToFront();

        score = 0;
        coins = 0;
        coinsCollected = (TextView) findViewById(R.id.coinsCollected);
        totalScore = (TextView) findViewById(R.id.score);

        //updating score as per the coins are collected
        updateScore();

        //initializing music for coin pickup
        waves = MediaPlayer.create(StageOneActivity.this, R.raw.coinpickup);
    }

    //updating score simultaneously coins are collected
    public void updateScore(){
        score = coins*1000;
        coinsCollected.setText(Integer.toString(coins));
        totalScore.setText(Integer.toString(score));
    }

    //navigates to previous activity
    public void onBackClick(View view) {
        Intent intent = new Intent(this, StagesActivity.class);
        startActivity(intent);
    }

    //check the selected move by the player and return the list
    public ArrayList<String> getActionSequence() {

        String moveOne = (String) findViewById(R.id.firstAction).getContentDescription();
        String moveTwo = (String) findViewById(R.id.secondAction).getContentDescription();
        String moveThree = (String) findViewById(R.id.thirdAction).getContentDescription();

        ArrayList<String> actionSequence = new ArrayList<String>();
        actionSequence.add(moveOne);
        actionSequence.add(moveTwo);
        actionSequence.add(moveThree);
        return actionSequence;
    }

    // animates the player as per the selected moves
    public void onPlayClick(View view) {

        ArrayList<String> actionSequence = getActionSequence();

        //required sequence defined in the list
        ArrayList<String> requiredActionSequence = new ArrayList<String>();
        requiredActionSequence.add("right");
        requiredActionSequence.add("down");
        requiredActionSequence.add("right");

        // checks if the reqired and selected sequence is correct
        if (requiredActionSequence.equals(actionSequence)) {

            //move1
            ObjectAnimator actionOneAnimation = ObjectAnimator.ofFloat(player, "translationX", 0f, 670f);
            //move2
            ObjectAnimator actionTwoAnimation = ObjectAnimator.ofFloat(player, "translationY", 0f, 480f);
            //move3
            ObjectAnimator actionThreeAnimation = ObjectAnimator.ofFloat(player, "translationX", 670f, 1340f);
            AnimatorSet set = new AnimatorSet();
            set.setDuration(3000);
            //animating all moves simultaneously
            set.playSequentially(actionOneAnimation, actionTwoAnimation, actionThreeAnimation);
            set.start();

            actionThreeAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float imageXPosition = (Float) animation.getAnimatedValue();

                    if (imageXPosition >= 670) {
                        //coin music
                        waves.start();
                        //collecting coins
                        findViewById(R.id.stageone_coin1).setVisibility(View.INVISIBLE);
                        //updating score
                        coins = 1;
                        updateScore();
                    }
                    if ((int) imageXPosition >= 870) {
                        //coin music
                        waves.start();
                        //collecting coins
                        findViewById(R.id.stageone_coin2).setVisibility(View.INVISIBLE);
                        //updating score
                        coins = 2;
                        updateScore();
                    }
                    if ((int) imageXPosition >= 1120) {
                        //coin music
                        waves.start();
                        //collecting coins
                        findViewById(R.id.stageone_coin3).setVisibility(View.INVISIBLE);
                        //updating score
                        coins = 3;
                        updateScore();
                    }
                    if ((int) imageXPosition == 1340) {
                        // Dailouge for playing again the same level or next level
                        AlertDialog.Builder alertadd = new AlertDialog.Builder(StageOneActivity.this);
                        LayoutInflater factory = LayoutInflater.from(StageOneActivity.this);
                        final View youwin = factory.inflate(R.layout.activity_winning, null);
                        alertadd.setView(youwin);
                        alertadd.setMessage("Score: "+ score);
                        alertadd.setPositiveButton("Play Again!", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dlg, int something) {
                                Intent intent = new Intent(StageOneActivity.this, StageOneActivity.class);
                                startActivity(intent);
                            }
                        });
                        alertadd.setNegativeButton("Next Level >>", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dlg, int something) {
                                Intent intent = new Intent(StageOneActivity.this, StageTwoActivity.class);
                                startActivity(intent);
                            }
                        });
                        AlertDialog alert = alertadd.create();
                        alert.show();
                        waves = MediaPlayer.create(StageOneActivity.this, R.raw.gameover);
                        waves.start();

                    }
                }

            });

        } else {
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setMessage("Please select correct sequence!");
            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Ok", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            alertDialog.show();
        }

    }

    // Restarts the same activity
    public void onStopClick(View view) {

        Intent intent = new Intent(this,StageOneActivity.class);
        startActivity(intent);

    }

    //game exit
    public void onExitClick(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
        System.exit(0);
    }

    //start dragging lisntr
    private class StartDraggingLsntr implements View.OnLongClickListener {
        @Override
        public boolean onLongClick(View v) {
            WithDraggingShadow shadow = new WithDraggingShadow(v);
            ClipData data = ClipData.newPlainText("", "");
            v.startDrag(data, shadow, v, 0);
            return false;
        }
    }

    //end dragging lisntr
    private class EndDraggingLsntr implements View.OnDragListener {
        @Override
        public boolean onDrag(View v, DragEvent event) {
            if (event.getAction() == DragEvent.ACTION_DROP) {
                ((Button) v).setBackground(((Button) event.getLocalState()).getBackground());
                ((Button) v).setContentDescription(((Button) event.getLocalState()).getContentDescription());
            }

            return true;
        }
    }

    //Bitmap image;
    private class WithDraggingShadow extends View.DragShadowBuilder {
        public WithDraggingShadow(View view) {
            super(view);
        }

        @Override
        public void onDrawShadow(Canvas canvas) {
            super.onDrawShadow(canvas);
        }

        @Override
        public void onProvideShadowMetrics(Point shadowSize, Point shadowTouchPoint) {
            super.onProvideShadowMetrics(shadowSize, shadowTouchPoint);
        }
    }
}

