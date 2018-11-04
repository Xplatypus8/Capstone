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
        intent.putExtras(b);
        startActivity(intent);

    }

    public void goToInventoryReport(View view){
        Intent intent = new Intent(MainScreenActivity.this, ReportInventoryActivity.class);
        Bundle b = new Bundle();
        b.putParcelable("currentUser", currentUser);
        intent.putExtras(b);
        startActivity(intent);

    }

    private void notAccessible(){
        Toast.makeText(getApplicationContext(),"You do not have access to this feature.",Toast.LENGTH_SHORT).show();
    }
}
