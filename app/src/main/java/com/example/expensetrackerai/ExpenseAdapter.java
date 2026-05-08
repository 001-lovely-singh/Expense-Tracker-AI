package com.example.expensetrackerai;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ViewHolder> {

    ArrayList<ExpenseModel> list;

    public ExpenseAdapter(ArrayList<ExpenseModel> list) {
        this.list = list;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle, tvDate, tvAmount;
        ImageView imgCategory;

        public ViewHolder(View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDate = itemView.findViewById(R.id.tvSub);
            tvAmount = itemView.findViewById(R.id.tvAmount);
            imgCategory = itemView.findViewById(R.id.imgCategory);
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

        // category title
        holder.tvTitle.setText(model.getCategory());

        // date + time show
        holder.tvDate.setText(
                model.getDate() + ", " + model.getTime()
        );

        // amount
        holder.tvAmount.setText("-₹" + model.getAmount());

        // category icon
        if (model.getCategory().equals("Food")) {

            holder.imgCategory.setImageResource(R.drawable.burger__1_);

        } else if (model.getCategory().equals("Travel")) {

            holder.imgCategory.setImageResource(R.drawable.airplane__1_);

        } else if (model.getCategory().equals("Shopping")) {

            holder.imgCategory.setImageResource(R.drawable.shopping_cart__1_);

        } else if (model.getCategory().equals("Bills")) {

            holder.imgCategory.setImageResource(R.drawable.bills_1);

        } else {

            holder.imgCategory.setImageResource(R.drawable.ic_light);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}