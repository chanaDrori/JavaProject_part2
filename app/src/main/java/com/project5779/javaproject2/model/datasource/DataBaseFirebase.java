package com.project5779.javaproject2.model.datasource;

import com.project5779.javaproject2.model.backend.BackEnd;
import com.project5779.javaproject2.model.entities.Drive;
import com.project5779.javaproject2.model.entities.Driver;

import java.util.Date;
import java.util.List;

public class DataBaseFirebase implements BackEnd {
    @Override
    public List<String> getListNamesDrivers() {
        return null;
    }

    @Override
    public void addDriver(Driver driver) {

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
