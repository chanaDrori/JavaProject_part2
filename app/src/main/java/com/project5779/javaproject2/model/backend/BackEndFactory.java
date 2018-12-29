/**
 * Project in java-Android part 2
 * writers: Tirtza Rubinstain and Chana Drori
 * 01/2019
 * BackEndFactory return instance of BackEnd.
 */
package com.project5779.javaproject2.model.backend;

import android.content.Context;

import com.project5779.javaproject2.model.datasource.DataBaseFirebase;

public final class BackEndFactory {

    private static BackEnd instance = null;

    /**
     * get instance - for singleton instance
     * @param context Context
     * @return BackEnd instance
     */
    public static final BackEnd getInstance(Context context) {
        if (instance == null) {
            instance = new DataBaseFirebase();
        }
        return instance;
    }

}
