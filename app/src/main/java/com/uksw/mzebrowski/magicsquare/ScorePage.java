package com.uksw.mzebrowski.magicsquare;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;

import com.uksw.mzebrowski.magicsquare.consts.BundleKeyNames;
import com.uksw.mzebrowski.magicsquare.database.DatabaseConsts;
import com.uksw.mzebrowski.magicsquare.database.DbScoreItemAdapter;
import com.uksw.mzebrowski.magicsquare.database.MagicSquareDBHelper;

public class ScorePage extends AppCompatActivity {

    MagicSquareDBHelper magicSquareDBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initDataBase();
        setContentView(R.layout.activity_score_page);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        int score = getIntent().getIntExtra(BundleKeyNames.SCORE, -1);
        if (score >= 0 && magicSquareDBHelper != null) {
            magicSquareDBHelper.addScoreAtNow(score);
        }
        showScores();
    }

    private void initDataBase() {
        Context context = getBaseContext();
        magicSquareDBHelper =
                new MagicSquareDBHelper(context, DatabaseConsts.NAME, null, DatabaseConsts.VERSION);
    }

    private void showScores() {
        Cursor allScores = magicSquareDBHelper.getAll();

        ListView dbScoreItems = findViewById(R.id.db_score_items_listview);
        DbScoreItemAdapter dbScoreItemAdapter = new DbScoreItemAdapter(this, allScores);
        dbScoreItems.setAdapter(dbScoreItemAdapter);
    }
}
