/**
 * Project in java-Android part 2
 * writers: Tirtza Rubinstain and Chana Drori
 * 01/2019
 * DataBaseFirebase code.
 * Manager the data in the firebase
 */
package com.project5779.javaproject2.model.datasource;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.text.BoringLayout;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
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
import com.project5779.javaproject2.R;
import com.project5779.javaproject2.model.backend.BackEnd;
import com.project5779.javaproject2.model.entities.Drive;
import com.project5779.javaproject2.model.entities.Driver;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.project5779.javaproject2.model.entities.StateOfDrive;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

//Manager the data in the firebase
public class DataBaseFirebase implements BackEnd {

    //define the fields.
    private static DatabaseReference DriverRef;
    private static DatabaseReference DriveRef;

    private static ChildEventListener driveRefChildEventListener;
    private static ChildEventListener driverRefChildEventListener;

    private static List<Drive> driveList;
    private static List<Driver> driverList;

    static {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DriverRef = database.getReference("Drivers");
        DriveRef = database.getReference("Drives");
        driveList = new ArrayList<>();
        driverList = new ArrayList<>();
    }

//    public DataBaseFirebase() {
/////////////////////////////////////////////////////
//        list_toDelete_after();
/////////////////////////////////////////////////////////////////////////////////
//        this.notifyToDriveList(new NotifyDataChange<List<Drive>>() {
//            @Override
//            public void onDataChange(List<Drive> obj) {
//                driveList = obj;
//            }
//
//            @Override
//            public void onFailure(Exception exp) {
//                //Toast.makeText().show;
//            }
//        });
//
//        this.notifyToDriverList(new NotifyDataChange<List<Driver>>() {
//            @Override
//            public void onDataChange(List<Driver> obj) {
//                driverList = obj;
//            }
//
//            @Override
//            public void onFailure(Exception exp) {
//
//            }
//        });
//    }

    /**
     * interface NotifyDataChange. For update the list from the firebase.
     * @param <T>
     */
    public interface NotifyDataChange<T>{
        void onDataChange(T obj);
        void onFailure(Exception exp);
    }

//    /**
//     * format the data from firebase to Drive
//     *
//     * @param dataSnapshot the firebase ref
//     * @return new Drive
//     */
//    private Drive dataToDrive(DataSnapshot dataSnapshot) {
//        Drive drive = new Drive(
//                dataSnapshot.child("state").getValue().toString(),
//                dataSnapshot.child("startTime").getValue().toString(),
//                dataSnapshot.child("endTime").getValue().toString(),
//                dataSnapshot.child("nameClient").getValue().toString(),
//                dataSnapshot.child("phoneClient").getValue().toString(),
//                dataSnapshot.child("emailClient").getValue().toString()
//        );
//        drive.setDriverID(dataSnapshot.child("driverID").getValue().toString());
//        drive.setEndPointString(dataSnapshot.child("startPointString").getValue().toString());
//        drive.setEndPointString(dataSnapshot.child("endPointString").getValue().toString());
//        return drive;
//    }

    @Override
    public void setDriveList(List<Drive> _driveList) {
        driveList = _driveList;
    }

    @Override
    public List<Drive> getDriveList() {
        return driveList;
    }

    @Override
    public void setDriverList(List<Driver> driverList) {
        driverList = driverList;
    }

    @Override
    public List<Driver> getDriverList() {
        return driverList;
    }

    /**
     * notifyToDriveList function. Notify when the data change.
     * @param notifyDataChange NotifyDataChange<List<Drive>>.
     */
    //@Override
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
                    drive.setKey(dataSnapshot.getKey());
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
    //@Override
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
    //@Override
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
    //@Override
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
       List<Drive> driveByCity = new ArrayList<>();
       for(Drive d : driveList){
           if(d.getStartPointString().contains(city)){
               driveByCity.add(d);
           }
       }
        return driveByCity;
    }

    /**
     * getListDriveByKM
     * @param KM int num of kilo meters.
     * @return List<Drive>
     */
    @Override
    public List<Drive> getListDriveByKM(Context context, int KM, Location driverLocation) {
        List<Drive> drivesByKM = new ArrayList<>();
        Location locDrive = new Location(LocationManager.GPS_PROVIDER);
        for (Drive d : driveList){
            try {
                locDrive.setLatitude(d.getLat(context));
                locDrive.setLongitude(d.getLon(context));
                if(locDrive.distanceTo(driverLocation) <= KM){
                    drivesByKM.add(d);
                }
            }
            catch (Exception e){
                Toast.makeText(context, "שגיאה בסינון הנסיעות לפי KM", Toast.LENGTH_LONG).show();
            }

        }
        return drivesByKM;
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

    @Override
    public void startDrive(Drive drive, final String driverID, final Action<String> action) {
        List<Drive> listDrive = getListDriveByDriver(driverID);
        for(Drive d : listDrive){
            if(d.getState().equals(StateOfDrive.WORK)){
                action.onFailure(new Exception(String.valueOf(R.string.please_finish_driving)));
                return;
            }
        }
        DriveRef.child(drive.getKey()).child("state").setValue(StateOfDrive.WORK);
        DriveRef.child(drive.getKey()).child("driverID").setValue(driverID)
        .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
               action.onSuccess(driverID);
            }

        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                action.onFailure(e);
            }
        });
    }

    public void endDrive(String driverID, final Action<String> action)
    {
        List<Drive> driveList = getListDriveByDriver(driverID);
        Boolean inWorkExist = false;
        for (Drive d : driveList){
            if (d.getState().equals(StateOfDrive.WORK))
            {
                inWorkExist = true;
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat hm = new SimpleDateFormat("HH:mm");
                String endTime = hm.format(calendar.getTime());

                DriverRef.child(d.getKey()).child("endTime").setValue(endTime);
                DriveRef.child(d.getKey()).child("state").setValue(StateOfDrive.FINISH)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                action.onSuccess(String.valueOf(R.string.success_finish_drive));
                            }

                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                action.onFailure(new Exception(""));
                            }
                        });
            }
        }
        if(!inWorkExist) {
            action.onFailure(new Exception(""));
        }
    }

//    public void list_toDelete_after(){
//        if (driveList.isEmpty()) {
//            Drive drive1 = new Drive();
//            drive1.setNameClient("Dana");
//            drive1.setEmailClient("dana@gmail.com");
//            drive1.setPhoneClient("0546637284");
//            drive1.setStartPointString("jerusalem");
//            drive1.setStartTime("11:00");
//            driveList.add(drive1);
//
//            Drive drive2 = new Drive();
//            drive2.setNameClient("rina");
//            drive2.setEmailClient("rina@gmail.com");
//            drive2.setPhoneClient("0546333284");
//            drive2.setStartPointString("jerusalem");
//            drive2.setStartTime("12:00");
//            drive2.setDriverID("112233445");
//            driveList.add(drive2);
//
//            Drive drive3 = new Drive();
//            drive3.setNameClient("Shalom");
//            drive3.setEmailClient("shalom@gmail.com");
//            drive3.setPhoneClient("0546666666");
//            drive3.setStartPointString("jerusalem");
//            drive3.setStartTime("11:48");
//            drive3.setDriverID("112233445");
//            driveList.add(drive3);
//
//            Drive drive4 = new Drive();
//            drive4.setNameClient("Ovadia");
//            drive4.setEmailClient("ovadia@gmail.com");
//            drive4.setPhoneClient("0543827284");
//            drive4.setStartPointString("jerusalem");
//            drive4.setStartTime("07:30");
//            driveList.add(drive4);
//        }
//    }

//    @Override
//    protected void finalize() throws Throwable {
//        this.stopNotifyToDriverList();
//        this.stopNotifyToDriveList();
//        super.finalize();
//    }
}
