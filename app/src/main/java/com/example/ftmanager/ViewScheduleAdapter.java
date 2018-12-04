package com.example.ftmanager;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;

//Very similar to MakeScheduleAdapter, except without onclick listeners
public class ViewScheduleAdapter extends RecyclerView.Adapter<ViewScheduleAdapter.ViewScheduleViewHolder> {

    private Context context;
    private List<Schedule> scheduleList;

    public ViewScheduleAdapter(Context context, List<Schedule> scheduleList) {
        setHasStableIds(true);
        this.context = context;
        this.scheduleList = scheduleList;
    }


    @Override
    public ViewScheduleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(this.context);
        View view = layoutInflater.inflate(R.layout.layout_make_schedule, null);
        return new ViewScheduleViewHolder(view);
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
    public void onBindViewHolder(@NonNull final ViewScheduleViewHolder holder, final int position) {
        final Schedule schedule = scheduleList.get(position);


        holder.dateTV.setText(schedule.getDate());
        holder.employeeOneTV.setText(schedule.getEmployeeOne());
        holder.employeeTwoTV.setText(schedule.getEmployeeTwo());
    }

    @Override
    public int getItemCount() {

        return scheduleList.size();
    }

    public class ViewScheduleViewHolder extends RecyclerView.ViewHolder {
        TextView dateTV, employeeOneTV, employeeTwoTV;

        public ViewScheduleViewHolder(View itemView) {
            super(itemView);
            dateTV = itemView.findViewById(R.id.msDateTV);
            employeeOneTV = itemView.findViewById(R.id.msEmployee1TV);
            employeeTwoTV = itemView.findViewById(R.id.msEmployee2TV);

        }


    }
}