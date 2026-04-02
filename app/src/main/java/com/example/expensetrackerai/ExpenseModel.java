package com.example.expensetrackerai;

public class ExpenseModel {

        String id, desc, category, date, time;
        int amount;

        public ExpenseModel(){}

        public ExpenseModel(String id, String desc, String category, int amount, String date, String time) {
            this.id = id;
            this.desc = desc;
            this.category = category;
            this.amount = amount;
            this.date = date;
            this.time = time;
        }

        public String getDesc() { return desc; }
        public String getCategory() { return category; }
        public int getAmount() { return amount; }
        public String getDate() {return date;}
        public String getTime() {return time;}
}
