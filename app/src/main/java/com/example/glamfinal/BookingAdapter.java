package com.example.glamfinal;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.ViewHolder> {

    private List<Booking> bookings;
    private Context context;
    private boolean isFromPastBookings; // Flag to check if the adapter is for past bookings

    // Updated constructor to accept the flag
    public BookingAdapter(List<Booking> bookings, Context context, boolean isFromPastBookings) {
        this.bookings = bookings;
        this.context = context;
        this.isFromPastBookings = isFromPastBookings;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_booking, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Booking booking = bookings.get(position);
        holder.tvPhoneNumber.setText("Phone no: " + booking.getPhoneNumber());
        holder.tvServices.setText(booking.getServices());
        holder.tvDate.setText(booking.getDate());
        holder.tvTime.setText(booking.getTime());
        holder.tvTotalPrice.setText(String.format(Locale.US, "Total: $%.2f", booking.getTotalPrice()));
        holder.tvReceiptUrl.setText("View Receipt");
        holder.tvReceiptUrl.setOnClickListener(v -> {
            Uri receiptUri = Uri.parse(booking.getReceiptUrl());
            context.startActivity(new Intent(Intent.ACTION_VIEW, receiptUri));
        });
        holder.tvStatus.setText(booking.getStatus());

        // Set visibility based on the source of the adapter
        if (isFromPastBookings) {
            holder.btnComplete.setVisibility(View.GONE);
            holder.btnCancel.setVisibility(View.GONE);
        } else {
            holder.btnComplete.setVisibility(View.VISIBLE);
            holder.btnCancel.setVisibility(View.VISIBLE);
            holder.btnComplete.setOnClickListener(v -> updateBookingStatus(booking, "complete"));
            holder.btnCancel.setOnClickListener(v -> updateBookingStatus(booking, "cancelled"));
        }
    }

    @Override
    public int getItemCount() {
        return bookings.size();
    }



    private void updateBookingStatus(Booking booking, String status) {
        if (booking == null || booking.getPhoneNumber() == null) {
            Toast.makeText(context, "Number dalo", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseReference pastBookingsRef = FirebaseDatabase.getInstance().getReference("PastBookings")
                .child(booking.getPhoneNumber()).child(booking.getBookingId());
        booking.setStatus(status);
        pastBookingsRef.setValue(booking).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Remove from current bookings
                DatabaseReference currentBookingRef = FirebaseDatabase.getInstance().getReference("Bookings")
                        .child(booking.getPhoneNumber()).child(booking.getBookingId());
                currentBookingRef.removeValue();
            } else {
                // Handle error
            }
        });
    }




    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvServices, tvDate, tvTime, tvTotalPrice, tvReceiptUrl,tvPhoneNumber,tvStatus;
        ImageButton btnComplete, btnCancel;

        public ViewHolder(View itemView) {
            super(itemView);
            tvServices = itemView.findViewById(R.id.tvServices);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvTotalPrice = itemView.findViewById(R.id.tvTotalPrice);
            tvReceiptUrl = itemView.findViewById(R.id.tvReceiptUrl);
            btnComplete = itemView.findViewById(R.id.btnComplete);
            btnCancel = itemView.findViewById(R.id.btnCancel);
            tvPhoneNumber=itemView.findViewById(R.id.tvPhoneNumber);
            tvStatus = itemView.findViewById(R.id.tvStatus);
        }
    }
}
