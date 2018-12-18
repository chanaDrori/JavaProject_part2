package com.project5779.javaproject2.model.backend;

import android.content.Context;

import com.project5779.javaproject2.model.datasource.DataBaseFirebase;

public final class BackEndFactory {

    private static BackEnd instance = null;

    public static final BackEnd getInstance(Context context) {
        if (instance == null) {
            instance = new DataBaseFirebase();
        }
        return instance;
    }

}
