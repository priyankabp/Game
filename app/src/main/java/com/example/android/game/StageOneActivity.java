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
import android.view.animation.Animation;
import android.view.animation.AnimationSet;

import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import static android.R.attr.animation;


public class StageOneActivity extends AppCompatActivity {


    StartDraggingLsntr myStartDraggingLsntr;
    EndDraggingLsntr myEndDraggingLsntr;
    Button playButton;
    ImageView player;
    MediaPlayer wav1,wav2,wav3,wav4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stage_one);

        myStartDraggingLsntr = new StartDraggingLsntr();
        myEndDraggingLsntr = new EndDraggingLsntr();

        player = (ImageView) findViewById(R.id.player);
        playButton = (Button) findViewById(R.id.playButton);
        findViewById(R.id.playButton).setOnLongClickListener(myStartDraggingLsntr);
        findViewById(R.id.moveUp).setOnLongClickListener(myStartDraggingLsntr);
        findViewById(R.id.moveDown).setOnLongClickListener(myStartDraggingLsntr);
        findViewById(R.id.moveLeft).setOnLongClickListener(myStartDraggingLsntr);
        findViewById(R.id.moveRight).setOnLongClickListener(myStartDraggingLsntr);

        findViewById(R.id.firstAction).setOnDragListener(myEndDraggingLsntr);
        findViewById(R.id.secondAction).setOnDragListener(myEndDraggingLsntr);
        findViewById(R.id.thirdAction).setOnDragListener(myEndDraggingLsntr);

        player.bringToFront();

    }

    public void onBackClick(View view) {
        Intent intent = new Intent(this, StagesActivity.class);
        startActivity(intent);
    }

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

    public void onPlayClick(View view) {

        ArrayList<String> actionSequence = getActionSequence();

        ArrayList<String> requiredActionSequence = new ArrayList<String>();
        requiredActionSequence.add("right");
        requiredActionSequence.add("down");
        requiredActionSequence.add("right");

        if (requiredActionSequence.equals(actionSequence)) {

            ObjectAnimator actionOneAnimation = ObjectAnimator.ofFloat(player, "translationX", 0f, 670f);
            ObjectAnimator actionTwoAnimation = ObjectAnimator.ofFloat(player, "translationY", 0f, 480f);
            ObjectAnimator actionThreeAnimation = ObjectAnimator.ofFloat(player, "translationX", 670f, 1340f);
            AnimatorSet set = new AnimatorSet();
            set.setDuration(3000);
            set.playSequentially(actionOneAnimation, actionTwoAnimation, actionThreeAnimation);
            set.start();

            actionThreeAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float imageXPosition = (Float) animation.getAnimatedValue();
                    String position = String.format("X:%d", (int) imageXPosition);

                    TextView positionTextView = (TextView) findViewById(R.id.positionTextView);
                    positionTextView.setText(position);

                    if (imageXPosition >= 670) {
                        wav1 = MediaPlayer.create(StageOneActivity.this, R.raw.coinpickup);
                        wav1.start();
                        findViewById(R.id.stageone_coin1).setVisibility(View.INVISIBLE);
                    }
                    if ((int) imageXPosition >= 870) {
                        wav2 = MediaPlayer.create(StageOneActivity.this, R.raw.coinpickup);
                        wav2.start();
                        findViewById(R.id.stageone_coin2).setVisibility(View.INVISIBLE);
                    }
                    if ((int) imageXPosition >= 1120) {
                        wav3 = MediaPlayer.create(StageOneActivity.this, R.raw.coinpickup);
                        wav3.start();
                        findViewById(R.id.stageone_coin3).setVisibility(View.INVISIBLE);
                    }
                    if ((int) imageXPosition == 1340) {

                        AlertDialog.Builder alertadd = new AlertDialog.Builder(StageOneActivity.this);
                        LayoutInflater factory = LayoutInflater.from(StageOneActivity.this);
                        final View youwin = factory.inflate(R.layout.activity_winning, null);
                        alertadd.setView(youwin);
                        alertadd.setNegativeButton("Next Level >>", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dlg, int something) {
                                Intent intent = new Intent(StageOneActivity.this, StageTwoActivity.class);
                                startActivity(intent);
                            }
                        });
                        alertadd.setPositiveButton("Play Again!", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dlg, int something) {
                                Intent intent = new Intent(StageOneActivity.this, StageOneActivity.class);
                                startActivity(intent);
                            }
                        });
                        AlertDialog alert = alertadd.create();
                        alert.show();
                        wav4 = MediaPlayer.create(StageOneActivity.this, R.raw.gameover);
                        wav4.start();

                    }
                }

            });

            /*final AnimationSet set = new AnimationSet(true);
            TranslateAnimation actionOne = new TranslateAnimation(0, 670, 0, 0);
            actionOne.setDuration(3000);
            set.addAnimation(actionOne);
            Animation actionTwo = new TranslateAnimation(0, 0, 0, 480);
            actionTwo.setDuration(3000);
            actionTwo.setStartOffset(3000);
            set.addAnimation(actionTwo);
            final Animation actionThree = new TranslateAnimation(0, 670, 0, 0);
            actionThree.setDuration(3000);
            actionThree.setStartOffset(6000);
            actionThree.setFillEnabled(true);
            set.addAnimation(actionThree);
            player.startAnimation(set);



            actionThree.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {

                    AlertDialog.Builder alertadd = new AlertDialog.Builder(StageOneActivity.this);
                    LayoutInflater factory = LayoutInflater.from(StageOneActivity.this);
                    final View youwin = factory.inflate(R.layout.activity_winning, null);
                    alertadd.setView(youwin);
                    alertadd.setNegativeButton("Next Level >>", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dlg, int something) {
                            Intent intent = new Intent(StageOneActivity.this, StageTwoActivity.class);
                            startActivity(intent);
                        }
                    });
                    alertadd.setPositiveButton("Play Again!", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dlg, int something) {
                            Intent intent = new Intent(StageOneActivity.this, StageOneActivity.class);
                            startActivity(intent);
                        }
                    });
                    AlertDialog alert = alertadd.create();
                    alert.show();
                    waves = MediaPlayer.create(StageOneActivity.this, R.raw.gameover);
                    waves.start();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });*/
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

    public void onStopClick(View view) {

        player.clearAnimation();

    }

    public void onExitClick(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
        System.exit(0);
    }

    private class StartDraggingLsntr implements View.OnLongClickListener {
        @Override
        public boolean onLongClick(View v) {
            WithDraggingShadow shadow = new WithDraggingShadow(v);
            ClipData data = ClipData.newPlainText("", "");
            v.startDrag(data, shadow, v, 0);
            return false;
        }
    }

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

