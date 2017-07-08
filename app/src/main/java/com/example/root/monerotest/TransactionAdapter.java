package com.example.root.monerotest;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.zip.Inflater;

/**
 * Created by Andrea on 6/21/2017.
 */

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
                Transaction insert;

             if(type.equals("in") ) {
                insert  = new Transaction(true, amount, fee, temp.getString("date"), "");
            }else {
                 insert = new Transaction(false, amount, fee, temp.getString("date"), "");
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

        ViewHolder holder;

        if (convertView == null) {

            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.item_history, parent,false);

            holder = new ViewHolder();

            holder.amountTextView = (TextView) convertView.findViewById(R.id.amount_value);
            holder.feeTextView = (TextView) convertView.findViewById(R.id.fee_value);
            holder.dateTextView = (TextView) convertView.findViewById(R.id.date);
            convertView.setTag(holder);
        }
        else {

            holder = (ViewHolder) convertView.getTag();
        }

        Transaction transaction = getItem(position);
        if(transaction.getFee().equals("0.00000"))
        {
            holder.amountTextView.setTextColor(Color.rgb(54,176,91));
            holder.feeTextView.setTextColor(Color.rgb(54,176,91));
        }else{
            holder.amountTextView.setTextColor(Color.rgb(255,79,65));
            holder.feeTextView.setTextColor(Color.rgb(255,79,65));

        }

       holder.amountTextView.setText(transaction.getAmount());
        holder.feeTextView.setText(transaction.getFee());
        holder.dateTextView.setText(transaction.getDate());
        holder.mAmount = transaction.getAmount();
        holder.mFee = transaction.getFee();
        holder.mDateString = transaction.getDate();
        holder.mTimeString = "";


        return convertView;
    }

    static class ViewHolder {
        public TextView amountTextView;
        public TextView feeTextView;
        public TextView dateTextView;
        public String mAmount;
        public String mFee;
        public String mDateString;
        public String mTimeString;
    }

}
