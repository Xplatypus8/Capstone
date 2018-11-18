package com.example.ftmanager;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
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
import java.util.concurrent.ExecutionException;

public class RegisterActivity extends AppCompatActivity {

    private EditText firstNameET, lastNameET, usernameET, passwordET;
    private Spinner positionOptions;
    private List<String> accessList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_register);
        super.onCreate(savedInstanceState);
        firstNameET = (EditText)findViewById(R.id.firstNameET);
        lastNameET = (EditText)findViewById(R.id.lastNameET);
        usernameET = (EditText)findViewById(R.id.usernameET2);
        passwordET = (EditText)findViewById(R.id.passwordET2);
        accessList = new ArrayList<String>(Arrays.asList("Select a position","owner", "manager", "cashier"));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, accessList);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        positionOptions = (Spinner)findViewById(R.id.positionSpinner);
        positionOptions.setAdapter(adapter);
        positionOptions.setSelection(0);


    }

    public void register(View view) {
        DatabaseConnector dbConnect = new DatabaseConnector();
        android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(this).create();

        final String firstName = firstNameET.getText().toString();
        final String lastName = lastNameET.getText().toString();
        final String username = usernameET.getText().toString();
        final String password = passwordET.getText().toString();
        final String position = positionOptions.getSelectedItem().toString();
        final String type = "register";

        if (positionOptions.getSelectedItem().toString().equals("Select a position")
                || firstName.equals("")
                || lastName.equals("")
                || username.equals("")
                || password.equals("")) {
            alertDialog.setTitle("Error:");
            alertDialog.setMessage("Please fill out all fields.");
            alertDialog.show();
        } else {


            ///////////////////////////////////////////////
            AlertDialog.Builder verifyDialog = new AlertDialog.Builder(this);
            verifyDialog.setTitle("Verify Information:")
                    .setMessage("Are you sure this information is correct?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            DatabaseConnector dbConnect = new DatabaseConnector();
                            try {

                                DatabaseConnector dbConnector = new DatabaseConnector();
                                AlertDialog.Builder resultDialog = new AlertDialog.Builder(RegisterActivity.this);
                                String result = dbConnect.execute(type, username, password, position, firstName, lastName).get();
                                if (result.equals("success")) {
                                    resultDialog.setTitle("Status")
                                            .setMessage("The user has been registered!")
                                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    finish();
                                                }
                                            }).show();

                                } else {
                                    resultDialog.setTitle("Status")
                                            .setMessage("Failure. Try again.")
                                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    //Do nothing
                                                }
                                            }).show();
                                }
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            }
                        }
                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    //Do nothing
                }
            }).show();
            //////////////////////////////////////////////
        }
    }



}
