package com.example.root.monerotest;


import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.text.Html;
import android.text.Spannable;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Andrea Abdelnour
 * MDF III - 0517
 * Java file name:  DashboardFragment
 * 6/15/17
 */

public class DashboardFragment extends Fragment {


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        View view  = getView();
//
//        if(view != null){
//            TextView connected = (TextView) view.findViewById(R.id.textView7);
//
//            TextView Balance = (TextView) view.findViewById(R.id.textView11);
//            TextView unLockedBalance = (TextView) view.findViewById(R.id.textView10);
//
//
//            boolean con = CheckConnection();
//            if(!con)
//            {
//                connected.setText("DISCONNECTED!!!");
//                connected.setTextColor(Color.RED);
//            }
//            else {
//                Balance.setText(String.format("%.3f",Balance())+"\tXMR");
//                unLockedBalance.setText(String.format("%.3f",UnlockedBalance())+"\tXMR");
//
//                String transfers = Transfers();
//
//                ListView Histroy = (ListView) view.findViewById(R.id.listView1);
//
//
//                TransactionAdapter adapter = new TransactionAdapter(getActivity(),
//                                            R.layout.item_history, transfers);
//
//                Histroy.setAdapter(adapter);
//            }
//        }
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
