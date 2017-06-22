package com.example.root.monerotest;

/**
 * Created by Andrea on 6/21/2017.
 */

public class Transaction {

    private boolean isSending;

    private String amount;

    private String fee;
    private String date;
    private String time;


    public Transaction(boolean isSending, String amount, String fee, String date, String time){
        this.isSending = isSending;
        this.amount = amount;
        this.fee = fee;
        this.date = date;
        this.time = time;
    }

    public boolean getIsSending(){
        return isSending;
    }

    public String getAmount() {
        return amount;
    }

    public String getFee() {
        return fee;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }
}
