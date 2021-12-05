package com.kiristudio.jh.d_day;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.undo.UndoAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lee on 2015-07-19.
 */
public class dayListAdapter extends BaseAdapter implements UndoAdapter {

    int Position;

    List<ddayListItem> items = new ArrayList<ddayListItem>();

    Context mContext;


    public dayListAdapter(Context context) {
        mContext = context;

    }


    public String removeTitle(int position) {

        return items.get(position).getTitle();
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    public void addItem(ddayListItem item) {

        items.add(item);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Position=position;
        dayListContent content;

        if (convertView == null) {
            content = new dayListContent(mContext);
        } else {
            content = (dayListContent) convertView;
        }

        content.setDday(items.get(position).getDday());
        content.setDate(items.get(position).getDate());
        content.setTitle(items.get(position).getTitle());

        return content;
    }




    @NonNull
    @Override
    public View getUndoView(int i, View view, @NonNull ViewGroup viewGroup) {

        View view1 = view;
        if (view1 == null) {
            view1 = LayoutInflater.from(mContext).inflate(R.layout.undo, viewGroup, false);
        }


        return view1;
    }

    @NonNull
    @Override


    public View getUndoClickView(View view) {
        return view.findViewById(R.id.undo);
    }
}
