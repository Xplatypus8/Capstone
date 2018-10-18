package com.example.ftmanager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class Login extends AppCompatActivity {
    private EditText usernameET, passwordET;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        usernameET = (EditText)findViewById(R.id.username);
        passwordET = (EditText)findViewById(R.id.password);
    }

    public void login(View view){
        String username = usernameET.getText().toString();
        String password = passwordET.getText().toString();
        String type = "login";
        DatabaseConnector dbConnect = new DatabaseConnector(this);
        dbConnect.execute(type, username, password );
    }
}
