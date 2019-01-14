/**
 * Project in java-Android part 2
 * writers: Tirtza Rubinstain and Chana Drori
 * 01/2019
 * DataBaseFirebase code.
 * Manager the data in the firebase
 */
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

//Manager the data in the firebase
public class DataBaseFirebase implements BackEnd {

    /**
     * interface NotifyDataChange. For update the list from the firebase.
     * @param <T>
     */
    public interface NotifyDataChange<T>{
        void onDataChange(T obj);
        void onFailure(Exception exp);
    }

    //define the fields.
    private static DatabaseReference DriverRef;
    private static DatabaseReference DriveRef;
    private static List<Drive> driveList;
    private static List<Driver> driverList;
    static {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DriverRef = database.getReference("Drivers");
        DriveRef = database.getReference("Drive");

        driveList = new ArrayList<>();
        driverList = new ArrayList<>();
    }

    private static ChildEventListener driveRefChildEventListener;
    private static ChildEventListener driverRefChildEventListener;

    @Override
    public void setDriveList(List<Drive> driveList) {
        DataBaseFirebase.driveList = driveList;
    }

    @Override
    public List<Drive> getDriveList() {
        return driveList;
    }

    @Override
    public void setDriverList(List<Driver> driverList) {
        DataBaseFirebase.driverList = driverList;
    }

    @Override
    public List<Driver> getDriverList() {
        return driverList;
    }

    /**
     * notifyToDriveList function. Notify when the data change.
     * @param notifyDataChange NotifyDataChange<List<Drive>>.
     */
    @Override
    public void notifyToDriveList(final NotifyDataChange<List<Drive>> notifyDataChange){
        if(notifyDataChange != null){
            if(driveRefChildEventListener != null){
                notifyDataChange.onFailure(new Exception("first unNotify drive list"));
                return;
            }
            driveList.clear();
            driveRefChildEventListener = new ChildEventListener(){
                /**
                 * onChildAdded - add the new to the list
                 * @param dataSnapshot DataSnapshot
                 * @param s String
                 */
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Drive drive = dataSnapshot.getValue(Drive.class);
                    driveList.add(drive);
                    notifyDataChange.onDataChange(driveList);
                }

                /**
                 * onChildChanged- onChildChanged update the list
                 * @param dataSnapshot DataSnapshot
                 * @param s String
                 */
                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    Drive drive = dataSnapshot.getValue(Drive.class);
                    for (int i=0; i < driveList.size(); i++){
                        if(driveList.get(i).equals(drive)) {
                            driveList.set(i, drive);
                            break;
                        }
                    }
                    notifyDataChange.onDataChange(driveList);
                }

                /**
                 * onChildRemoved update the list
                 * @param dataSnapshot DataSnapshot
                 */
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

    /**
     * stopNotifyToDriveList remove Event Listener
     */
    @Override
    public void stopNotifyToDriveList(){
        if(driveRefChildEventListener != null){
            DriveRef.removeEventListener(driveRefChildEventListener);
            driveRefChildEventListener = null;
        }
    }

    /**
     * notifyToDriverList function. Notify when the data change.
     * @param notifyDataChange NotifyDataChange<List<Driver>>.
     */
    @Override
    public void notifyToDriverList(final NotifyDataChange<List<Driver>> notifyDataChange){
        if(notifyDataChange != null){
            if(driverRefChildEventListener != null){
                notifyDataChange.onFailure(new Exception("first unNotify driver list"));
                return;
            }
            driverList.clear();
            driverRefChildEventListener = new ChildEventListener(){
                /**
                 * onChildAdded - add the new to the list
                 * @param dataSnapshot DataSnapshot
                 * @param s String
                 */
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Driver driver = dataSnapshot.getValue(Driver.class);
                    driverList.add(driver);

                    notifyDataChange.onDataChange(driverList);
                }
                /**
                 * onChildChanged- onChildChanged update the list
                 * @param dataSnapshot DataSnapshot
                 * @param s String
                 */
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
                /**
                 * onChildRemoved update the list
                 * @param dataSnapshot DataSnapshot
                 */
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

    /**
     * stopNotifyToDriverList remove Event Listener
     */
    @Override
    public void stopNotifyToDriverList(){
        if(driverRefChildEventListener != null){
            DriverRef.removeEventListener(driverRefChildEventListener);
            driverRefChildEventListener = null;
        }
    }

    /**
     * @return List<String> list driver's names.
     */
    @Override
    public List<String> getListNamesDrivers() {
        List<String> names = new ArrayList<>();
        for (Driver driver: driverList) {
            names.add(driver.getName() + " " + driver.getLastName());
        }
        return names;
    }

    /**
     *
     * @param driver Driver to add to the list
     * @param action Action<String>. help functions to add new driver to firebase
     */
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

    /**
     * register function. login with email and password
     * @param email String Email
     * @param password String password
     */
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

    /**
     * @return List<Drive> available
     */
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

    /**
     * getListDriveByDriver
     * @param id String id of the user
     * @return  List<Drive> driveByDriver
     */
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

    /**
     * getListDriveByTarget
     * @param city String location
     * @return List<Drive>
     */
    @Override
    public List<Drive> getListDriveByTarget(String city) {
        return null;
    }

    /**
     * getListDriveByKM
     * @param KM int num of kilo meters.
     * @return List<Drive>
     */
    @Override
    public List<Drive> getListDriveByKM(int KM) {
        return null;
    }

    /**
     * getListDriveByTime
     * @param time String
     * @return List<Drive> driveByTime
     */
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

    /**
     * getListDriveByPayment calculate the payment of the drives.
     * @param payment int.
     * @return List<Drive>
     */
    @Override
    public List<Drive> getListDriveByPayment(int payment) {
        return null;
    }

    public static void list_toDelete_after(){
        if (driveList.isEmpty()) {
            Drive drive1 = new Drive();
            drive1.setNameClient("Dana");
            drive1.setEmailClient("dana@gmail.com");
            drive1.setPhoneClient("0546637284");
            drive1.setStartPointString("jerusalem");
            drive1.setStartTime("11:00");
            driveList.add(drive1);

            Drive drive2 = new Drive();
            drive2.setNameClient("rina");
            drive2.setEmailClient("rina@gmail.com");
            drive2.setPhoneClient("0546333284");
            drive2.setStartPointString("jerusalem");
            drive2.setStartTime("12:00");
            drive2.setDriverID("112233445");
            driveList.add(drive2);

            Drive drive3 = new Drive();
            drive3.setNameClient("Shalom");
            drive3.setEmailClient("shalom@gmail.com");
            drive3.setPhoneClient("0546666666");
            drive3.setStartPointString("jerusalem");
            drive3.setStartTime("11:48");
            drive3.setDriverID("112233445");
            driveList.add(drive3);

            Drive drive4 = new Drive();
            drive4.setNameClient("Ovadia");
            drive4.setEmailClient("ovadia@gmail.com");
            drive4.setPhoneClient("0543827284");
            drive4.setStartPointString("jerusalem");
            drive4.setStartTime("07:30");
            driveList.add(drive4);
        }
    }
}
