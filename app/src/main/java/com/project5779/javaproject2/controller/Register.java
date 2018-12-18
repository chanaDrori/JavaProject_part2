package com.project5779.javaproject2.controller;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;

import com.project5779.javaproject2.R;
import com.project5779.javaproject2.model.backend.BackEnd;
import com.project5779.javaproject2.model.backend.BackEndFactory;
import com.project5779.javaproject2.model.entities.Driver;

public class Register extends Activity {

    private EditText EditTextFirstName;
    private EditText EditTextLastName;
    private EditText EditTextId;
    private EditText EditTextPhoneNum;
    private EditText EditTextEmail;
    private EditText EditTextCredit;
    private EditText EditTextPassword;
    private EditText EditTextConfirmPassword;
    private Button ButtonCreatAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    /**
     * Find the Views in the layout<br />
     * <br />
     * Auto-created on 2018-12-18 15:26:21 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    private void findViews() {
        EditTextFirstName = (EditText)findViewById( R.id.EditTextFirstName );
        EditTextLastName = (EditText)findViewById( R.id.EditTextLastName );
        EditTextId = (EditText)findViewById( R.id.EditTextId );
        EditTextPhoneNum = (EditText)findViewById( R.id.EditTextPhoneNum );
        EditTextEmail = (EditText)findViewById( R.id.EditTextEmail );
        EditTextCredit = (EditText)findViewById( R.id.EditTextCredit );
        EditTextPassword = (EditText)findViewById( R.id.EditTextPassword );
        EditTextConfirmPassword = (EditText)findViewById( R.id.EditTextConfirmPassword );
        ButtonCreatAccount = (Button)findViewById( R.id.Button );

        ButtonCreatAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Driver driver = new Driver();
                driver.setId(EditTextId.getText().toString());
                driver.setName(EditTextFirstName.getText().toString());
                driver.setLastName(EditTextLastName.getText().toString());
                driver.setPhone(EditTextPhoneNum.getText().toString());
                driver.setEmail(EditTextEmail.getText().toString());
                driver.setCreditCard(EditTextCredit.getText().toString());
                driver.setPassword(EditTextPassword.getText().toString());
                BackEndFactory.getInstance(Register.this).addDriver(driver, new BackEnd.Action<String>() {
                    @Override
                    public void onSuccess(String obj) {
                        Toast.makeText(Register.this, R.string.created_successfully, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(Exception exp) {
                        Toast.makeText(Register.this, R.string.error_creating_the_account, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onProgress(String status, double precent) {

                    }
                });

            }
        });
    }

}
