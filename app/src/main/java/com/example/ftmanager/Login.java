package com.example.ftmanager;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.util.concurrent.ExecutionException;

public class Login extends AppCompatActivity {
    AlertDialog alertDialog;
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
        DatabaseConnector dbConnect = new DatabaseConnector();

        try {
            if(dbConnect.execute(type, username, password ).get().equals("login success")){
                startActivity(new Intent(Login.this, MainScreenActivity.class));
                usernameET.setText("");
                passwordET.setText("");
            }
            else{
                alertDialog = new AlertDialog.Builder(this).create();
                alertDialog.setTitle("Login Status");
                alertDialog.setMessage("Incorrect username or passsword.");
                alertDialog.show();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }
}
