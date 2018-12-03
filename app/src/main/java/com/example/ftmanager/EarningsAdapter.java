package com.example.ftmanager;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class EarningsAdapter extends RecyclerView.Adapter<EarningsAdapter.EarningsViewHolder> {

    private Context context;
    private List<EarningsReport> earningsReportList;

    public EarningsAdapter(Context context, List<EarningsReport> earningsReportList) {
        setHasStableIds(true);
        this.context = context;
        this.earningsReportList = earningsReportList;
    }

    public String formatDateMMDDYYYY(String yyyymmdd){
        String [] dateArray = yyyymmdd.split("-");
        return dateArray[1] + "/" + dateArray[2] + "/" + dateArray[0];
    }


    @Override
    public EarningsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(this.context);
        View view = layoutInflater.inflate(R.layout.layout_earnings, null);
        return new EarningsViewHolder(view);
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
    public void onBindViewHolder(@NonNull final EarningsViewHolder holder, final int position) {
        final EarningsReport report = earningsReportList.get(position);
        final HashMap<Integer, String> userMap;
        final HashMap<String, Location> locationMap;

        Bundle data = ((Activity) context).getIntent().getExtras();
        if(data != null){
            userMap = (HashMap<Integer,String>) data.getSerializable("userMap");
            locationMap = (HashMap<String,Location>) data.getSerializable("locationMap");
        }
        else {
            userMap = null;
            locationMap = null;
        }

        holder.dateTV.setText(formatDateMMDDYYYY(report.getDate()));
        holder.totalTV.setText("Total: " + report.getTotal().toString());
        holder.cashTV.setText("Cash: " + report.getCash().toString());
        holder.creditTV.setText("Credit: " + report.getCredit().toString());

        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final PopupMenu popupMenu = new PopupMenu(context, holder.button);
                popupMenu.getMenuInflater().inflate(R.menu.report_popup_menu, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {

                        if(menuItem.getTitle().equals("Report Details")){


                            Intent intent = new Intent(context, ViewReportDetails.class);
                            Bundle b = new Bundle();
                            b.putParcelable("currentReport", report);
                            b.putSerializable("userMap", userMap);
                            b.putSerializable("locationMap", locationMap);
                            intent.putExtras(b);
                            context.startActivity(intent);
                        }
                        else if(menuItem.getTitle().equals("Modify Report")){
                            Intent intent = new Intent(context, EditReportActivity.class);
                            Bundle b = new Bundle();
                            b.putParcelable("currentReport", report);
                            b.putSerializable("locationMap", locationMap);
                            intent.putExtras(b);
                            context.startActivity(intent);
                        }
                        else if(menuItem.getTitle().equals("Delete")){
                            AlertDialog.Builder verifyDialog = new AlertDialog.Builder(context);
                            verifyDialog.setTitle("Verify Information:")
                                    .setMessage("Are you sure you want to delete this report? This action can't be undone.")
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            DatabaseConnector dbConnect = new DatabaseConnector();
                                            try {
                                                AlertDialog.Builder resultDialog = new AlertDialog.Builder(context);
                                                String data = dbConnect.execute("delete_report", report.getId()+"").get();
                                                if (data.equals("success")){
                                                    resultDialog.setTitle("Status")
                                                            .setMessage("Report has been deleted.")
                                                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                                    //remove data from recyclerview and update
                                                                    earningsReportList.remove(position);
                                                                    notifyItemRemoved(position);
                                                                    notifyItemRangeChanged(position, earningsReportList.size());
                                                                    return;
                                                                }
                                                            }).show();

                                                }
                                                else{
                                                    resultDialog.setTitle("Status")
                                                            .setMessage("Failure. Could not delete report. Please try again.")
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
                        return true;
                    }
                });
                popupMenu.show();
            }
        });


    }

    @Override
    public int getItemCount() {

        return earningsReportList.size();
    }

    public class EarningsViewHolder extends RecyclerView.ViewHolder {
        TextView dateTV, totalTV, cashTV, creditTV;
        ImageButton button;

        public EarningsViewHolder(View itemView) {
            super(itemView);
            dateTV = itemView.findViewById(R.id.dateTV);
            totalTV = itemView.findViewById(R.id.totalTV);
            cashTV = itemView.findViewById(R.id.cashTV);
            creditTV = itemView.findViewById(R.id.creditTV);
            button = itemView.findViewById(R.id.popup);

            /*title = itemView.findViewById(R.id.productTitle);
            amountET = itemView.findViewById(R.id.amountET);
            checkBox = itemView.findViewById(R.id.checkBox);

            amountET.setEnabled(false);
            //stops app from crashing when selecting next edittext
            amountET.setImeOptions(EditorInfo.IME_ACTION_DONE);*/
        }
    }
}
