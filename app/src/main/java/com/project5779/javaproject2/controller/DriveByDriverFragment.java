/**
 * Project in java-Android part 2
 * writers: Tirtza Rubinstain and Chana Drori
 * 01/2019
 * DriveByDriverFragment show the drive of the driver by id's driver.
 */
package com.project5779.javaproject2.controller;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import com.project5779.javaproject2.R;
import com.project5779.javaproject2.model.backend.BackEndFactory;
import com.project5779.javaproject2.model.entities.Drive;
import com.project5779.javaproject2.model.entities.StateOfDrive;

import java.util.ArrayList;
import java.util.List;

public class DriveByDriverFragment extends Fragment {

    private static List<Drive> driveListToShow;
    private ListView listView;
    private TextView TextViewWithoutDrives;
    private Switch switchDriveInProgress;
    private TextView textViewWork;
    private View myView;
    private ArrayAdapter<Drive> adapter;
    private List<Drive> drivesFinish;
    private Drive driveInWork;
    private String driverID;

    /**
     * onCreateView DriveByDriverFragment show the drive list
     * @param inflater LayoutInflater
     * @param container ViewGroup
     * @param savedInstanceState Bundle
     * @return myView View
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.fragment_drive_by_driver, container, false);
        driverID = getActivity().getIntent().getStringExtra(getString(R.string.id));
        //switchDriveInProgress = myView.findViewById(R.id.switch_drive_in_progress);
        //textViewWork = myView.findViewById(R.id.text_view_work);
        drivesFinish = new ArrayList<>();
        driveInWork = new Drive();

        driveListToShow = BackEndFactory.getInstance(getActivity()).getListDriveByDriver(driverID);
        if(driveListToShow != null) {
            for (Drive d : driveListToShow) {
                //add to list of the finished drive
                if (d.getState().equals(StateOfDrive.FINISH)) {
                    drivesFinish.add(d);
                } else if (d.getState().equals(StateOfDrive.WORK)) {
                    driveInWork = d; // the drive in progress by this driver
                }
            }
        }
        listView = (ListView)myView.findViewById(R.id.list_view);
        adapter = new ArrayAdapter<Drive>(myView.getContext(), android.R.layout.simple_list_item_1, drivesFinish);
        listView.setAdapter(adapter);

//        switchDriveInProgress.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            /**
//             * onCheckedChanged of switchDriveInProgress set the detail of the drive in progress
//             * @param buttonView CompoundButton
//             * @param isChecked boolean check if the switch is true/false
//             */
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if(isChecked){
//                    if(driveInWork != null){
//                        //textViewWork.setVisibility(View.VISIBLE);
//                        textViewWork.setText(driveInWork.toString());
//                    }
//                    else {
//                        //textViewWork.setVisibility(View.VISIBLE);
//                        textViewWork.setText(R.string.no_trip_progress);
//                    }
//                }
//                else{
//                   // textViewWork.setVisibility(View.INVISIBLE);
//                    textViewWork.setText("");
//                }
//            }
//        });
        if (drivesFinish.size() == 0)
        {
            TextViewWithoutDrives = (TextView)myView.findViewById(R.id.TextViewWithoutDrives);
            TextViewWithoutDrives.setVisibility(View.VISIBLE);
        }
        return myView;
    }
}
