package com.project5779.javaproject2.controller;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.project5779.javaproject2.R;
import com.project5779.javaproject2.model.backend.BackEndFactory;
import com.project5779.javaproject2.model.entities.Drive;
import com.project5779.javaproject2.model.entities.Driver;

import java.util.List;

public class AvailableDriveFragment extends Fragment {

    private View myView;
    private List<Drive> driveList;
    private ListView listView;
    ArrayAdapter<String> adapter;
    List<String> listNamesDrivers;

    private SearchView searchView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        try {
            myView = inflater.inflate(R.layout.fragment_drive_available, container, false);
            //driveList = BackEndFactory.getInstance(getActivity()).getListDriveAvailable();
            listNamesDrivers = BackEndFactory.getInstance(getActivity()).getListNamesDrivers();

            listView = (ListView)myView.findViewById(R.id.list_view);
            adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_expandable_list_item_1, listNamesDrivers);
            listView.setAdapter(adapter);

            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    adapter.getFilter().filter(newText);
                    adapter.notifyDataSetChanged();
                    return false;
                }
            });

        } catch (Exception exp) {
            Toast.makeText(getActivity(), "ERROR", Toast.LENGTH_LONG).show();
        }
        return myView;
    }
}
