package com.example.topza.piggy;

import android.app.Application;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * Created by topza on 6/6/2559.
 */
public class HistoryListAdapter extends BaseAdapter {

    DBHelper dbHelper;

    public void getDatabase(DBHelper db){
        dbHelper = db;
    }


    @Override
    public int getCount() {
        int count = dbHelper.getCountDatabase();
        return count;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HistoryTable historyTable = null;

        if (position >= 0)
            historyTable = dbHelper.getHistory(Integer.toString(position+1));

        CustomViewGroupTemplate view;

        if (convertView != null)
            view = (CustomViewGroupTemplate) convertView;
        else
            view = new CustomViewGroupTemplate(parent.getContext());

        if (historyTable != null) {

            if (historyTable.getHistoryIO() == "Input") {
                view.setCustomListItemImage(R.drawable.group2);
            } else view.setCustomListItemImage(R.drawable.group);

            view.setCustomListTextApp(historyTable.getHistoryApp());
            view.setCustomListTextMoney(historyTable.getHistoryMoney());
            view.setCustomListTextTime(historyTable.getHistoryTime());
        }


        return view;
    }


}
