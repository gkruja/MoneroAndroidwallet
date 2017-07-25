package com.example.root.monerotest.QRGenerator;


import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.root.monerotest.R;
import com.google.zxing.qrcode.QRCodeReader;
import com.google.zxing.qrcode.encoder.QRCode.*;

import net.glxn.qrgen.android.QRCode;



public class QRGeneratorFragment extends Fragment {

    static {
        System.loadLibrary("c++_shared");
        System.loadLibrary("native-lib");
    }

    public static final String TAG = "QRGeneratorFragment.TAG";
    private Bitmap mQRImage;

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
        if (views == null)
            return;


        DisplayMetrics metrics = getResources().getDisplayMetrics();


        String address = GetAddress();
        String PaymentId = GeneratePaymentId();
        float amount =0;

        if(amount !=0 && PaymentId != null) {
             mQRImage = QRCode.from("monero:" + address + "?tx_amount="+Float.toString(amount)+"&tx_payment_id="+PaymentId).bitmap();
        }
        else if(PaymentId != null){
            mQRImage =  QRCode.from("monero:" + address + "?tx_payment_id="+PaymentId).bitmap();
        }
        else if(amount != 0){
            mQRImage = QRCode.from("monero:" + address + "?tx_amount="+Float.toString(amount)).bitmap();
        }
        else {
            mQRImage = QRCode.from("monero:" + address ).bitmap();
        }

        ((ImageView) views.findViewById(R.id.imageview_qr)).setImageBitmap(mQRImage);

    }

    public Bitmap getQRBitmap(){
        if(mQRImage != null)
            return mQRImage;

        return null;
    }

    public native String GeneratePaymentId();

    public native String GetAddress();
}
