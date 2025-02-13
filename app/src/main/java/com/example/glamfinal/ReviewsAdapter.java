package com.example.glamfinal;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewViewHolder> {

    private List<Review> reviews = new ArrayList<>();

    public ReviewsAdapter(List<Review> initialReviews) {
        this.reviews = initialReviews;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_review, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        Review review = reviews.get(position);
        holder.tvUsername.setText(review.getUsername());
        holder.tvReview.setText(review.getReview());
        holder.ratingBar.setRating((float) review.getRating());
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public void addReview(Review review) {
        reviews.add(review);
        notifyItemInserted(reviews.size() - 1);
    }

    public static class ReviewViewHolder extends RecyclerView.ViewHolder {
        TextView tvUsername, tvReview;
        RatingBar ratingBar;

        public ReviewViewHolder(View itemView) {
            super(itemView);
            tvUsername = itemView.findViewById(R.id.tvUserName);
            tvReview = itemView.findViewById(R.id.tvReviewText);
            ratingBar = itemView.findViewById(R.id.ratingBar);
        }
    }
}
