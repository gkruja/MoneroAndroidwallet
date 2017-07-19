package com.example.root.monerotest;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.root.monerotest.QRReader.QRReaderActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.zip.Inflater;



public class TransactionAdapter extends ArrayAdapter {

    private Context mContext;

    private ArrayList<Transaction> mData;

    public TransactionAdapter(@NonNull Context context, @LayoutRes int resource, String jsonString) {
        super(context, resource);
        mContext = context;
        //TODO:Load the Json String to ArrayList<Transactions>
        JSONArray receivedObject = null;

        try{
            receivedObject = new JSONArray(jsonString);
        }catch (JSONException e){e.printStackTrace(); return;}


        //TODO: set mData.
        mData = new ArrayList<>();
            try {
                for (int i = 0; i < receivedObject.length(); i++) {
                JSONObject temp;

                temp =  receivedObject.getJSONObject(i);

                String amount = String.format("%9f",temp.getDouble("amount"));
                String fee = String.format("%.5f",temp.getDouble("fee"));
                String type = temp.getString("type");
                String paymentID = temp.getString("Payment_id");
                int BlockHeight = temp.getInt("blockheight");
                    String TXID = temp.getString("TX");
                Transaction insert;

             if(type.equals("in") ) {

                insert  = new Transaction(true, amount, fee, temp.getString("date"),temp.getString("time"),BlockHeight,TXID,paymentID);
            }else {
                 insert = new Transaction(false, amount, fee, temp.getString("date"), temp.getString("time"),BlockHeight,TXID,paymentID);
            }
            mData.add(insert);
        }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getCount() {
        if(mData != null && mData.size() >= 1){
            return mData.size();
        }
        return 10;
    }
    @Nullable
    @Override
    public Transaction getItem(int position) {
        if(mData != null && mData.size() >= 1){
            return mData.get(position);
        }
        return null;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        final ViewHolder holder;

        if (convertView == null) {

            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.item_transaction, parent,false);

            holder = new ViewHolder();

            holder.amountTextView = (TextView) convertView.findViewById(R.id.amount_value);
            //holder.feeTextView = (TextView) convertView.findViewById(R.id.fee_value);
            holder.dateTextView = (TextView) convertView.findViewById(R.id.date);
            holder.timeTextView = (TextView) convertView.findViewById(R.id.time);

            convertView.setTag(holder);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Transaction details");

                    builder.setNegativeButton("Copy TX ID", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ClipboardManager clipboardManager = (ClipboardManager) getContext().getSystemService(getContext().CLIPBOARD_SERVICE);
                            ClipData clip = ClipData.newPlainText("TX",holder.mTX);
                            clipboardManager.setPrimaryClip(clip);
                        }
                    });

                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    builder.setMessage("TX ID: "+holder.mTX+
                            "\nPayment ID: "+holder.mPaymentID+
                            "\nBlockHeight: "+holder.mBlockHeight+
                            "\nAmount: "+holder.mAmount+
                            "\nFee: "+holder.mFee);
                    AlertDialog alert1 = builder.create();
                    alert1.show();
                }
            });
        }
        else {

            holder = (ViewHolder) convertView.getTag();
        }

        Transaction transaction = getItem(position);
        if(transaction == null)
            return null;

        if(transaction.getFee().equals("0.00000"))
        {
            holder.amountTextView.setTextColor(Color.rgb(54,176,91));
            //holder.feeTextView.setTextColor(Color.rgb(54,176,91));
        }else{
            holder.amountTextView.setTextColor(Color.rgb(255,79,65));
           // holder.feeTextView.setTextColor(Color.rgb(255,79,65));

        }

       holder.amountTextView.setText(transaction.getAmount());
        //holder.feeTextView.setText(transaction.getFee());
        holder.dateTextView.setText(transaction.getDate());
        holder.timeTextView.setText(transaction.getTime());

        holder.mAmount = transaction.getAmount();
        holder.mFee = transaction.getFee();
        holder.mDateString = transaction.getDate();
        holder.mTimeString = transaction.getTime();
        holder.mTX = transaction.getTXid();
        holder.mBlockHeight = String.valueOf(transaction.getBlockheight());
        holder.mPaymentID = transaction.getpaymentID();
        return convertView;
    }

    static class ViewHolder {
        public TextView amountTextView;
        public TextView feeTextView;
        public TextView dateTextView;
        public TextView timeTextView;
        public String mTX;
        public String mPaymentID;
        public String mBlockHeight;
        public String mAmount;
        public String mFee;
        public String mDateString;
        public String mTimeString;
    }

}
