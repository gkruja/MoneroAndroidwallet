package com.example.root.monerotest.QRGenerator;


import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.root.monerotest.R;
import com.google.zxing.qrcode.encoder.QRCode.*;

import net.glxn.qrgen.android.QRCode;

/**
 * Andrea Abdelnour
 * MDF III - 0517
 * Java file name:  QRGeneratorFragment
 * 7/10/17
 */

public class QRGeneratorFragment extends Fragment {

    public static final String TAG = "QRGeneratorFragment.TAG";

    public static QRGeneratorFragment newInstance() {
        return new QRGeneratorFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.qr_generator_fragment, container, false);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View views = getView();
        if(views == null)
            return;


        Bitmap QRImage = QRCode.from("asdasdasd").bitmap();

        ((ImageView) views.findViewById(R.id.imageview_qr)).setImageBitmap(QRImage);
    }
}
