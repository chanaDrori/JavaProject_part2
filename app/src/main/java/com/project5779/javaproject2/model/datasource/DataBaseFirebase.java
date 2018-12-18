package com.project5779.javaproject2.model.datasource;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.project5779.javaproject2.model.backend.BackEnd;
import com.project5779.javaproject2.model.entities.Drive;
import com.project5779.javaproject2.model.entities.Driver;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;
import java.util.List;

public class DataBaseFirebase implements BackEnd {
    private static DatabaseReference DriverRef;
    static {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DriverRef = database.getReference("Drivers");
    }

    @Override
    public List<String> getListNamesDrivers() {
        return null;
    }

    @Override
    public void addDriver(final Driver driver, final Action<String> action) {
        DriverRef.child(driver.getId()).setValue(driver).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                action.onSuccess(driver.getName());
                action.onProgress("upload driver data", 100);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                action.onFailure(e);
                action.onProgress("error upload driver data", 100);
            }
        });

    }

    @Override
    public List<Drive> getListDriveAvailable() {
        return null;
    }

    @Override
    public List<Drive> getListDriveByDriver(String nameDriver) {
        return null;
    }

    @Override
    public List<Drive> getListDriveByTarget(String city) {
        return null;
    }

    @Override
    public List<Drive> getListdriveByKM(int KM) {
        return null;
    }

    @Override
    public List<Drive> getListdriveByDate(Date date) {
        return null;
    }

    @Override
    public List<Drive> getListdriveByPayment(int payment) {
        return null;
    }
}
