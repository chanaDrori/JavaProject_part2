package com.project5779.javaproject2.controller;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Button;

import com.project5779.javaproject2.R;

public class Register extends Activity {

    private EditText EditTextFirstName;
    private EditText EditTextLastName;
    private EditText EditTextId;
    private EditText EditTextPhoneNum;
    private EditText EditTextEmail;
    private EditText EditTextCredit;
    private EditText EditTextPassword;
    private EditText EditTextConfirmPassword;
    private Button Button;

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
        Button = (Button)findViewById( R.id.Button );
    }

}
