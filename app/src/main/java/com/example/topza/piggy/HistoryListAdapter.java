package com.example.topza.piggy;

import android.app.Application;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * Created by topza on 6/6/2559.
 */
public class HistoryListAdapter extends BaseAdapter {

    Cursor itemList;
    DBHelper dbHelper;

    int count = 0;

    public void setDatabase(Context context) {
        dbHelper = new DBHelper(context);
        itemList = dbHelper.getHistoryList();
    }

    @Override
    public int getCount() {
        count = itemList.getCount();
        return count;
    }

    @Override
    public Object getItem(int position) {
        HistoryTable history = new HistoryTable();
        itemList.moveToPosition(position);

        if(itemList != null){
            history.setId(itemList.getInt(0));
            history.setHistoryApp(itemList.getString(1));
            history.setHistoryMoney(itemList.getString(2));
            history.setHistoryTime(itemList.getString(3));
            history.setHistoryIO(itemList.getString(4));
        }

        return history;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HistoryTable historyTable = null;

        historyTable = (HistoryTable) getItem(position);

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
