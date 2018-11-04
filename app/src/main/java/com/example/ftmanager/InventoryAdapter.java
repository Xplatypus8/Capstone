package com.example.ftmanager;

import android.content.Context;
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

import java.util.ArrayList;
import java.util.List;

public class InventoryAdapter extends RecyclerView.Adapter<InventoryAdapter.InventoryViewHolder> {

    private Context context;
    private List<Product> productList;

    public InventoryAdapter(Context context, List<Product> productList) {
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
    public InventoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(this.context);
        View view = layoutInflater.inflate(R.layout.layout_inventory, null);
        return new InventoryViewHolder(view);
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
    public void onBindViewHolder(@NonNull final InventoryViewHolder holder, final int position) {
        Product product = productList.get(position);
        holder.title.setText(product.getName());
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        holder.amountET.setEnabled(isChecked);
                        productList.get(position).setNeeded(isChecked);

                        //stops app from crashing when selecting next edittext
                        if(isChecked){
                            holder.amountET.setImeOptions(EditorInfo.IME_ACTION_NEXT);
                        }
                        else{
                            holder.amountET.setImeOptions(EditorInfo.IME_ACTION_DONE);
                        }
                    }
                }
        );

        holder.amountET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                productList.get(position).setQuantity(holder.amountET.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public int getItemCount() {

        return productList.size();
    }

    public class InventoryViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        EditText amountET;
        CheckBox checkBox;

        public InventoryViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.productTitle);
            amountET = itemView.findViewById(R.id.amountET);
            checkBox = itemView.findViewById(R.id.checkBox);

            amountET.setEnabled(false);
            //stops app from crashing when selecting next edittext
            amountET.setImeOptions(EditorInfo.IME_ACTION_DONE);
        }
    }
}
