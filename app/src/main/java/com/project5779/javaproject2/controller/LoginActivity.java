/**
 * Project in java-Android part 2
 * writers: Tirtza Rubinstain and Chana Drori
 * 01/2019
 * Login Activity code.
 */
package com.project5779.javaproject2.controller;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.project5779.javaproject2.model.datasource.DataBaseFirebase;
import com.project5779.javaproject2.model.entities.Driver;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends Activity {

    private List<Driver> driverList;

    //private LinearLayout loginLayout;
    private EditText Email;
    private EditText password;
    private CheckBox CheckBoxRememberMe;
    private Button ButtonLogin;
    private TextView createAccount;
    private ImageView eyePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findViews();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (sharedPreferences.getBoolean(getString(R.string.save_Password), false)) {
            Email.setText(sharedPreferences.getString(String.valueOf(R.string.Email), null));
            password.setText(sharedPreferences.getString(String.valueOf(R.string.password), null));
            CheckBoxRememberMe.setChecked(true);
        }

        DataBaseFirebase.notifyToDriverList(new DataBaseFirebase.NotifyDataChange<List<Driver>>() {
            @Override
            public void onDataChange(List<Driver> obj) {
                driverList = obj;
            }

            @Override
            public void onFailure(Exception exp) {
                Toast.makeText(getBaseContext(), getString(R.string.Error_to_get_drivers_list)
                        + exp.toString() +"\n login", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        DataBaseFirebase.stopNotifyToDriverList();
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
            @Override
            public void onClick(View v) {
                Driver user = null;
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

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra(getString(R.string.id), user.getId());
                    startActivity(intent);
                } else {
                    Toast.makeText(getBaseContext(), R.string.not_found_user, Toast.LENGTH_LONG).show();
                }
            }
        });

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, Register.class);
                startActivity(intent);
            }
        });

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        Email.addTextChangedListener(textWatcher);
        password.addTextChangedListener(textWatcher);

        eyePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (password.getInputType() ==  (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
                    password.setInputType( InputType.TYPE_CLASS_TEXT );
                } else {
                    password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });
    }

}
