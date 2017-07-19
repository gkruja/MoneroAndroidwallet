package com.example.root.monerotest;


import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class DashboardFragment extends Fragment {


    @Override
    public void onResume() {
        super.onResume();
        setActionBar();
        //reloadTransactions();
    }


    public void reloadTransactions(){

        //TODO: native call for check if wallet2 is not null (is available)

        
        try {
            JSONObject localData = new JSONObject(Transfers());
            if(localData.length() > 0 && getView() != null){
                ListView history = (ListView) getView().findViewById(R.id.listView1);
                TransactionAdapter adapter = new TransactionAdapter(getActivity(),
                        R.layout.item_transaction, localData.toString());

                history.setAdapter(adapter);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void setActionBar(){
        MainActivity activity = (MainActivity) getActivity();

        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View customActionBar = inflater.inflate(R.layout.ab_main, null);

        activity.setCustomActionBar(customActionBar);
    }
    public void setData(){
        View view  = getView();

        if(view != null){
            //TextView connected = (TextView) view.findViewById(R.id.textView7);

            TextView Balance = (TextView) view.findViewById(R.id.textView11);
            TextView unLockedBalance = (TextView) view.findViewById(R.id.textView10);


            boolean con = CheckConnection();
            if(!con)
            {
                //connected.setText("DISCONNECTED!!!");
                //connected.setTextColor(Color.RED);
            }
            else {
                Balance.setText(String.format("%.3f",Balance())+"\tXMR");
                unLockedBalance.setText(String.format("%.3f",UnlockedBalance())+"\tXMR");

                String transfers = Transfers();

                if(transfers.equals("[]")){
                    return;
                }

                ListView Histroy = (ListView) view.findViewById(R.id.listView1);

                final TransactionAdapter adapter = new TransactionAdapter(getActivity(),
                        R.layout.item_transaction, transfers);

                Histroy.setAdapter(adapter);


            }
        }
    }


    public static DashboardFragment newInstance() {
        return new DashboardFragment();
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dashboard_fragment, container, false);

    }

    public native boolean CheckConnection();
    public native double Balance();
    public native double UnlockedBalance();
    public native String Transfers();
}
