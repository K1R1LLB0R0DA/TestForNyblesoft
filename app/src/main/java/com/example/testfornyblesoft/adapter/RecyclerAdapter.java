package com.example.testfornyblesoft.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.testfornyblesoft.R;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    private List<SavedData> savedData;

    private OnItemClickListener onItemClickListener;

    public RecyclerAdapter(List<SavedData> savedData, OnItemClickListener onItemClickListener) {
        this.savedData = savedData;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(savedData.get(position));
    }

    @Override
    public int getItemCount() {
        return savedData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvItemDate, tvItemLocation;

        public ViewHolder(View itemView) {
            super(itemView);
            tvItemDate = itemView.findViewById(R.id.tvItemDate);
            tvItemLocation = itemView.findViewById(R.id.tvItemLocation);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(getLayoutPosition());
                }
            });
        }

        public void bind(SavedData savedData) {
            tvItemDate.setText(savedData.getDate().toString());
            tvItemLocation.setText(String.valueOf(savedData.getLat()));
            tvItemLocation.append(" ");
            tvItemLocation.append(String.valueOf(savedData.getLon()));
            tvItemLocation.append(", ");
            tvItemLocation.append(String.valueOf(savedData.getCity()));
        }
    }
}
