package com.example.glamfinal;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    private List<Item> services;
    private OnItemRemoveClickListener listener;

    public interface OnItemRemoveClickListener {
        void onItemRemoveClicked(int position); // Pass the position
    }

    public CartAdapter(List<Item> services, OnItemRemoveClickListener listener) {
        this.services = services;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Item service = services.get(position);
        holder.tvServiceName.setText(service.getName());
        holder.tvServicePrice.setText(service.getPrice());
        holder.imgRemoveService.setOnClickListener(v -> listener.onItemRemoveClicked(position));
    }

    @Override
    public int getItemCount() {
        return services.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvServiceName, tvServicePrice;
        public ImageView imgRemoveService;

        public ViewHolder(View itemView) {
            super(itemView);
            tvServiceName = itemView.findViewById(R.id.tvServiceName);
            tvServicePrice = itemView.findViewById(R.id.tvServicePrice);
            imgRemoveService = itemView.findViewById(R.id.imgRemoveService);
        }
    }
}
