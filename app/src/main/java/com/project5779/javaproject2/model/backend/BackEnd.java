package com.project5779.javaproject2.model.backend;

import android.drm.DrmStore;

import com.project5779.javaproject2.model.entities.Drive;
import com.project5779.javaproject2.model.entities.Driver;

import java.util.Date;
import java.util.List;

public interface BackEnd {
    public interface Action<T>{
        void onSuccess(T obj);
        void onFailure(Exception exp);
        void onProgress(String status, double precent);
    }
    public List<String> getListNamesDrivers();
    public void addDriver(Driver driver, final Action<String> action);
    public void register(String email, String password);
    public List<Drive> getListDriveAvailable();
    public List<Drive> getListDriveByDriver(String id);
    public List<Drive> getListDriveByTarget(String city);
    public List<Drive> getListDriveByKM(int KM);
    public List<Drive> getListDriveByTime(String time);
    public List<Drive> getListDriveByPayment(int payment);
}
