package com.example.android.game;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class StagesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stages);
    }

    public void onStageOneClick(View view) {
        Intent intent = new Intent(this,StageOneActivity.class);
        startActivity(intent);
    }

    public void onStageTwoClick(View view) {
        Intent intent = new Intent(this,StageTwoActivity.class);
        startActivity(intent);
    }

    public void onStageThreeClick(View view) {
        Intent intent = new Intent(this,StageThreeActivity.class);
        startActivity(intent);
    }

    public void onBackClick(View view) {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    public void onExitClick(View view) {
        finish();
        System.exit(0);
    }
}
