package com.example.root.monerotest;


import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Andrea Abdelnour
 * MDF III - 0517
 * Java file name:  DashboardFragment
 * 6/15/17
 */

public class DashboardFragment extends Fragment {


    public static DashboardFragment newInstance() {
        return new DashboardFragment();
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dashboard_fragment, container, false);
    }
}
