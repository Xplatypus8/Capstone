package com.example.ftmanager;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

public class ViewInventoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Spinner locSpinner;
    private ArrayList<String> locationList;
    private ArrayList<Product> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_inventory);

        productList = new ArrayList<Product>();

        //get inventory data and store it in the database
        viewInventory();

        locSpinner = (Spinner)findViewById(R.id.locationSpinnerVI);
        locationList = new ArrayList<String>(Arrays.asList("Select a location", "Garrisonville", "Deacon", "North Carolina"));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, locationList);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locSpinner.setAdapter(adapter);
        locSpinner.setSelection(0);

        //Data is only pulled from the database once. A spinner listener is used to filter through the data based on location.
        locSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                ArrayList<Product> productsAtLocation = new ArrayList<Product>();
                for(Product product: productList){
                    //creates a list of products needed at a certain location
                    if(product.getLocationID()==i){
                        productsAtLocation.add(product);
                    }
                }

                ViewInventoryAdapter adapter = new ViewInventoryAdapter(ViewInventoryActivity.this, productsAtLocation);
                if(recyclerView.getAdapter() == null) {
                    recyclerView.setAdapter(adapter);
                }
                else{
                    recyclerView.swapAdapter(adapter, true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //do nothing
            }
        });

    }

    public void viewInventory(){
        recyclerView = (RecyclerView)findViewById(R.id.recyclerVI);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        DatabaseConnector dbConnector = new DatabaseConnector();
        try {
            String productValues = dbConnector.execute("view_inventory").get();
            if(!productValues.equals("failure")) {
                for (String product : productValues.split("@")) {
                    productList.add(new Product(product.split(",")));
                }
            }


        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}