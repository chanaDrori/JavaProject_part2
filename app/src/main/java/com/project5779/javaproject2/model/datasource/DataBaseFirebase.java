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

import java.util.Date;
import java.util.List;

public class DataBaseFirebase implements BackEnd {
    private static DatabaseReference DriverRef;
    private static DatabaseReference DriveRef;
   // private
    static {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DriverRef = database.getReference("Drivers");
        DriveRef = database.getReference("Drive");
    }

    @Override
    public List<String> getListNamesDrivers() {
        return null;
    }

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

    @Override
    public List<Drive> getListDriveAvailable() {
        Query query = DriveRef.orderByChild("state").equalTo("AVAILABLE");
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return ((List<Drive>) query);
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
