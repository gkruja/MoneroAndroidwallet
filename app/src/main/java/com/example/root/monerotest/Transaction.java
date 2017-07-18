package com.example.root.monerotest;


public class Transaction {

    private boolean isSending;

    private String amount;

    private String fee;
    private String date;
    private String time;
    private String paymentID;
    private String txid;
    private int BH;

    public Transaction(boolean isSending, String amount, String fee, String date, String time, int BlockHeight , String TXid,String PaymentID){
        this.isSending = isSending;
        this.amount = amount;
        this.fee = fee;
        this.date = date;
        this.time = time;
        this.BH = BlockHeight;
        this.txid = TXid;
        this.paymentID = PaymentID;
    }

    public boolean getIsSending(){
        return isSending;
    }

    public String getAmount() {
        return amount;
    }

    public String getTXid() {
        return txid;
    }

    public int getBlockheight() {
        return BH;
    }

    public String getpaymentID() {return paymentID;}

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
