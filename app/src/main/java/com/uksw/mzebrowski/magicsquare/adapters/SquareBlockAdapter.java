package com.uksw.mzebrowski.magicsquare.adapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;

import java.util.ArrayList;

import static com.uksw.mzebrowski.magicsquare.consts.SquareBlockType.EDIT_TEXT;
import static com.uksw.mzebrowski.magicsquare.consts.SquareBlockType.TEXT_VIEW;

public class SquareBlockAdapter extends BaseAdapter {

    private ArrayList<View> views;

    public SquareBlockAdapter(ArrayList<View> squares) {
        this.views = squares;
    }

    @Override
    public int getCount() {
        return views.size();
    }

    @Override
    public Object getItem(int position) {
        return views.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return views.get(position) instanceof EditText ? EDIT_TEXT.ordinal() : TEXT_VIEW.ordinal();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return views.get(position);
    }
}
