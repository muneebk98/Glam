package com.example.glamfinal;
public class Booking {
    private String bookingId;
    private String phoneNumber;
    private String services;
    private String prices;
    private String date;
    private String time;
    private String receiptUrl;
    private String status;
    private double totalPrice;

    public Booking() {
        // Default constructor required for calls to DataSnapshot.getValue(Booking.class)
    }

    public Booking(String bookingId, String phoneNumber, String services, String prices, String date, String time, String receiptUrl, String status, double totalPrice) {
        this.bookingId = bookingId;
        this.phoneNumber = phoneNumber;
        this.services = services;
        this.prices = prices;
        this.date = date;
        this.time = time;
        this.receiptUrl = receiptUrl;
        this.status = status;
        this.totalPrice = totalPrice;
    }

    // Getters and setters
    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getServices() {
        return services;
    }

    public void setServices(String services) {
        this.services = services;
    }

    public String getPrices() {
        return prices;
    }

    public void setPrices(String prices) {
        this.prices = prices;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getReceiptUrl() {
        return receiptUrl;
    }

    public void setReceiptUrl(String receiptUrl) {
        this.receiptUrl = receiptUrl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
