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
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.project5779.javaproject2.R;
import com.project5779.javaproject2.model.backend.BackEndFactory;
import com.project5779.javaproject2.model.entities.Drive;
import com.project5779.javaproject2.model.entities.StateOfDrive;

import java.util.ArrayList;
import java.util.List;

public class DriveByDriverFragment extends Fragment {

    View myView;
    private static List<Drive> driveList;
    private ListView listView;
    ArrayAdapter<Drive> adapter;
    private TextView TextViewWithoutDrives;
    List<Drive> drivesFinish;
    Drive driveInWork;
    String driverID;
    private Switch switchDriveInProgress;
    private TextView textViewWork;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.fragment_drive_by_driver, container, false);
        driverID = getActivity().getIntent().getStringExtra(getString(R.string.id));
        switchDriveInProgress = myView.findViewById(R.id.switch_drive_in_progress);
        textViewWork = myView.findViewById(R.id.text_view_work);
        drivesFinish = new ArrayList<>();

        driveList = BackEndFactory.getInstance(getActivity()).getListDriveByDriver(driverID);
        for (Drive d : driveList){
            if(d.getState().equals(StateOfDrive.FINISH)){
                drivesFinish.add(d);
            }
            else if(d.getState().equals(StateOfDrive.WORK)){
                driveInWork = d;
            }
        }
        listView = (ListView)myView.findViewById(R.id.list_view);
        adapter = new ArrayAdapter<Drive>(this.getActivity(), android.R.layout.simple_list_item_1, drivesFinish);
        listView.setAdapter(adapter);

        switchDriveInProgress.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    if(driveInWork != null){
                        textViewWork.setVisibility(View.VISIBLE);
                        textViewWork.setText(driveInWork.toString());
                    }
                    else {
                        textViewWork.setVisibility(View.VISIBLE);
                        textViewWork.setText(R.string.no_trip_progress);
                    }
                }
                else{
                    textViewWork.setVisibility(View.INVISIBLE);
                    textViewWork.setText("");
                }
            }
        });
        if (driveList.size() == 0)
        {
            TextViewWithoutDrives = (TextView)myView.findViewById(R.id.TextViewWithoutDrives);
            TextViewWithoutDrives.setVisibility(View.VISIBLE);
        }
        return myView;
    }

}
