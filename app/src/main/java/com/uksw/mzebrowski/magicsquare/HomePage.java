package com.uksw.mzebrowski.magicsquare;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.uksw.mzebrowski.magicsquare.consts.BundleKeyNames;
import com.uksw.mzebrowski.magicsquare.consts.GameConsts;
import com.uksw.mzebrowski.magicsquare.consts.RequestCodes;

public class HomePage extends AppCompatActivity {

    private int level = GameConsts.DEFAULT_LEVEL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
    }

    public void onClickStartNewGameActivity(View view) {
        Intent newGameActivity = new Intent(this, GamePage.class);
        newGameActivity.putExtra(BundleKeyNames.LEVEL, level);
        startActivityForResult(newGameActivity, RequestCodes.GamePage);
    }

    public void onClickChangeDifficulty(View view) {
        Intent changeLevelActivity = new Intent(this, LevelPage.class);
        changeLevelActivity.putExtra(BundleKeyNames.LEVEL, level);
        startActivityForResult(changeLevelActivity, RequestCodes.LevelPage);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RequestCodes.LevelPage || resultCode == RequestCodes.GamePage) {
            level = data.getIntExtra(BundleKeyNames.LEVEL, GameConsts.DEFAULT_LEVEL);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void onClickShowScore(View view) {
        Intent showScoreActivity = new Intent(this, ScorePage.class);
        startActivity(showScoreActivity);
    }

    @Override
    public void onBackPressed() {
        quitDialog();
    }

    public void onClickQuitGame(View view) {
        quitDialog();
    }

    private void quitDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finishAffinity();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
