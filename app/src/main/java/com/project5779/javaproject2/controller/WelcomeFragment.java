package com.project5779.javaproject2.controller;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.project5779.javaproject2.R;
import com.project5779.javaproject2.model.backend.BackEndFactory;
import com.project5779.javaproject2.model.entities.Driver;

import java.util.List;

public class WelcomeFragment extends Fragment {

    View myView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.fragment_welcome, container, false);
        String id = getActivity().getIntent().getStringExtra(getString(R.string.id));
        TextView textView = (TextView)myView.findViewById(R.id.textView);

        List<Driver> driverList = BackEndFactory.getInstance(getActivity()).getDriverList();
        for (Driver d : driverList )
        {
            if(d.getId().equals(id))
            {
                textView.setText(getString(R.string.welcome) +"\n" + d.getName() + " " + d.getLastName());
            }
        }

        return myView;
    }
}
