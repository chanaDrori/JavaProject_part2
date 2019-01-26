/**
 * Project in java-Android part 2
 * writers: Tirtza Rubinstain and Chana Drori
 * 01/2019
 * Available Drive fragment code.
 *Shows the possible trips and lets you filter them by destination or city of origin
 */
package com.project5779.javaproject2.controller;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Location;
import android.os.AsyncTask;
import android.provider.ContactsContract;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;

import java.util.List;

import android.app.Fragment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.NumberPicker;
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

    //Defines view fields for a fragment
    private View myView;
    private Button startDriveButton;
    private Button endDriveButton;
    private ListView listView;
    private Spinner spinnerFilter;
    private TextView detailDrive;
    private Button ButtonAddToContact;
    private NumberPicker numberPickerKM;
    private Spinner spinnerCity;
    private Button buttonSearch;
    //Defines fields for a fragment
    private FusedLocationProviderClient mFusedLocationClient;
    private List<Drive> driveList;
    private ArrayAdapter<Drive> driveAdapter;
    private List<String> listNamesDrivers;
    private Location driverLocation;
    private String[] listSortBy;
    private Drive checkedDrive;
    private ArrayAdapter<String> cityAdapter;
    private List<String> cities;
    private BackEnd backEnd;

    /**
     * onCreateView function initializes the fragment for the user
     * @param inflater LayoutInflater
     * @param container ViewGroup the activity that contain the fragment
     * @param savedInstanceState Bundle
     * @return View myView
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        try {
            //Initializes the fragment for the user
            checkedDrive = null;
            myView = inflater.inflate(R.layout.fragment_drive_available, container, false);
            backEnd = BackEndFactory.getInstance(myView.getContext());
            findViews();

            //---initialize driver location---
            setCurrentLocation();

        } catch (Exception exp) {
            Toast.makeText(getActivity(), "ERROR", Toast.LENGTH_LONG).show();
        }
        return myView;
    }

    /**
     * findViews function initializes the fragment for the user
     */
    @SuppressLint("StaticFieldLeak")
    private void findViews(){
        //get all the view element from the fragment
        driveList = backEnd.getListDriveAvailable();
        listView = (ListView) myView.findViewById(R.id.list_view);
        ButtonAddToContact = (Button)myView.findViewById(R.id.ButtonAddToContact);
        detailDrive = (TextView)myView.findViewById(R.id.textViewDetail);
        startDriveButton = (Button)myView.findViewById(R.id.buttonStartDrive);
        endDriveButton = (Button)myView.findViewById(R.id.buttonEndDrive);
        spinnerFilter = (Spinner)myView.findViewById(R.id.spinnerFilter);
        spinnerCity = (Spinner)myView.findViewById(R.id.spinnerCity);
        numberPickerKM = (NumberPicker)myView.findViewById(R.id.numberPickerKM);
        buttonSearch = (Button)myView.findViewById(R.id.buttonSearch);

        // set the button of add contact to false enabled
        ButtonAddToContact.setEnabled(false);
        //show the list of the drive
        driveAdapter = new ArrayAdapter<Drive>(myView.getContext(), android.R.layout.simple_list_item_1, driveList);
        listView.setAdapter(driveAdapter);
        //set the range of km
        numberPickerKM.setMinValue(1);
        numberPickerKM.setMaxValue(300);

        buttonSearch.setOnClickListener(new View.OnClickListener() {
            /**
             * onClick sent to Async Task of filter the drives list
             * @param v View
             */
            @Override
            public void onClick(View v) {
                new getDriveFilter().execute();
            }
        });
        //set the list of the city in the spinner sent to new thread
        listSortBy = new String[]{getString(R.string.select), getString(R.string.sort_by_km), getString(R.string.sort_by_city)};
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>
                (myView.getContext(), android.R.layout.simple_spinner_dropdown_item, listSortBy);
        //Sets the spinner's filter options
        spinnerFilter.setAdapter(spinnerArrayAdapter);
        spinnerFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0: {
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
        //AsyncTask set the list of the city in the spinner
        new AsyncTask<Void, Void, List<String>>() {

            @Override
            protected List<String> doInBackground(Void... voids) {
                cities = backEnd.ListCitiesOfDrive(myView.getContext());
                return cities;
            }

            @Override
            protected void onPostExecute(List<String> citesString) {
                cityAdapter = new ArrayAdapter<>(myView.getContext(), android.R.layout.simple_spinner_dropdown_item, citesString);
                spinnerCity.setAdapter(cityAdapter);
            }
        }.execute();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            /**
             *onItemClick of listView the list of the drive, show the details of the drive.
             * @param parent AdapterView<?>
             * @param view View
             * @param position int the position of the item were clicked.
             * @param id long of the item
             */
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                checkedDrive = (Drive) listView.getItemAtPosition(position);
                String detail ="";
                detail += getString(R.string.name)+ ": "+ checkedDrive.getNameClient() + "\n"
                        + getString(R.string.phone)+ ": " + checkedDrive.getPhoneClient() + "\n"
                        + getString(R.string.start_point)+ ": " + checkedDrive.getStartPointString() +"\n"
                        + getString(R.string.Start_time) + " : " + checkedDrive.getStartTime() + "\n";
                detailDrive.setText(detail);
                ButtonAddToContact.setEnabled(true);
            }
        });

        ButtonAddToContact.setOnClickListener(new View.OnClickListener() {
            /**
             * onClick of ButtonAddToContact sent to the app of the contacts.
             * @param v View
             */
            @Override
            public void onClick(View v) {
                if (checkedDrive != null) {
                    Intent intentContact = new Intent(Intent.ACTION_INSERT);
                    intentContact.setType(ContactsContract.Contacts.CONTENT_TYPE);
                    intentContact.putExtra(ContactsContract.Intents.Insert.NAME, checkedDrive.getNameClient());
                    intentContact.putExtra(ContactsContract.Intents.Insert.PHONE, checkedDrive.getPhoneClient());
                    startActivity(intentContact);
                } else {
                    Toast.makeText(myView.getContext(), getString(R.string.no_drive_selected), Toast.LENGTH_LONG).show();
                }
            }
        });

        startDriveButton.setOnClickListener(new View.OnClickListener() {
            /**
             * onClick of startDriveButton update the drive details and update the list of drives.
             * @param v View startDriveButton
             */
            @Override
            public void onClick(View v) {
                if (checkedDrive != null) {
                    String driverID = getActivity().getIntent().getStringExtra(getString(R.string.id));
                    backEnd.startDrive(checkedDrive, driverID, new BackEnd.Action<String>() {
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
                    new getDriveFilter().execute();
                } else {
                    Toast.makeText(myView.getContext(), getString(R.string.no_drive_selected), Toast.LENGTH_LONG).show();
                }
            }
        });

        endDriveButton.setOnClickListener(new View.OnClickListener() {
            /**
             * onClick of endDriveButton update the drive details.
             *
             * @param v View endDriveButton
             */
            @Override
            public void onClick(View v) {
                String driverID = getActivity().getIntent().getStringExtra(getString(R.string.id));
                backEnd.endDrive(driverID, new BackEnd.Action<String>() {
                    @Override
                    public void onSuccess(String obj) {
                        Toast.makeText(myView.getContext(), R.string.the_drive_finish, Toast.LENGTH_LONG).show();
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
    }//end findView.

    /**
     * sent to AsyncTask setDriverLocationAsyncTask
     */
    private void setCurrentLocation(){
        new setDriverLocationAsyncTask().execute();
    }

    /**
     * class getDriveFilter extends AsyncTask<Void, Void, List<Drive>> filter the drives list
     */
    @SuppressLint("StaticFieldLeak")
    public class getDriveFilter extends AsyncTask<Void, Void, List<Drive>>{
        /**
         * filter the drives list
         * @param aVoid Void
         * @return List<Drive> List of filtered trips
         */
        @Override
        protected List<Drive> doInBackground(Void... aVoid) {
            if (spinnerFilter.getSelectedItem().toString().equals(getString(R.string.sort_by_city))) {
                return backEnd.getListDriveByTarget(myView.getContext(), spinnerCity.getSelectedItem().toString());
            } else if (spinnerFilter.getSelectedItem().toString().equals(getString(R.string.sort_by_km))) {
                setCurrentLocation();
                if (driverLocation != null) {
                    return backEnd.getListDriveByKM(myView.getContext(), numberPickerKM.getValue(), driverLocation);
                } else {
                    Toast.makeText(myView.getContext(), R.string.location_services_impossible, Toast.LENGTH_LONG).show();
                }
            }
            return driveList;
        }

        /**
         * onPostExecute display the List of filtered trips
         * @param updateDriveList List<Drive>
         */
        @Override
        protected void onPostExecute(List<Drive> updateDriveList) {
            driveAdapter = new ArrayAdapter<Drive>(myView.getContext(), android.R.layout.simple_list_item_1, updateDriveList);
            listView.setAdapter(driveAdapter);
        }
    }

    /**
     * class setDriverLocationAsyncTask extends AsyncTask<Void, Void, Void> set the
     * current location of the driver
     */
    @SuppressLint("StaticFieldLeak")
    public class setDriverLocationAsyncTask extends AsyncTask<Void, Void, Void> {
        /**
         * doInBackground check Permission and set the location of the driver
         *
         * @param voids Void
         * @return Void
         */
        @Override
        protected Void doInBackground(Void... voids) {
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
                        Toast.makeText(myView.getContext(), R.string.cant_find_your_location, Toast.LENGTH_LONG).show();
                    }
                }
            });
            return null;
        }
    }
}
