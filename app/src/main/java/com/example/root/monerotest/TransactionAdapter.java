package com.example.root.monerotest;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

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

        //TODO: set mData.
    }

    @Override
    public int getCount() {
        if(mData != null && mData.size() > 1){
            return mData.size();
        }
        return 10;
    }
    @Nullable
    @Override
    public Transaction getItem(int position) {
        if(mData != null && mData.size() > 1){
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
            convertView = inflater.inflate(R.layout.item_history, null);

            holder = new ViewHolder();
            //holder.nameTextView = (TextView) convertView.findViewById(R.id.person_name);
            //.surnameTextView = (TextView) convertView.findViewById(R.id.person_surname);
            //holder.personImageView = (ImageView) convertView.findViewById(R.id.person_image);
            //convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        //Transaction transaction = getItem(position);

        //holder.mAmount = "asd";
        //holder.mFee = "asd";
        //holder.mDateString = "asd";
        //holder.mTimeString = "Asd";

        return convertView;
    }

    static class ViewHolder {
        public String mAmount;
        public String mFee;
        public String mDateString;
        public String mTimeString;
    }

}
