/**
 * Project in java-Android part 2
 * writers: Tirtza Rubinstain and Chana Drori
 * 01/2019
 * DriveService
 */
package com.project5779.javaproject2.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;


import com.project5779.javaproject2.controller.BroadCastReceiverNotification;
import com.project5779.javaproject2.model.backend.BackEnd;
import com.project5779.javaproject2.model.backend.BackEndFactory;
import com.project5779.javaproject2.model.datasource.DataBaseFirebase;
import com.project5779.javaproject2.model.entities.Drive;
import com.project5779.javaproject2.model.entities.StateOfDrive;

import java.util.List;

public class DriveService extends Service {

    Context context;
    DataBaseFirebase backEndManger;
    Boolean isRun;

    /**
     * onStartCommand of DriveService send intent when add a new drive
     * @param intent Intent
     * @param flags int
     * @param startId int
     * @return START_REDELIVER_INTENT
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        context = getApplicationContext();
        backEndManger = (DataBaseFirebase) BackEndFactory.getInstance(context);
        List<Drive> driveListOld = backEndManger.getListDriveAvailable();
        backEndManger.notifyToDriveList(new DataBaseFirebase.NotifyDataChange<List<Drive>>() {
            @Override
            public void onDataChange(List<Drive> driveList) {
                try {
                    for (Drive d : driveList) {
                        if (d.getState().equals(StateOfDrive.AVAILABLE)) {
                            Intent intent = new Intent(context, BroadCastReceiverNotification.class);
                            sendBroadcast(intent);
                            break;
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void onFailure(Exception exp) {
            }
        });
        return START_REDELIVER_INTENT;
    }

    /**
     * Return the communication channel to the service.  May return null if
     * clients can not bind to the service.  The returned
     * {@link IBinder} is usually for a complex interface
     * that has been <a href="{@docRoot}guide/components/aidl.html">described using
     * aidl</a>.
     * <p>
     * <p><em>Note that unlike other application components, calls on to the
     * IBinder interface returned here may not happen on the main thread
     * of the process</em>.  More information about the main thread can be found in
     * <a href="{@docRoot}guide/topics/fundamentals/processes-and-threads.html">Processes and
     * Threads</a>.</p>
     *
     * @param intent The Intent that was used to bind to this service,
     *               as given to {@link Context#bindService
     *               Context.bindService}.  Note that any extras that were included with
     *               the Intent at that point will <em>not</em> be seen here.
     * @return Return an IBinder through which clients can call on to the
     * service.
     */
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
