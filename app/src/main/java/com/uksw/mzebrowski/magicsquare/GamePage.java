package com.uksw.mzebrowski.magicsquare;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Chronometer;
import android.widget.GridView;

import com.uksw.mzebrowski.magicsquare.adapters.SquareBlockAdapter;
import com.uksw.mzebrowski.magicsquare.consts.BundleKeyNames;
import com.uksw.mzebrowski.magicsquare.consts.GameConsts;
import com.uksw.mzebrowski.magicsquare.consts.RequestCodes;
import com.uksw.mzebrowski.magicsquare.game.MagicSquareEngine;

import java.util.ArrayList;

public class GamePage extends AppCompatActivity {

    private SquareBlockAdapter squareBlockAdapter;
    private GridView gridView;
    private MagicSquareEngine magicSquareEngine;
    private Chronometer chronometer;
    private int level;
    private int hintsUsed;

    private NavigationView.OnNavigationItemSelectedListener onSidebarMenuItemSelectedListener
            = new NavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            return menuItemSelected(item);
        }
    };

    private NavigationView.OnNavigationItemSelectedListener onSidebarGameItemSelectedListener
            = new NavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            return gameItemSelected(item);
        }
    };

    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationMenuItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            return menuItemSelected(item);
        }
    };

    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationGameItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            return gameItemSelected(item);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_page);

        level = getIntent().getIntExtra(BundleKeyNames.LEVEL, GameConsts.DEFAULT_LEVEL);

        magicSquareEngine = new MagicSquareEngine(getBaseContext());
        magicSquareEngine.initMagicSquare(level);
        squareBlockAdapter = new SquareBlockAdapter(magicSquareEngine.getAllSquares());
        gridView = findViewById(R.id.Game_grid);
        gridView.setAdapter(squareBlockAdapter);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            //      Init navigation bars for orientation portrait
            initNavigationBars();
        } else {
            //      Init side bars for orientation landscape
            initSideBars();
        }

        chronometer = findViewById(R.id.game_chronometer);
        chronometer.start();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        ArrayList<String> squaresMatrix = magicSquareEngine.getSquaresMatrixContent();
        Long chronomaterBase = chronometer.getBase();

        outState.putStringArrayList(BundleKeyNames.MATRIX_CONTENT, squaresMatrix);
        outState.putLong(BundleKeyNames.COUNTED_TIME, chronomaterBase);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        ArrayList<String> squaresMatrix = savedInstanceState.getStringArrayList(BundleKeyNames.MATRIX_CONTENT);
        Long chronometerBase = savedInstanceState.getLong(BundleKeyNames.COUNTED_TIME);

        if (squaresMatrix != null) {
            magicSquareEngine.loadSquaresMatrixContent(squaresMatrix);
            if (chronometerBase != 0) {
                chronometer.setBase(chronometerBase);
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    private void initNavigationBars() {
        BottomNavigationView navigation_menu = findViewById(R.id.navigation_menu);
        BottomNavigationView navigation_game = findViewById(R.id.navigation_game);
        navigation_menu.setOnNavigationItemSelectedListener(onNavigationMenuItemSelectedListener);
        navigation_game.setOnNavigationItemSelectedListener(onNavigationGameItemSelectedListener);
    }

    private void initSideBars() {
        NavigationView sidebar_menu = findViewById(R.id.sidebar_menu);
        NavigationView sidebar_game = findViewById(R.id.sidebar_game);
        sidebar_menu.setNavigationItemSelectedListener(onSidebarMenuItemSelectedListener);
        sidebar_game.setNavigationItemSelectedListener(onSidebarGameItemSelectedListener);
    }

    private void resetMagicSquare() {
        magicSquareEngine.resetMagicSquare(level);
        gridView.removeAllViewsInLayout();
        squareBlockAdapter = new SquareBlockAdapter(magicSquareEngine.getAllSquares());
        gridView.setAdapter(squareBlockAdapter);
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();
        hintsUsed = 0;
    }

    private void submitResolvingResults() {
        if (magicSquareEngine.checkMagicSquareResolve()) {
            Intent scoreActivity = new Intent(getBaseContext(), ScorePage.class);
            scoreActivity.putExtra(BundleKeyNames.SCORE, calculateScore());
            finish();
            startActivity(scoreActivity);
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Not every square is resolved correctly, stop current game?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent scoreActivity = new Intent(getBaseContext(), ScorePage.class);
                            finish();
                            startActivity(scoreActivity);
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

    private int calculateScore() {
        int elapsedSeconds = (int) ((SystemClock.elapsedRealtime() - chronometer.getBase()) / 1000);
        return (int) Math.max(0, GameConsts.MAX_BONUS_SCORE_TIME_SECONDS - elapsedSeconds) * Math.max(1, level - hintsUsed);
    }

    private boolean menuItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_menu_home:
                Intent homePageActivity = new Intent(getBaseContext(), HomePage.class);
                homePageActivity.putExtra(BundleKeyNames.LEVEL, level);
                setResult(RequestCodes.GamePage, homePageActivity);
                this.finish();
                return true;
            case R.id.navigation_menu_score:
                Intent showScoreActivity = new Intent(getBaseContext(), ScorePage.class);
                startActivity(showScoreActivity);
                return true;
            case R.id.navigation_menu_rules:
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://pl.wikipedia.org/wiki/Kwadrat_magiczny_(matematyka)"));
                startActivity(browserIntent);
                return true;
        }
        return false;
    }

    private boolean gameItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_game_new:
                resetMagicSquare();
                return true;
            case R.id.navigation_game_hint:
                boolean solved = magicSquareEngine.solveSingleSquare();
                if (solved) {
                    hintsUsed++;
                }
                return true;
            case R.id.navigation_game_submit:
                submitResolvingResults();
                return true;
        }
        return false;
    }
}
