package com.uksw.mzebrowski.magicsquare;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.uksw.mzebrowski.magicsquare.consts.BundleKeyNames;
import com.uksw.mzebrowski.magicsquare.consts.GameConsts;
import com.uksw.mzebrowski.magicsquare.consts.RequestCodes;

public class LevelPage extends AppCompatActivity {

    private TextView difficultyInfo;
    private int level = GameConsts.DEFAULT_LEVEL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        level = getIntent().getIntExtra(BundleKeyNames.LEVEL, GameConsts.DEFAULT_LEVEL);

        setContentView(R.layout.activity_level_page);
        SeekBar seekbar = findViewById(R.id.seekBar);
        seekbar.setProgress(level);
        seekbar.setOnSeekBarChangeListener(initOnDragListener());

        difficultyInfo = findViewById(R.id.tv_difficultynumber);
    }

    private SeekBar.OnSeekBarChangeListener initOnDragListener() {

        return new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                level = progress;
                String s = getString(R.string.chosen_difficulty);
                difficultyInfo.setText(s.concat(" " + String.valueOf(progress)));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        };
    }

    public void onClickSubmitChosenLevel(View view) {
        Intent resultIntent = new Intent(this, HomePage.class);
        resultIntent.putExtra(BundleKeyNames.LEVEL, level);
        setResult(RequestCodes.LevelPage, resultIntent);
        this.finish();
    }
}
