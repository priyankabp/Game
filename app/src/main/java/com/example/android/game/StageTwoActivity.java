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




public class StageTwoActivity extends AppCompatActivity {


    StartDraggingLsntr myStartDraggingLsntr;
    EndDraggingLsntr myEndDraggingLsntr;
    Button playButton;
    ImageView player;
    MediaPlayer waves;
    AnimatorSet set;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stage_two);

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
        findViewById(R.id.fourthAction).setOnDragListener(myEndDraggingLsntr);
        findViewById(R.id.fifthAction).setOnDragListener(myEndDraggingLsntr);

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
        String moveFour = (String) findViewById(R.id.fourthAction).getContentDescription();
        String moveFive = (String) findViewById(R.id.fifthAction).getContentDescription();

        ArrayList<String> actionSequence = new ArrayList<String>();
        actionSequence.add(moveOne);
        actionSequence.add(moveTwo);
        actionSequence.add(moveThree);
        actionSequence.add(moveFour);
        actionSequence.add(moveFive);
        return actionSequence;
    }

    public void onPlayClick(View view) {

        ArrayList<String> actionSequence = getActionSequence();

        ArrayList<String> requiredActionSequence = new ArrayList<String>();
        requiredActionSequence.add("right");
        requiredActionSequence.add("down");
        requiredActionSequence.add("right");
        requiredActionSequence.add("down");
        requiredActionSequence.add("right");

        if (requiredActionSequence.equals(actionSequence)) {

            ObjectAnimator actionOneAnimation = ObjectAnimator.ofFloat(player, "translationX", 0f, 430f);
            ObjectAnimator actionTwoAnimation = ObjectAnimator.ofFloat(player, "translationY", 0f, 240f);
            ObjectAnimator actionThreeAnimation = ObjectAnimator.ofFloat(player, "translationX", 430f, 670f);
            ObjectAnimator actionFourAnimation = ObjectAnimator.ofFloat(player, "translationY", 240f, 474f);
            ObjectAnimator actionFiveAnimation = ObjectAnimator.ofFloat(player, "translationX", 670f, 1340f);
            set = new AnimatorSet();
            set.setDuration(3000);
            set.playSequentially(actionOneAnimation,
                                 actionTwoAnimation,
                                 actionThreeAnimation,
                                 actionFourAnimation,
                                 actionFiveAnimation);
            set.start();

            actionFiveAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float imageXPosition = (Float) animation.getAnimatedValue();
                    String positionX = String.format("X:%d", (int) imageXPosition);

                    TextView positionTextView = (TextView) findViewById(R.id.positionTextView);
                    positionTextView.setText(positionX);
                    if (imageXPosition >= 240) {
                        findViewById(R.id.stagetwo_coin1).setVisibility(View.INVISIBLE);
                    }
                    if (imageXPosition >= 670) {
                        findViewById(R.id.stagetwo_coin2).setVisibility(View.INVISIBLE);
                    }
                    if ((int) imageXPosition >= 870) {
                        findViewById(R.id.stagetwo_coin3).setVisibility(View.INVISIBLE);
                    }
                    if ((int) imageXPosition >= 1120) {
                        findViewById(R.id.stagetwo_coin4).setVisibility(View.INVISIBLE);
                    }
                    if ((int) imageXPosition == 1340) {

                        AlertDialog.Builder alertadd = new AlertDialog.Builder(StageTwoActivity.this);
                        LayoutInflater factory = LayoutInflater.from(StageTwoActivity.this);
                        final View youwin = factory.inflate(R.layout.activity_winning, null);
                        alertadd.setView(youwin);
                        alertadd.setNegativeButton("Next Level >>", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dlg, int something) {
                                Intent intent = new Intent(StageTwoActivity.this, StageThreeActivity.class);
                                startActivity(intent);
                            }
                        });
                        alertadd.setPositiveButton("Play Again!", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dlg, int something) {
                                Intent intent = new Intent(StageTwoActivity.this, StageTwoActivity.class);
                                startActivity(intent);
                            }
                        });
                        AlertDialog alert = alertadd.create();
                        alert.show();
                        waves = MediaPlayer.create(StageTwoActivity.this, R.raw.gameover);
                        waves.start();

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

                    AlertDialog.Builder alertadd = new AlertDialog.Builder(StageTwoActivity.this);
                    LayoutInflater factory = LayoutInflater.from(StageTwoActivity.this);
                    final View youwin = factory.inflate(R.layout.activity_winning, null);
                    alertadd.setView(youwin);
                    alertadd.setNegativeButton("Next Level >>", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dlg, int something) {
                            Intent intent = new Intent(StageTwoActivity.this, StageThreeActivity.class);
                            startActivity(intent);
                        }
                    });
                    alertadd.setPositiveButton("Play Again!", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dlg, int something) {
                            Intent intent = new Intent(StageTwoActivity.this, StageTwoActivity.class);
                            startActivity(intent);
                        }
                    });
                    AlertDialog alert = alertadd.create();
                    alert.show();
                    waves = MediaPlayer.create(StageTwoActivity.this, R.raw.gameover);
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
        //TODO: clear animation on same page
       Intent intent = new Intent(this,StageTwoActivity.class);
        startActivity(intent);

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

