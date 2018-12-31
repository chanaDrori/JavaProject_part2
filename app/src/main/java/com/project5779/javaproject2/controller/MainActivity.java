/**
 * Project in java-Android part 2
 * writers: Tirtza Rubinstain and Chana Drori
 * 01/2019
 * Main Activity code.
 *
 */
package com.project5779.javaproject2.controller;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.project5779.javaproject2.R;
import com.project5779.javaproject2.model.backend.BackEndFactory;
import com.project5779.javaproject2.model.datasource.DataBaseFirebase;
import com.project5779.javaproject2.model.entities.Driver;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    private TextView helloTextView;

    private List<Driver> driverList;

    /**
     * onCreate function
     * @param savedInstanceState Bundle.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();

        BackEndFactory.getInstance(this).notifyToDriverList(new DataBaseFirebase.NotifyDataChange<List<Driver>>() {
            /**
             * onDataChange function. Work when the data change.
             * @param obj List<Driver>.
             */
            @Override
            public void onDataChange(List<Driver> obj) {
                driverList = obj;
            }

            /**
             * onFailure function. work when failure notify
             * @param exp exception
             */
            @Override
            public void onFailure(Exception exp) {
                Toast.makeText(getBaseContext(), getString(R.string.Error_to_get_drivers_list)
                        + exp.toString() + "\n main", Toast.LENGTH_LONG).show();
            }
        });

        Intent myIntent = getIntent();
        String id = myIntent.getStringExtra(getString(R.string.id));
        String hello = getString(R.string.hello);
        helloTextView.setText(hello);
        /*for (Driver d : driverList) {
            if (d.getId().equals(id)) {
                hello += d.getName() + " " + d.getLastName() + "!";
                helloTextView.setText(hello);
            }
        }*/


    }

    private void findViews(){
        helloTextView = (TextView)findViewById(R.id.helloTextView);
    }

    /**
     * onDestroy function. Sent to  DataBaseFirebase.stopNotifyToDriverList();
     */
    @Override
    protected void onDestroy() {
        BackEndFactory.getInstance(this).stopNotifyToDriverList();
        //BackEndFactory.getInstance(this).stopNotifyToDriveList();
        super.onDestroy();
    }
}
