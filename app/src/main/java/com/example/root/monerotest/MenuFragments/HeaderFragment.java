package com.example.root.monerotest.MenuFragments;


import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.root.monerotest.R;

/**
 * Andrea Abdelnour
 * MDF III - 0517
 * Java file name:  HeaderFragment
 * 6/15/17
 */

public class HeaderFragment extends Fragment {


    public static HeaderFragment newInstance() {
        return new HeaderFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.drawer_header_fragment, container, false);
    }
}
