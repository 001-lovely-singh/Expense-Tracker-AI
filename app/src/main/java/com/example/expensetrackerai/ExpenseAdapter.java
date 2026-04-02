package com.example.expensetrackerai;

import android.view.*;
import android.widget.*;
import androidx.recyclerview.widget.RecyclerView;
import java.util.*;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ViewHolder> {

    ArrayList<ExpenseModel> list;

    public ExpenseAdapter(ArrayList<ExpenseModel> list){
        this.list = list;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView desc, category, amount, tvDate, tvTime;

        public ViewHolder(View itemView) {
            super(itemView);
            desc = itemView.findViewById(R.id.tvDesc);
            category = itemView.findViewById(R.id.tvCategory);
            amount = itemView.findViewById(R.id.tvAmount);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvTime = itemView.findViewById(R.id.tvTime);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.expense_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ExpenseModel model = list.get(position);
        holder.desc.setText(model.getDesc());
        holder.category.setText(model.getCategory());
        holder.amount.setText("-" + model.getAmount());
        holder.tvDate.setText(model.getDate());
        holder.tvTime.setText(model.getTime());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
