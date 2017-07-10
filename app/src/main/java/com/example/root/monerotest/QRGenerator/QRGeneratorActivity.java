package com.example.root.monerotest.QRGenerator;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.root.monerotest.R;

/**
 * Andrea Abdelnour
 * MDF III - 0517
 * Java file name:  QRGeneratorActivity
 * 7/10/17
 */

public class QRGeneratorActivity extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.qr_generator_activity);


        getFragmentManager().beginTransaction().
                replace(R.id.fragment_container,
                        QRGeneratorFragment.newInstance(),
                        QRGeneratorFragment.TAG)
                .commit();
    }

}
