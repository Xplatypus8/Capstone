package com.example.ftmanager;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainScreenActivity extends AppCompatActivity {
    private User currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        Bundle b = getIntent().getExtras();
        if(b != null){
            currentUser = b.getParcelable("currentUser");
            if(currentUser.getUsername()==null){
            Toast.makeText(getApplicationContext(),"null",Toast.LENGTH_SHORT).show();}
            else{
                Toast.makeText(getApplicationContext(),"succ",Toast.LENGTH_SHORT).show();
            }
        }
        else {
            currentUser = null;
            Toast.makeText(getApplicationContext(),"null2",Toast.LENGTH_SHORT).show();
        }
    }

    public void goToRegister(View view){
        if(currentUser.getPosition().equals("manager") || currentUser.getPosition().equals("owner")) {
            startActivity(new Intent(MainScreenActivity.this, RegisterActivity.class));
        }
        else{
            notAccessible();
        }

    }

    public void goToFinanceReport(View view){
        if(currentUser.getPosition().equals("manager") || currentUser.getPosition().equals("owner")) {
            startActivity(new Intent(MainScreenActivity.this, ReportFinanceActivity.class));
        }
        else{
            notAccessible();
        }

    }

    private void notAccessible(){
        Toast.makeText(getApplicationContext(),"You do not have access to this feature.",Toast.LENGTH_SHORT).show();
    }
}
