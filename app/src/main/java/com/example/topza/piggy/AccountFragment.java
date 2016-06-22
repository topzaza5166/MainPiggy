package com.example.topza.piggy;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Created by nuuneoi on 11/16/2014.
 */
public class AccountFragment extends Fragment {

    DBHelper dbHelper;
    ListView listView;
    HistoryListAdapter historyListAdapter;

    public AccountFragment() {
        super();
    }

    public static AccountFragment newInstance() {
        AccountFragment fragment = new AccountFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.account_fragment, container, false);
        initInstances(rootView);
        return rootView;
    }

    private void initInstances(View rootView) {
        // Init 'View' instance(s) with rootView.findViewById here
        listView = (ListView) rootView.findViewById(R.id.fragmentListView);
        historyListAdapter = new HistoryListAdapter();
        historyListAdapter.setDatabase(getContext());
        listView.setAdapter(historyListAdapter);

//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
//
//                android.app.AlertDialog.Builder builder =
//                        new android.app.AlertDialog.Builder(getContext());
//                builder.setTitle("Delete this history");
//                builder.setMessage("Are you sure to delete this history?");
//
//                builder.setPositiveButton(getString(android.R.string.ok),
//                        new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                int id = position+1;
//                                dbHelper.deleteHistory(Integer.toString(id));
//
//                                listView.invalidateViews();
//                                Toast.makeText(getContext(), "Deleted", Toast.LENGTH_LONG).show();
//                            }
//                        });
//
//                builder.setNegativeButton(getString(android.R.string.cancel), null);
//
//                builder.show();
//
//               // Toast.makeText(getContext(), "This position is " + position, Toast.LENGTH_SHORT).show();
//            }
//        });


    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    /*
     * Save Instance State Here
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save Instance State here
    }

    /*
     * Restore Instance State Here
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            // Restore Instance State here
        }
    }
}
