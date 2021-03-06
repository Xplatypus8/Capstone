package com.example.ftmanager;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;

//This activity allows you to add locations to the database
public class AddBuildingActivity extends AppCompatActivity {

    private EditText buildingName;
    private HashMap<String,Location> locationMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_building);

        buildingName = (EditText)findViewById(R.id.buildingName);

        Bundle b = getIntent().getExtras();
        if(b != null){
            locationMap = (HashMap<String, Location>) b.getSerializable("locationMap");
        }

    }

    public void addBuilding(View view){
        AlertDialog.Builder verifyDialog = new AlertDialog.Builder(this);

        if(buildingName.getText().toString().equals("")){
            verifyDialog.setTitle("Error:");
            verifyDialog.setMessage("Enter a name for the location.");
            verifyDialog.show();
        }
        else if(locationMap.keySet().contains(buildingName.getText().toString())){
            verifyDialog.setTitle("Error:");
            verifyDialog.setMessage("That name is already in use.");
            verifyDialog.show();
        }
        else {
            verifyDialog.setTitle("Verify Information:")
                    .setMessage("Are you sure you want to add this building?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            DatabaseConnector dbConnect = new DatabaseConnector();
                            try {

                                DatabaseConnector dbConnector = new DatabaseConnector();
                                AlertDialog.Builder resultDialog = new AlertDialog.Builder(AddBuildingActivity.this);
                                String result = dbConnect.execute("add_location", buildingName.getText().toString()).get();
                                if (result.equals("success")) {
                                    resultDialog.setTitle("Status")
                                            .setMessage("The building has been added. Restart the app for changes to take effect.")
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
        }
    }
}
