package com.example.root.monerotest.QRReader;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.root.monerotest.R;

public class QRReaderFragment extends Fragment {


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.qr_reader_fragment, container, false);
        //TODO: here you set up any listeners to the views in the layout


        return v;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(getView() != null){
            View v = getView();
            //TODO: here you can call findViewWithId and get the view u need from the qr_reder_fragment layout

        }
    }
}
