package com.example.ftmanager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RegisterActivity extends AppCompatActivity {

    private EditText usernameET, passwordET;
    private Spinner positionOptions;
    private List<String> accessList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_register);
        super.onCreate(savedInstanceState);
        usernameET = (EditText)findViewById(R.id.usernameET2);
        passwordET = (EditText)findViewById(R.id.passwordET2);
        accessList = new ArrayList<String>(Arrays.asList("owner", "manager", "cashier"));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, accessList);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        positionOptions = (Spinner)findViewById(R.id.positionSpinner);
        positionOptions.setAdapter(adapter);
        positionOptions.setSelection(2);


    }

    public void register(View view){
        String username = usernameET.getText().toString();
        String password = passwordET.getText().toString();
        String type = positionOptions.getSelectedItem().toString();
        String action = "register";
        DatabaseConnector dbConnect = new DatabaseConnector();
        dbConnect.execute(action, username, password, type);
    }



}
