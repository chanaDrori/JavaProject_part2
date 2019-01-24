package com.project5779.javaproject2.controller;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.project5779.javaproject2.R;
import com.project5779.javaproject2.model.backend.BackEndFactory;
import com.project5779.javaproject2.model.entities.Drive;

import java.util.ArrayList;
import java.util.List;

public class DriveByDriverFragment extends Fragment {

    View myView;
    private static List<Drive> driveList;
    private ListView listView;
    ArrayAdapter<Drive> adapter;
    private TextView TextViewWithoutDrives;
    String driverID;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.fragment_drive_by_driver, container, false);
        driverID = getActivity().getIntent().getStringExtra(getString(R.string.id));
        driveList = BackEndFactory.getInstance(getActivity()).getListDriveByDriver(driverID);

        listView = (ListView)myView.findViewById(R.id.list_view);
        adapter = new ArrayAdapter<Drive>(this.getActivity(), android.R.layout.simple_list_item_1, driveList);
        listView.setAdapter(adapter);

        if (driveList.size() == 0)
        {
            TextViewWithoutDrives = (TextView)myView.findViewById(R.id.TextViewWithoutDrives);
            TextViewWithoutDrives.setVisibility(View.VISIBLE);
        }
        return myView;
    }

//    @Override
//    public void onInflate(Context context, AttributeSet attrs, Bundle savedInstanceState) {
//       // driveList = BackEndFactory.getInstance(getActivity()).getListDriveByDriver(driverID);
//        super.onInflate(context, attrs, savedInstanceState);
//    }
//
//
//    @Override
//    public void onStart() {
////        driveList = new ArrayList<>();
////        driveList = BackEndFactory.getInstance(getActivity()).getListDriveByDriver(driverID);
//        super.onStart();
//    }
//
//    @Override
//    public void onResume() {
////        driveList = new ArrayList<>();
////        driveList = BackEndFactory.getInstance(getActivity()).getListDriveByDriver(driverID);
//        super.onResume();
//    }
}
