package com.example.ftmanager;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ViewInventoryAdapter extends RecyclerView.Adapter<ViewInventoryAdapter.ViewInventoryViewHolder> {

    private Context context;
    private List<Product> productList;

    public ViewInventoryAdapter(Context context, List<Product> productList) {
        setHasStableIds(true);
        this.context = context;
        this.productList = productList;
    }

    public List<Product> getSelectedItems(){
        List<Product> selected = new ArrayList<Product>();
        for(Product product: productList){
            if(product.isNeeded()){
                selected.add(product);
            }
        }
        return selected;
    }


    @Override
    public ViewInventoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(this.context);
        View view = layoutInflater.inflate(R.layout.layout_products, null);
        return new ViewInventoryViewHolder(view);
    }

    //Stops items from duplicating in recyclerview.
    @Override
    public long getItemId(int position) {
        return position;
    }

    //Stops items from duplicating in recyclerview.
    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewInventoryViewHolder holder, final int position) {
        Product product = productList.get(position);
        holder.titleTV.setText(product.getName());
        holder.quantityTV.setText(product.getQuantity());
        holder.dateTV.setText(product.getDate());

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Date date = new Date();

        if(product.getDate().equals(simpleDateFormat.format(date))){
            holder.dateTV.setTextColor(Color.RED);
        }
    }

    @Override
    public int getItemCount() {

        return productList.size();
    }

    public class ViewInventoryViewHolder extends RecyclerView.ViewHolder {
        TextView titleTV, quantityTV, dateTV;

        public ViewInventoryViewHolder(View itemView) {
            super(itemView);
            titleTV = itemView.findViewById(R.id.invTitleTV);
            quantityTV = itemView.findViewById(R.id.invQuantityTV);
            dateTV = itemView.findViewById(R.id.invDateTV);
        }
    }
}
