package com.project5779.javaproject2.model.datasource;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.project5779.javaproject2.model.backend.BackEnd;
import com.project5779.javaproject2.model.entities.Drive;
import com.project5779.javaproject2.model.entities.Driver;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.project5779.javaproject2.model.entities.StateOfDrive;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DataBaseFirebase implements BackEnd {

    public interface NotifyDataChange<T>{
        void onDataChange(T obj);
        void onFailure(Exception exp);
    }

    private static DatabaseReference DriverRef;
    private static DatabaseReference DriveRef;
    static List<Drive> driveList;
    static List<Driver> driverList;
    static {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DriverRef = database.getReference("Drivers");
        DriveRef = database.getReference("Drive");

        driveList = new ArrayList<>();
        driverList = new ArrayList<>();
    }

    private static ChildEventListener driveRefChildEventListener;
    private static ChildEventListener driverRefChildEventListener;

    public static void notifyToDriveList(final NotifyDataChange<List<Drive>> notifyDataChange){
        if(notifyDataChange != null){
            if(driveRefChildEventListener != null){
                notifyDataChange.onFailure(new Exception("first unNotify drive list"));
                return;
            }
            driveList.clear();
            driveRefChildEventListener = new ChildEventListener(){
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Drive drive = dataSnapshot.getValue(Drive.class);
                    driveList.add(drive);

                    notifyDataChange.onDataChange(driveList);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    Drive drive = dataSnapshot.getValue(Drive.class);
                    for (int i=0; i<driveList.size(); i++){
                        if(driveList.get(i).equals(drive)) {
                            driveList.set(i, drive);
                            break;
                        }
                    }
                    notifyDataChange.onDataChange(driveList);
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    Drive drive = dataSnapshot.getValue(Drive.class);
                    for (int i=0; i<driveList.size(); i++){
                        if(driveList.get(i).equals(drive)) {
                            driveList.remove(i);
                            break;
                        }
                    }
                    notifyDataChange.onDataChange(driveList);
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    notifyDataChange.onFailure(databaseError.toException());
                }
            };
            DriveRef.addChildEventListener(driveRefChildEventListener);
        }
    }

    public static void stopNotifyToDriveList(){
        if(driveRefChildEventListener != null){
            DriveRef.removeEventListener(driveRefChildEventListener);
            driveRefChildEventListener = null;
        }
    }


    public static void notifyToDriverList(final NotifyDataChange<List<Driver>> notifyDataChange){
        if(notifyDataChange != null){
            if(driverRefChildEventListener != null){
                notifyDataChange.onFailure(new Exception("first unNotify driver list"));
                return;
            }
            driverList.clear();
            driverRefChildEventListener = new ChildEventListener(){
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Driver driver = dataSnapshot.getValue(Driver.class);
                    driverList.add(driver);

                    notifyDataChange.onDataChange(driverList);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    Driver driver = dataSnapshot.getValue(Driver.class);
                    for (int i=0; i<driverList.size(); i++){
                        if(driverList.get(i).equals(driver)) {
                            driverList.set(i, driver);
                            break;
                        }
                    }
                    notifyDataChange.onDataChange(driverList);
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    Driver driver = dataSnapshot.getValue(Driver.class);
                    for (int i=0; i<driverList.size(); i++){
                        if(driverList.get(i).equals(driver)) {
                            driverList.remove(i);
                            break;
                        }
                    }
                    notifyDataChange.onDataChange(driverList);
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    notifyDataChange.onFailure(databaseError.toException());
                }
            };
            DriverRef.addChildEventListener(driverRefChildEventListener);
        }
    }

    public static void stopNotifyToDriverList(){
        if(driverRefChildEventListener != null){
            DriverRef.removeEventListener(driverRefChildEventListener);
            driverRefChildEventListener = null;
        }
    }

    @Override
    public List<String> getListNamesDrivers() {
        List<String> names = new ArrayList<>();
        for (Driver driver: driverList) {
            names.add(driver.getName() + " " + driver.getLastName());
        }
        return names;
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
    public void register(String email, String password) {
       // FirebaseAuth auth;
       // auth = FirebaseAuth.getInstance();
       /* auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    //FirebaseUser user = auth.getCurrentUser();
                } else {
                    // If sign in fails, display a message to the user.
                   // Toast.makeText(, "Authentication failed.", Toast.LENGTH_SHORT).show();
                }
            }
        });*/
    }

    @Override
    public List<Drive> getListDriveAvailable() {
        List<Drive> available = new ArrayList<>();
        for(Drive drive: driveList){
            if(drive.getState().toString().equals(StateOfDrive.AVAILABLE.toString())){
                available.add(drive);
            }
        }
        return  available;
    }

    @Override
    public List<Drive> getListDriveByDriver(String id) {
        List<Drive> driveByDriver = new ArrayList<>();
        for(Drive drive: driveList){
            if(drive.getDriverID().equals(id)){
                driveByDriver.add(drive);
            }
        }
        return  driveByDriver;
    }

    @Override
    public List<Drive> getListDriveByTarget(String city) {
        return null;
    }

    @Override
    public List<Drive> getListDriveByKM(int KM) {
        return null;
    }

    @Override
    public List<Drive> getListDriveByTime(String time) {
        List<Drive> driveByTime = new ArrayList<>();
        for(Drive drive: driveList){
            if(drive.getStartTime().equals(time)){
                driveByTime.add(drive);
            }
        }
        return  driveByTime;
    }

    @Override
    public List<Drive> getListDriveByPayment(int payment) {
        return null;
    }
}
