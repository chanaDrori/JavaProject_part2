/**
 * Project in java-Android part 2
 * writers: Tirtza Rubinstain and Chana Drori
 * 01/2019
 *  interface BackEnd.
 */
package com.project5779.javaproject2.model.backend;

import android.drm.DrmStore;

import com.project5779.javaproject2.model.datasource.DataBaseFirebase;
import com.project5779.javaproject2.model.entities.Drive;
import com.project5779.javaproject2.model.entities.Driver;

import java.util.Date;
import java.util.List;

public interface BackEnd {
    /**
     * interface Action. for addDriver function.
     * @param <T>
     */
    public interface Action<T>{
        void onSuccess(T obj);
        void onFailure(Exception exp);
        void onProgress(String status, double precent);
    }

    public void setDriveList(List<Drive> driveList);
    public List<Drive> getDriveList();
    public void setDriverList(List<Driver> driverList);
    public List<Driver> getDriverList();
    public List<String> getListNamesDrivers();
    public void addDriver(Driver driver, final Action<String> action);
    public List<Drive> getListDriveAvailable();
    public List<Drive> getListDriveByDriver(String id);
    public List<Drive> getListDriveByTarget(String city);
    public List<Drive> getListDriveByKM(int KM);
    public List<Drive> getListDriveByTime(String time);
    public List<Drive> getListDriveByPayment(int payment);


    public void stopNotifyToDriveList();
    public void stopNotifyToDriverList();

    public void notifyToDriverList(final DataBaseFirebase.NotifyDataChange<List<Driver>> notifyDataChange);
    public void notifyToDriveList(final DataBaseFirebase.NotifyDataChange<List<Drive>> notifyDataChange);

}
