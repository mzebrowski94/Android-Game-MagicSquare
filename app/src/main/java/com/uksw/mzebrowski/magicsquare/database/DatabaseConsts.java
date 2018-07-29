package com.uksw.mzebrowski.magicsquare.database;

public interface DatabaseConsts {
    static final String NAME = "MagicSquareBN";
    static final int VERSION = 1;

    static final String SCORES_TABLE_NAME = "Scores";
    static final String COL_ID = "_id";
    static final String COL_DATE = "Date";
    static final String COL_SCORE = "Score";
    static final String[] COLUMNS_TO_SHOW = {COL_ID, COL_DATE, COL_SCORE};
}
