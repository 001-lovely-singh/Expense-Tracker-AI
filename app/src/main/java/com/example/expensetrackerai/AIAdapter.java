package com.example.expensetrackerai;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AIAdapter extends RecyclerView.Adapter<AIAdapter.ViewHolder> {

    ArrayList<AIModel> list;

    public AIAdapter(ArrayList<AIModel> list) {
        this.list = list;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDesc, tvHighlight;
        ImageView imgIcon;

        public ViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDesc = itemView.findViewById(R.id.tvDesc);
            tvHighlight = itemView.findViewById(R.id.tvHighlight);
            imgIcon = itemView.findViewById(R.id.imgIcon);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_ai, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        AIModel model = list.get(position);

        holder.tvTitle.setText(model.title);
        holder.tvDesc.setText(model.desc);
        holder.tvHighlight.setText(model.highlight);
        holder.imgIcon.setImageResource(model.icon);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
