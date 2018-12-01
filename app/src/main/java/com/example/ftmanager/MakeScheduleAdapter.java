package com.example.ftmanager;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

public class MakeScheduleAdapter extends RecyclerView.Adapter<MakeScheduleAdapter.MakeScheduleViewHolder> {

    private Context context;
    private List<Schedule> scheduleList;
    private List<String> userList;

    public MakeScheduleAdapter(Context context, List<Schedule> scheduleList, List<String> userList) {
        setHasStableIds(true);
        this.context = context;
        this.scheduleList = scheduleList;
        this.userList = userList;
    }

    public String formatDateMMDDYYYY(String yyyymmdd){
        String [] dateArray = yyyymmdd.split("-");
        return dateArray[1] + "/" + dateArray[2] + "/" + dateArray[0];
    }


    @Override
    public MakeScheduleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(this.context);
        View view = layoutInflater.inflate(R.layout.layout_make_schedule, null);
        return new MakeScheduleViewHolder(view);
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
    public void onBindViewHolder(@NonNull final MakeScheduleViewHolder holder, final int position) {
        final Schedule schedule = scheduleList.get(position);

        holder.dateTV.setText(schedule.getDate());


        holder.employeeOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final PopupMenu popupMenu = new PopupMenu(context, holder.employeeOne);
                popupMenu.getMenuInflater().inflate(R.menu.schedule_popup_menu, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {

                        if(menuItem.getTitle().equals("unassigned")){

                            HashMap<Integer, String> userMap;
                            Bundle data = ((Activity) context).getIntent().getExtras();
                            if(data != null){
                                userMap = (HashMap<Integer,String>) data.getSerializable("userMap");
                            }
                            else {
                                userMap = null;
                            }

                            if(holder.employeeOneTV.getText().toString().equals("unassigned")){
                                //do nothing
                            }
                            else{
                                userList.add(holder.employeeOneTV.getText().toString());
                                holder.employeeOneTV.setText(R.string.unassigned);
                            }
                            /*Intent intent = new Intent(context, ViewReportDetails.class);
                            Bundle b = new Bundle();
                            b.putParcelable("currentReport", report);
                            b.putSerializable("userMap", userMap);
                            intent.putExtras(b);
                            context.startActivity(intent);*/
                        }
                        else{
                            for(String name: userList){
                                if(menuItem.getTitle().equals(name) && !holder.employeeOneTV.getText().toString().equals(R.string.unassigned)){
                                    userList.add(holder.employeeOneTV.getText().toString());
                                    holder.employeeOneTV.setText(name);
                                    userList.remove(name);
                                    break;
                                }
                                else if(menuItem.getTitle().equals(name) && holder.employeeOneTV.getText().toString().equals(R.string.unassigned)){
                                    holder.employeeOneTV.setText(name);
                                    userList.remove(name);
                                    break;

                                }
                            }
                        }
                        /*else if(menuItem.getTitle().equals("Modify Report")){
                            Intent intent = new Intent(context, EditReportActivity.class);
                            Bundle b = new Bundle();
                            b.putParcelable("currentReport", report);
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
                        }*/
                        return true;
                    }
                });
                popupMenu.show();
            }
        });


    }

    @Override
    public int getItemCount() {

        return scheduleList.size();
    }

    public class MakeScheduleViewHolder extends RecyclerView.ViewHolder {
        TextView dateTV, employeeOneTV, employeeTwoTV;
        CardView employeeOne, employeeTwo;

        public MakeScheduleViewHolder(View itemView) {
            super(itemView);
            dateTV = itemView.findViewById(R.id.msDateTV);
            employeeOne = itemView.findViewById(R.id.msEmployee1);
            employeeTwo = itemView.findViewById(R.id.msEmployee2);
            employeeOneTV = itemView.findViewById(R.id.msEmployee1TV);
            employeeTwoTV = itemView.findViewById(R.id.msEmployee2TV);
            /*title = itemView.findViewById(R.id.productTitle);
            amountET = itemView.findViewById(R.id.amountET);
            checkBox = itemView.findViewById(R.id.checkBox);

            amountET.setEnabled(false);
            //stops app from crashing when selecting next edittext
            amountET.setImeOptions(EditorInfo.IME_ACTION_DONE);*/
        }
    }
}
