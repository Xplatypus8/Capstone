package com.example.ftmanager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class RegisterActivity extends AppCompatActivity {

    private EditText usernameET, passwordET, typeET;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_register);
        super.onCreate(savedInstanceState);
        usernameET = (EditText)findViewById(R.id.usernameET2);
        passwordET = (EditText)findViewById(R.id.passwordET2);
        typeET = (EditText)findViewById(R.id.typeET2);



    }

    public void register(View view){
        String username = usernameET.getText().toString();
        String password = passwordET.getText().toString();
        String type = typeET.getText().toString();
        String action = "register";
        DatabaseConnector dbConnect = new DatabaseConnector();
        dbConnect.execute(action, username, password, type);
    }



}
