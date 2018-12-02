package com.example.ftmanager;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class MainScreenActivity extends AppCompatActivity {
    private User currentUser;
    private HashMap<Integer, String> userMap;
    private HashMap<String, Location> locationMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        userMap = createUserMap();
        locationMap = createLocationMap();

        Bundle b = getIntent().getExtras();
        if(b != null){
            currentUser = b.getParcelable("currentUser");
        }
        else {
            currentUser = null;
        }
    }

    public void goToRegister(View view){
        if(currentUser.getPosition().equals("manager") || currentUser.getPosition().equals("owner")) {
            Intent intent = new Intent(MainScreenActivity.this, RegisterActivity.class);
            Bundle b = new Bundle();
            b.putParcelable("currentUser", currentUser);
            b.putSerializable("userMap", userMap);
            b.putSerializable("locationMap", locationMap);
            intent.putExtras(b);
            startActivity(intent);
        }
        else{
            notAccessible();
        }

    }

    public void goToFinanceReport(View view){
        Intent intent = new Intent(MainScreenActivity.this, ReportFinanceActivity.class);
        Bundle b = new Bundle();
        b.putParcelable("currentUser", currentUser);
        b.putSerializable("locationMap", locationMap);
        intent.putExtras(b);
        startActivity(intent);

    }

    public void goToInventoryReport(View view){
        Intent intent = new Intent(MainScreenActivity.this, ReportInventoryActivity.class);
        Bundle b = new Bundle();
        b.putParcelable("currentUser", currentUser);
        b.putSerializable("locationMap", locationMap);
        intent.putExtras(b);
        startActivity(intent);

    }

    public void goToViewEarnings(View view){
        Intent intent = new Intent(MainScreenActivity.this, ViewEarningsActivity.class);
        Bundle b = new Bundle();
        b.putParcelable("currentUser", currentUser);
        b.putSerializable("locationMap", locationMap);
        b.putSerializable("userMap", userMap);
        intent.putExtras(b);
        startActivity(intent);
    }

    public void goToViewInventory(View view){
        Intent intent = new Intent(MainScreenActivity.this, ViewInventoryActivity.class);
        Bundle b = new Bundle();
        b.putParcelable("currentUser", currentUser);
        b.putSerializable("locationMap", locationMap);
        b.putSerializable("userMap", userMap);
        intent.putExtras(b);
        startActivity(intent);
    }

    public void goToViewSchedule(View view){
        Intent intent = new Intent(MainScreenActivity.this, ViewScheduleActivity.class);
        Bundle b = new Bundle();
        b.putParcelable("currentUser", currentUser);
        b.putSerializable("locationMap", locationMap);
        b.putSerializable("userMap", userMap);
        intent.putExtras(b);
        startActivity(intent);
    }

    public void goToMakeSchedule(View view){
        Intent intent = new Intent(MainScreenActivity.this, MakeScheduleActivity.class);
        Bundle b = new Bundle();
        b.putParcelable("currentUser", currentUser);
        b.putSerializable("locationMap", locationMap);
        b.putSerializable("userMap", userMap);
        intent.putExtras(b);
        startActivity(intent);
    }

    public void goToGraph(View view){
        Intent intent = new Intent(MainScreenActivity.this, GraphActivity.class);
        Bundle b = new Bundle();
        b.putParcelable("currentUser", currentUser);
        b.putSerializable("locationMap", locationMap);
        intent.putExtras(b);
        startActivity(intent);
    }

    public void goToAddLocation(View view){
        Intent intent = new Intent(MainScreenActivity.this, AddBuildingActivity.class);
        Bundle b = new Bundle();
        b.putParcelable("currentUser", currentUser);
        b.putSerializable("locationMap", locationMap);
        intent.putExtras(b);
        startActivity(intent);
    }

    private void notAccessible(){
        Toast.makeText(getApplicationContext(),"You do not have access to this feature.",Toast.LENGTH_SHORT).show();
    }

    private HashMap<Integer, String> createUserMap(){
        HashMap<Integer, String> userMap = new HashMap<Integer, String>();

        DatabaseConnector dbConnector = new DatabaseConnector();

        try {
            String result = dbConnector.execute("get_users").get();

            for(String userInfo: result.split("@")){
                User user = new User(userInfo.split(","));
                userMap.put(new Integer(user.getUserID()), user.getName());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return userMap;
    }

    private HashMap<String, Location> createLocationMap(){
        HashMap<String, Location> locationMap = new HashMap<String, Location>();

        DatabaseConnector dbConnector = new DatabaseConnector();

        try {
            String result = dbConnector.execute("get_locations").get();

            for(String userInfo: result.split("@")){
                Location location = new Location(userInfo.split(","));
                locationMap.put(location.getName(), location);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return locationMap;
    }
}
