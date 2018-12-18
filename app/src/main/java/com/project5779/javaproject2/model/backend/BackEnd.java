package com.project5779.javaproject2.model.backend;

import com.project5779.javaproject2.model.entities.Drive;
import com.project5779.javaproject2.model.entities.Driver;

import java.util.Date;
import java.util.List;

public interface BackEnd {
    public List<String> getListNamesDrivers();
    public void addDriver(Driver driver);
    public List<Drive> getListDriveAvailable();
    public List<Drive> getListDriveByDriver(String nameDriver);
    public List<Drive> getListDriveByTarget(String city);
    public List<Drive> getListdriveByKM(int KM);
    public List<Drive> getListdriveByDate(Date date);
    public List<Drive> getListdriveByPayment(int payment);
}
