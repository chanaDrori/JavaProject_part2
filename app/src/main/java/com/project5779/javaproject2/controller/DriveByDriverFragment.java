package com.project5779.javaproject2.controller;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.project5779.javaproject2.R;
import com.project5779.javaproject2.model.backend.BackEndFactory;
import com.project5779.javaproject2.model.entities.Drive;

import java.util.List;

public class DriveByDriverFragment extends Fragment {

    View myView;
    private List<Drive> driveList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.fragment_drive_by_driver, container, false);
        return myView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        driveList = BackEndFactory.getInstance(getActivity()).getListDriveByDriver("112233445");
    }
}
