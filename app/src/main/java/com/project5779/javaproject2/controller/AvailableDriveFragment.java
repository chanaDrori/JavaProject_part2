package com.project5779.javaproject2.controller;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Location;
import android.opengl.Visibility;
import android.provider.ContactsContract;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.AttributeSet;
import android.view.View;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Fragment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.project5779.javaproject2.R;
import com.project5779.javaproject2.model.backend.BackEnd;
import com.project5779.javaproject2.model.backend.BackEndFactory;
import com.project5779.javaproject2.model.entities.Drive;


public class AvailableDriveFragment extends Fragment {

    private FusedLocationProviderClient mFusedLocationClient;

    private View myView;
    private Button startDriveButton;
    private Button endDriveButton;
    private ListView listView;
    private Spinner spinnerFilter;
    private TextView detailDrive;
    private Button ButtonAddToContact;
    private NumberPicker numberPickerKM;
    private Spinner spinnerCity;

    private List<Drive> driveList;
    private ArrayAdapter<Drive> adapter;
    private List<String> listNamesDrivers;
    private Location driverLocation;
    private String[] listSortBy;
    private Drive checkedDrive;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        try {
            checkedDrive = null;
            myView = inflater.inflate(R.layout.fragment_drive_available, container, false);
            findViews();

            //initialize driver location
            setCurrentLocation();

        } catch (Exception exp) {
            Toast.makeText(getActivity(), "ERROR", Toast.LENGTH_LONG).show();
        }
        return myView;
    }

    private void findViews(){
        driveList = BackEndFactory.getInstance(getActivity()).getListDriveAvailable();
        listView = (ListView) myView.findViewById(R.id.list_view);
        ButtonAddToContact = (Button)myView.findViewById(R.id.ButtonAddToContact);
        detailDrive = (TextView)myView.findViewById(R.id.textViewDetail);
        startDriveButton = (Button)myView.findViewById(R.id.buttonStartDrive);
        endDriveButton = (Button)myView.findViewById(R.id.buttonEndDrive);
        spinnerFilter = (Spinner)myView.findViewById(R.id.spinnerFilter);
        spinnerCity = (Spinner)myView.findViewById(R.id.spinnerCity);
        numberPickerKM = (NumberPicker)myView.findViewById(R.id.numberPickerKM);

        ButtonAddToContact.setEnabled(false);

        adapter = new ArrayAdapter<Drive>(myView.getContext(), android.R.layout.simple_list_item_1, driveList);
        listView.setAdapter(adapter);

        numberPickerKM.setMinValue(1);
        numberPickerKM.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
               // driveList = BackEndFactory.getInstance(myView.getContext()).getListDriveByKM(myView.getContext(), newVal, driverLocation);
            }
        });

        //listSortBy = Arrays.asList(getString(R.string.select), getString(R.string.sort_by_km), getString(R.string.sort_by_city));
        listSortBy = new String[]{getString(R.string.select), getString(R.string.sort_by_km), getString(R.string.sort_by_city)};
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>
                (myView.getContext(), android.R.layout.simple_spinner_dropdown_item, listSortBy);
        spinnerFilter.setAdapter(spinnerArrayAdapter);

        spinnerFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0: {
                        //driveList = BackEndFactory.getInstance(getContext()).getListDriveAvailable();
                        spinnerCity.setEnabled(false);
                        numberPickerKM.setEnabled(false);
                        break;
                    }
                    case 1: {
                        spinnerCity.setEnabled(false);
                        numberPickerKM.setEnabled(true);
                        break;
                    }
                    case 2: {
                        spinnerCity.setEnabled(true);
                        numberPickerKM.setEnabled(false);
                        break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

//        listView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                try {
//                    checkedDrive = (Drive) listView.getItemAtPosition(position);
//                    String detail = "";
//                    detail += getString(R.string.name) + ": " + checkedDrive.getNameClient() + "\n"
//                            + getString(R.string.phone) + ": " + checkedDrive.getPhoneClient() + "\n"
//                            + getString(R.string.start_point) + ": " + checkedDrive.getStartPointString() + "\n";
//                    detailDrive.setText(detail);
//                    ButtonAddToContact.setEnabled(true);
//                }
//                catch (Exception e){
//                    Toast.makeText(myView.getContext(), e.toString(), Toast.LENGTH_LONG).show();
//
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//               // detailDrive.setText(R.string.no_drive_selected_show);
//            }
 //       });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                checkedDrive = (Drive) listView.getItemAtPosition(position);
                String detail ="";
                detail += getString(R.string.name)+ ": "+ checkedDrive.getNameClient() + "\n"
                        + getString(R.string.phone)+ ": " + checkedDrive.getPhoneClient() + "\n"
                        + getString(R.string.start_point)+ ": " + checkedDrive.getStartPointString() +"\n";
                detailDrive.setText(detail);
                ButtonAddToContact.setEnabled(true);
            }
        });

        ButtonAddToContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkedDrive != null) {
                    Intent intentContact = new Intent(Intent.ACTION_INSERT);
                    intentContact.setType(ContactsContract.Contacts.CONTENT_TYPE);

                    intentContact.putExtra(ContactsContract.Intents.Insert.NAME, checkedDrive.getNameClient());
                    intentContact.putExtra(ContactsContract.Intents.Insert.PHONE, checkedDrive.getPhoneClient());
                    startActivity(intentContact);

                }
                else{
                    Toast.makeText(myView.getContext(), getString(R.string.no_drive_selected), Toast.LENGTH_LONG).show();
                }
            }
        });

        startDriveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkedDrive != null) {
                    String driverID = getActivity().getIntent().getStringExtra(getString(R.string.id));
                    BackEndFactory.getInstance(myView.getContext()).startDrive(checkedDrive, driverID, new BackEnd.Action<String>() {
                        @Override
                        public void onSuccess(String obj) {
                            Toast.makeText(myView.getContext(), getString(R.string.Have_a_nice_drive), Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onFailure(Exception exp) {
                            Toast.makeText(myView.getContext(), getString(R.string.failure_update_drive), Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onProgress(String status, double precent) {

                        }
                    });
                    driveList = BackEndFactory.getInstance(getActivity()).getListDriveAvailable();
                } else {
                    Toast.makeText(myView.getContext(), getString(R.string.no_drive_selected), Toast.LENGTH_LONG).show();
                }
            }
        });

        endDriveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String driverID = getActivity().getIntent().getStringExtra(getString(R.string.id));
                BackEndFactory.getInstance(myView.getContext()).endDrive(driverID, new BackEnd.Action<String>() {
                    @Override
                    public void onSuccess(String obj) {
                        Toast.makeText(myView.getContext(), obj, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(Exception exp) {
                        Toast.makeText(myView.getContext(), getString(R.string.failureEndDrive), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onProgress(String status, double precent) {

                    }
                });
            }
        });
    }

    private void setCurrentLocation(){

        if (ActivityCompat.checkSelfPermission(myView.getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(myView.getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]
                    {android.Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        mFusedLocationClient.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
            @SuppressLint("setTextI18n")
            @Override
            public void onSuccess(Location _location) {
                // Got last known location. In some rare situations this can be null.
                if (_location != null) {
                    List<Address> addresses;
                    //save the location
                    driverLocation = _location;
                } else {
                    Toast.makeText(myView.getContext(), R.string.cant_find_your_location,Toast.LENGTH_LONG).show();
                }
            }
        });
    }

//    @Override
//    public void onResume() {
//        driveList = BackEndFactory.getInstance(getActivity()).getListDriveAvailable();
//        super.onResume();
//    }

//    @Override
//    public void onInflate(Context context, AttributeSet attrs, Bundle savedInstanceState) {
//        driveList = BackEndFactory.getInstance(getActivity()).getListDriveAvailable();
//        super.onInflate(context, attrs, savedInstanceState);
//    }

}
