package com.example.ftmanager;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

public class ViewInventoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ViewInventoryAdapter adapter;
    private Spinner locSpinner;
    private ArrayList<String> locationList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_inventory);

        locSpinner = (Spinner)findViewById(R.id.locationSpinnerVI);
        locationList = new ArrayList<String>(Arrays.asList("Select a location", "Garrisonville", "Deacon", "North Carolina"));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, locationList);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locSpinner.setAdapter(adapter);
        locSpinner.setSelection(0);

    }

    public void viewInventory(View view){
        recyclerView = (RecyclerView)findViewById(R.id.recyclerVI);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        DatabaseConnector dbConnector = new DatabaseConnector();
        try {
            ArrayList<Product> productList = new ArrayList<Product>();
            String productValues = dbConnector.execute("view_inventory").get();

            Toast.makeText(getApplicationContext(),productValues,Toast.LENGTH_LONG).show();

            for (String product : productValues.split("@")) {
                productList.add(new Product(product.split(",")));
            }

            adapter = new ViewInventoryAdapter(this, productList, locationList.indexOf(locSpinner.getSelectedItem().toString()));
            recyclerView.setAdapter(adapter);

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
