package com.project5779.javaproject2.controller;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;

import com.project5779.javaproject2.R;
import com.project5779.javaproject2.model.backend.BackEnd;
import com.project5779.javaproject2.model.backend.BackEndFactory;
import com.project5779.javaproject2.model.entities.Driver;

import static android.text.TextUtils.isEmpty;

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
        findViews();
    }

    /**
     * Find the Views in the layout
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
        ButtonCreatAccount.setEnabled(false);

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
               // BackEndFactory.getInstance(Register.this).register(driver.getEmail(), driver.getPassword());
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putString(String.valueOf(R.string.Email), driver.getEmail());
                editor.putString(String.valueOf(R.string.password), driver.getPassword());
                editor.putBoolean(getString(R.string.save_Password), true);
                editor.apply();

                finish();
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
                if (EditTextId.getText().toString().trim().length() == 0 ||
                        EditTextFirstName.getText().toString().trim().length() == 0 ||
                        EditTextLastName.getText().toString().trim().length() == 0 ||
                        EditTextEmail.getText().toString().trim().length() == 0 ||
                        EditTextCredit.getText().toString().trim().length() == 0 ||
                        EditTextPassword.getText().toString().trim().length() == 0 ||
                        EditTextConfirmPassword.getText().toString().trim().length() == 0 ||
                        EditTextPhoneNum.getText().toString().trim().length() == 0) {
                    ButtonCreatAccount.setEnabled(false);
                }
                else {
                    ButtonCreatAccount.setEnabled(true);
                }
                //check if string of the phone is valid.
                boolean isValidPhone = true;
                String phone = EditTextPhoneNum.getText().toString();
                if (isEmpty(phone) || phone.length() < 9)
                    isValidPhone = false;
                if (phone.contains("."))
                    isValidPhone = false;

                //check if string of the Email is valid.
                boolean isValidEmail = true;
                String email = EditTextEmail.getText().toString();
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
                Boolean isValidPassword = true;
                String password1 = EditTextPassword.getText().toString();
                String password2 = EditTextConfirmPassword.getText().toString();
                if (password1.trim().length() < 6) {
                    isValidPassword = false;
                } else {
                    if (!password1.equals(password2))
                        isValidPassword = false;
                }
                validationEnable(isValidPassword, EditTextPassword);
                validationEnable(EditTextCredit.getText().toString().length() >= 9, EditTextCredit);
                validationEnable(EditTextId.getText().toString().length() ==9 , EditTextId);
                validationEnable(isValidEmail, EditTextEmail);
                validationEnable(isValidPhone, EditTextPhoneNum);
                validationEnable(isValidPassword, EditTextConfirmPassword);

            }
        };
        EditTextEmail.addTextChangedListener(textWatcher);
        EditTextId.addTextChangedListener(textWatcher);
        EditTextCredit.addTextChangedListener(textWatcher);
        EditTextConfirmPassword.addTextChangedListener(textWatcher);
        EditTextPhoneNum.addTextChangedListener(textWatcher);
        EditTextPassword.addTextChangedListener(textWatcher);
        EditTextFirstName.addTextChangedListener(textWatcher);
        EditTextLastName.addTextChangedListener(textWatcher);
    }

    /**
     * the function show to the user if the input is valid and enabled the add button
     *
     * @param valid    Boolean. describe if the input is valid.
     * @param editText EditText. the source of the input.
     */
    public void validationEnable(Boolean valid, EditText editText) {
        if (!valid) {
            ButtonCreatAccount.setEnabled(false);
            editText.setTextColor(getResources().getColor(R.color.red));
        } else {
            editText.setTextColor(getResources().getColor(R.color.black));
        }
    }

    /**
     * Initialize all the fields
     */
    public void init() {
        EditTextEmail.setText("");
        EditTextId.setText("");
        EditTextCredit.setText("");
        EditTextConfirmPassword.setText("");
        EditTextPhoneNum.setText("");
        EditTextPassword.setText("");
        EditTextFirstName.setText("");
        EditTextLastName.setText("");

    }
}
