package com.example.ftmanager;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Parcel;
import android.provider.SyncStateContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class Login extends AppCompatActivity {
    AlertDialog alertDialog;
    private EditText usernameET, passwordET;
    private List<String> accessList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        usernameET = (EditText)findViewById(R.id.username);
        passwordET = (EditText)findViewById(R.id.password);
        accessList = new ArrayList<String>(Arrays.asList("owner", "manager", "cashier"));
    }

    public void login(View view){
        String username = usernameET.getText().toString();
        String password = passwordET.getText().toString();
        String type = "login";
        DatabaseConnector dbConnect = new DatabaseConnector();

        try {
            String data = dbConnect.execute(type, username, password ).get();
            User currentUser = new User(1, data.split(",",0));
            if(accessList.contains(data)){
                Intent intent = new Intent(Login.this, MainScreenActivity.class);
                Bundle b = new Bundle();
                b.putString("userPosition", data);
                intent.putExtras(b);
                startActivity(intent);
                usernameET.setText("");
                passwordET.setText("");
            }
            else{
                Toast.makeText(getApplicationContext(),data,Toast.LENGTH_SHORT).show();
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
