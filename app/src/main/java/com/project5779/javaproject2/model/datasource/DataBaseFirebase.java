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
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import java.util.Locale;

//Manager the data in the firebase
public class DataBaseFirebase implements BackEnd {

    //define the fields.
    private static DatabaseReference DriverRef;
    private static DatabaseReference DriveRef;

    private static ChildEventListener driveRefChildEventListener;
    private static ChildEventListener driverRefChildEventListener;
    private ChildEventListener serviceListener;

    private static List<Drive> driveList;
    private static List<Driver> driverList;

    static {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DriverRef = database.getReference("Drivers");
        DriveRef = database.getReference("Drives");
        driveList = new ArrayList<>();
        driverList = new ArrayList<>();
    }

    /**
     * interface NotifyDataChange. For update the list from the firebase.
     * @param <T>
     */
    public interface NotifyDataChange<T>{
        void onDataChange(T obj);
        void onFailure(Exception exp);
    }

    @Override
    public void setDriveList(List<Drive> _driveList) {
        driveList = _driveList;
    }

    @Override
    public List<Drive> getDriveList() {
        return driveList;
    }

    @Override
    public void setDriverList(List<Driver> _driverList) {
        driverList = _driverList;
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
            if(driveRefChildEventListener != null) {
                if (serviceListener != null) {
                    notifyDataChange.onFailure(new Exception("first unNotify drive list"));
                    return;
                }
                else {
                    serviceListener = new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                            notifyDataChange.onDataChange(driveList);
                        }

                        @Override
                        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                        }

                        @Override
                        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                        }

                        @Override
                        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    };
                    DriveRef.addChildEventListener(serviceListener);
                    return;
                }
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
                    if (dataSnapshot.getKey() != null) {
                        drive.setKey(dataSnapshot.getKey());
                    }
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
     * getCity get city from location
     * @param drive Drive
     * @param context Context
     * @return String city from location
     * @throws Exception
     */
    private String getCity(Drive drive, Context context)throws Exception {
        Geocoder gcd = new Geocoder(context, Locale.getDefault());
        List<Address> addresses = gcd.getFromLocation(drive.getLat(context), drive.getLon(context), 1);
        if (addresses.size() > 0)
            return addresses.get(0).getLocality();
        return null;
    }

    /**
     * ListCitiesOfDrive extract the cities from the drives list
     * @param context Context
     * @return List<String> cities
     */
    public List<String> ListCitiesOfDrive(Context context){
        List<String> cities = new ArrayList<>();
        int countError = 0;
        List<Drive> available = getListDriveAvailable();
        for(Drive d:available) {
            try {
                String city = getCity(d, context);
                if (city != null && !cities.contains(city)) {
                    cities.add(city);
                }
            } catch (Exception ex) {
                  countError++;
            }
        }
        if(countError > 0){
            Toast.makeText(context, context.getString(R.string.Can_not_get) + countError + context.getString(R.string.cities), Toast.LENGTH_SHORT).show();
        }
        return cities;
    }

    /**
     * getListDriveByTarget
     * @param city String location
     * @return List<Drive>
     */
    @Override
    public List<Drive> getListDriveByTarget(Context context, String city) {
       List<Drive> driveByCity = new ArrayList<>();
       for(Drive d : driveList) {
           try {
               if(getCity(d, context) != null && getCity(d, context).equals(city)) {
                   driveByCity.add(d);
               }
           } catch (Exception ex) {
               Toast.makeText(context,
                       context.getString(R.string.Can_not_get) + context.getString(R.string.cities), Toast.LENGTH_SHORT).show();
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
                if(locDrive.distanceTo(driverLocation)/1000 <= KM){
                    drivesByKM.add(d);
                }
            }
            catch (Exception e){
                Toast.makeText(context, R.string.error_filter_by_km, Toast.LENGTH_LONG).show();
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
    public List<Drive> getListDriveByPayment(Context context,int payment)throws Exception {

        double COST_FOR_KM = 2.0;
        int START_MONEY = 20;
        List<Drive> listDriveByPayment = new ArrayList<>();
        for (Drive d : driveList){
            if(START_MONEY + d.getLocation(context).distanceTo(d.getEndLocation(context)) *COST_FOR_KM >= payment){
                listDriveByPayment.add(d);
            }
        }
        return listDriveByPayment;
    }

    /**
     * startDrive update the drive detail in the fire base
     * @param drive Drive
     * @param driverID String driver ID
     * @param action Action<String>
     */
    @Override
    public void startDrive(final Drive drive, final String driverID, final Action<String> action) {
        List<Drive> listDrive = getListDriveByDriver(driverID);
        for(Drive d : listDrive){
            if(d.getState().equals(StateOfDrive.WORK)){
                action.onFailure(new Exception(String.valueOf(R.string.please_finish_driving)));
                return;
            }
        }
        drive.setDriverID(driverID);
        drive.setState(StateOfDrive.WORK);
        DriveRef.child(drive.getKey()).setValue(drive).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                action.onSuccess(drive.getKey());
            }
        });
    }

    /**
     * endDrive update the drive detail in the fire base
     * @param driverID String driver ID
     * @param action Action<String> contain on success and failure
     */
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
                final String endTime = hm.format(calendar.getTime());

                DriveRef.child(d.getKey()).child("endTime").setValue(endTime);
                DriveRef.child(d.getKey()).child("state").setValue(StateOfDrive.FINISH);
                d.setState(StateOfDrive.FINISH);
                d.setEndTime(endTime);
                DriveRef.child(d.getKey()).setValue(d).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        action.onSuccess(endTime);
                    }
                });
            }
        }
        if(!inWorkExist) {
            action.onFailure(new Exception(""));
        }
    }
}
