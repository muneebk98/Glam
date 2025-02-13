package com.example.glamfinal;

public class Review {
    private String phoneno;
    private String username; // Username will be fetched separately
    private int rating;
    private String review;

    public Review() {
        // Default constructor for Firebase DataSnapshot.getValue(Review.class)
    }

    public Review(String phoneno, String username, int rating, String review) {
        this.phoneno = phoneno;
        this.username = username;
        this.rating = rating;
        this.review = review;
    }

    // Getters and setters
    public String getPhoneno() {
        return phoneno;
    }

    public void setPhoneno(String phoneno) {
        this.phoneno = phoneno;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }
}
