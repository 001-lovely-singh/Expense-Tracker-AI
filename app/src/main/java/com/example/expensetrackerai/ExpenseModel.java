package com.example.expensetrackerai;

public class ExpenseModel {

    private String id;
    private String note;
    private String category;
    private String date;
    private String time;
    private int amount;

    // 🔹 Empty constructor (Firebase ke liye mandatory)
    public ExpenseModel() {
    }

    // 🔹 Full constructor
    public ExpenseModel(String id, String note, String category, int amount, String date, String time) {
        this.id = id;
        this.note = note;
        this.category = category;
        this.amount = amount;
        this.date = date;
        this.time = time;
    }

    // 🔹 Getters
    public String getId() {
        return id;
    }

    public String getnote() {
        return note;
    }

    public String getCategory() {
        return category;
    }

    public int getAmount() {
        return amount;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    // 🔹 (Optional but useful) Setters
    public void setId(String id) {
        this.id = id;
    }

    public void setnote(String note) {
        this.note = note;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }
}