package com.project5779.javaproject2.controller;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.project5779.javaproject2.R;
import com.project5779.javaproject2.model.backend.BackEndFactory;
import com.project5779.javaproject2.model.entities.Drive;

import java.util.List;

public class DriveByDriverFragment extends Fragment {

    View myView;
    private List<Drive> driveList;
    private ListView listView;
    ArrayAdapter<Drive> adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.fragment_drive_by_driver, container, false);
        String id = getActivity().getIntent().getStringExtra(getString(R.string.id));
        driveList = BackEndFactory.getInstance(getActivity()).getListDriveByDriver(id);

        listView = (ListView)myView.findViewById(R.id.list_view);
        adapter = new ArrayAdapter<Drive>(this.getActivity(), android.R.layout.simple_list_item_1, driveList);
        listView.setAdapter(adapter);

        return myView;
    }
}
