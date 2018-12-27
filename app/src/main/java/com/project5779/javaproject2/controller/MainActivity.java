package com.project5779.javaproject2.controller;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.project5779.javaproject2.R;

public class MainActivity extends Activity {

    private TextView helloTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();

        String id = getIntent().getStringExtra(String.valueOf(R.string.id));
        
        helloTextView.setText("Hello ");

    }

    private void findViews(){
        helloTextView = (TextView)findViewById(R.id.helloTextView);
    }
}
