/**
 * Project in java-Android part 2
 * writers: Tirtza Rubinstain and Chana Drori
 * 01/2019
 * Login Activity code.
 * First activity login for the user.
 * If not exist account possible to register
 */
package com.project5779.javaproject2.controller;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.project5779.javaproject2.R;
import com.project5779.javaproject2.model.backend.BackEndFactory;
import com.project5779.javaproject2.model.datasource.DataBaseFirebase;
import com.project5779.javaproject2.model.entities.Drive;
import com.project5779.javaproject2.model.entities.Driver;

import java.util.ArrayList;
import java.util.List;

import static android.graphics.drawable.Drawable.*;
import static android.text.TextUtils.isEmpty;

/**
 * Login Activity
 *  First activity login for the user.
 * If not exist account possible to register
 */
public class LoginActivity extends Activity {

    private EditText Email;
    private EditText password;
    private CheckBox CheckBoxRememberMe;
    private Button ButtonLogin;
    private TextView createAccount;
    private ImageView eyePassword;

    /**
     *  onCreate function
     *  load the user detail if is exist in the device.
     * @param savedInstanceState Bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findViews();

        //SharedPreferences load the user detail
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (sharedPreferences.getBoolean(getString(R.string.save_Password), false)) {
            Email.setText(sharedPreferences.getString(String.valueOf(R.string.Email), null));
            password.setText(sharedPreferences.getString(String.valueOf(R.string.password), null));
            CheckBoxRememberMe.setChecked(true);
        }

        BackEndFactory.getInstance(getApplicationContext()).notifyToDriverList(new DataBaseFirebase.NotifyDataChange<List<Driver>>() {
            /**
             * onDataChange function. Work when the data change.
             * @param obj List<Driver>.
             */
            @Override
            public void onDataChange(List<Driver> obj) {
                BackEndFactory.getInstance(getApplicationContext()).setDriverList(obj);
            }

            /**
             * onFailure function. work when failure notify
             * @param exp exception
             */
            @Override
            public void onFailure(Exception exp) {
                Toast.makeText(getBaseContext(), getString(R.string.Error_to_get_drivers_list), Toast.LENGTH_LONG).show();
            }
        });

        BackEndFactory.getInstance(getApplicationContext()).notifyToDriveList(new DataBaseFirebase.NotifyDataChange<List<Drive>>() {
            /**
             * onDataChange function. Work when the data change.
             * @param obj List<Drive>.
             */
            @Override
            public void onDataChange(List<Drive> obj) {
                BackEndFactory.getInstance(getApplicationContext()).setDriveList(obj);
            }

            /**
             * onFailure function. work when failure notify
             * @param exp exception
             */
            @Override
            public void onFailure(Exception exp) {
                Toast.makeText(getBaseContext(), getString(R.string.Error_to_get_drives_list), Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * onDestroy function. Sent to  DataBaseFirebase.stopNotifyToDriverList();
     */
    @Override
    protected void onDestroy() {
        //BackEndFactory.getInstance(getApplicationContext()).stopNotifyToDriveList();
        //BackEndFactory.getInstance(getApplicationContext()).stopNotifyToDriverList();
        super.onDestroy();
    }

    /**
     * Find the Views in the layout<br />
     * <br />
     * Auto-created on 2018-12-18 12:59:03 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    private void findViews() {

        Email = (EditText)findViewById( R.id.EditTextEmail );
        password = (EditText)findViewById( R.id.password );
        CheckBoxRememberMe = (CheckBox)findViewById( R.id.CheckBoxRememberMe );
        ButtonLogin = (Button)findViewById( R.id.ButtonLogin );
        createAccount = (TextView)findViewById( R.id.createAccount );
        eyePassword = (ImageView)findViewById(R.id.eyePassword);

        ButtonLogin.setOnClickListener(new View.OnClickListener() {
            /**
             * on Click function. sign in to the app.
             * @param v View
             */
            @Override
            public void onClick(View v) {
                Driver user = null;
                List<Driver> driverList =  BackEndFactory.getInstance(getBaseContext()).getDriverList();
                List<Drive> driveDelete = BackEndFactory.getInstance(getBaseContext()).getDriveList();
                for (Driver driver : driverList) {
                    if (driver.getEmail().equals(Email.getText().toString())
                            && driver.getPassword().equals(password.getText().toString())) {
                        user = driver;
                        break;
                    }
                }
                if (user != null) {
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    editor.putString(String.valueOf(R.string.Email), Email.getText().toString());
                    editor.putString(String.valueOf(R.string.password), password.getText().toString());
                    editor.putBoolean(getString(R.string.save_Password), CheckBoxRememberMe.isChecked());
                    editor.apply();

                    Intent intent = new Intent(LoginActivity.this, Nav_drawer.class);
                    intent.putExtra(getString(R.string.id), user.getId());
                    startActivity(intent);
                } else {
                    //there is problem with the input.
                    Toast.makeText(getBaseContext(), R.string.not_found_user, Toast.LENGTH_LONG).show();
                }
            }
        });

        createAccount.setOnClickListener(new View.OnClickListener() {
            /**
             * onClick function. moving to the register activity.
             * @param v
             */
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, Register.class);
                startActivity(intent);
            }
        });

        //check the validation of the input when the text change
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //check if string of the Email is valid.
                boolean isValidEmail = true;
                String email = Email.getText().toString();
                if (isEmpty(email) || email.length() < 5)
                    isValidEmail = false;
                int atSign = email.indexOf('@');
                if (atSign == -1 || atSign != email.lastIndexOf('@') ||
                        atSign == 0 || atSign == email.length() - 1 || email.contains("\""))
                    isValidEmail = false;
                int dotSign = email.indexOf('.', atSign);
                if (dotSign == -1 || dotSign == 0 || dotSign == email.length() - 1
                        || dotSign - atSign < 2)
                    isValidEmail = false;

                if (!isValidEmail) {
                    ButtonLogin.setEnabled(false);
                    Email.setTextColor(getResources().getColor(R.color.red));
                } else {
                    Email.setTextColor(getResources().getColor(R.color.white));
                    ButtonLogin.setEnabled(true);
                }
            }
        };
        Email.addTextChangedListener(textWatcher);
        password.addTextChangedListener(textWatcher);

        eyePassword.setOnClickListener(new View.OnClickListener() {
            /**
             * onClick function. show the password or write points.
             * @param v View
             */
            @Override
            public void onClick(View v) {
                if (password.getInputType() ==  (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
                    password.setInputType( InputType.TYPE_CLASS_TEXT );
                    eyePassword.setImageResource(R.drawable.eye_off_yellow_icon);
                } else {
                    password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    eyePassword.setImageResource(R.drawable.eye_yellow_icon);
                }
            }
        });
    }

}
