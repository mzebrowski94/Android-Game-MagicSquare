package com.uksw.mzebrowski.magicsquare.database;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.uksw.mzebrowski.magicsquare.R;

public class DbScoreItemAdapter extends CursorAdapter {
    public DbScoreItemAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.db_score_item, parent, false);

    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView dbScoreItemDate = view.findViewById(R.id.db_score_Date);
        TextView dbScoreItemScore = view.findViewById(R.id.db_score_Score);

        String date = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseConsts.COL_DATE));
        int score = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseConsts.COL_SCORE));

        dbScoreItemDate.setText(date);
        dbScoreItemScore.setText(String.valueOf(score));
    }
}
