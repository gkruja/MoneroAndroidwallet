package com.example.root.monerotest.MenuFragments;


import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.root.monerotest.MainActivity;
import com.example.root.monerotest.R;

/**
 * Andrea Abdelnour
 * MDF III - 0517
 * Java file name:  ReceiveFragment
 * 7/6/17
 */

public class ReceiveFragment extends Fragment {

    public static ReceiveFragment newInstance() {
        return new ReceiveFragment();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.receive_fragment,container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        MainActivity mainActivity = (MainActivity) getActivity();
        LayoutInflater inflater = (LayoutInflater) mainActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View customActionBar = inflater.inflate(R.layout.ab_receive, null);
        mainActivity.setCustomActionBar(customActionBar);
    }
}
