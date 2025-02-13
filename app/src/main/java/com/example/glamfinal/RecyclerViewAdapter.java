package com.example.glamfinal;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private List<Item> items;
    private String userPhoneNo;  // User's phone number passed from the activity

    public RecyclerViewAdapter(List<Item> items, String userPhoneNo) {
        this.items = items;
        this.userPhoneNo = userPhoneNo;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.services_rv, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Item item = items.get(position);
        holder.tvName.setText(item.getName());
        holder.tvInfo.setText(item.getInfo());
        holder.tvPrice.setText(item.getPrice());
        holder.imgAdd.setOnClickListener(v -> addToCart(item));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private void addToCart(Item item) {
        DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference("Cart");
        DatabaseReference newServiceRef = cartRef.child(userPhoneNo).push();
        newServiceRef.setValue(new ServiceDetail(item.getName(), item.getPrice()));
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvName;
        public TextView tvInfo;
        public TextView tvPrice;
        public ImageView imgAdd;

        public ViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            tvInfo = itemView.findViewById(R.id.tv_info);
            tvPrice = itemView.findViewById(R.id.tv_price);
            imgAdd = itemView.findViewById(R.id.imgAdd);
        }
    }

    static class ServiceDetail {
        public String name;
        public String price;

        public ServiceDetail(String name, String price) {
            this.name = name;
            this.price = price;
        }
    }
}
