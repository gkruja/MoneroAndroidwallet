package com.example.root.monerotest.MenuFragments;


import android.app.Fragment;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.root.monerotest.MainActivity;
import com.example.root.monerotest.R;



public class ReceiveFragment extends Fragment {

    public static ReceiveFragment newInstance() {
        return new ReceiveFragment();
    }


    public native String GetAddress();
    public native String GeneratePaymentId();
    public native  String GetIntegratedAddress(String PaymentID);


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.receive_fragment,container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final View view = getView();
        if(getView() == null)
            return;

        EditText address = (EditText) getView().findViewById(R.id.addressValue);

        String getAddress = GetAddress();
        address.setKeyListener(null);
        address.setText(getAddress);

        Button Generate = (Button) getView().findViewById(R.id.button_generate);

        Generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText PaymentIDEditText = (EditText) view.findViewById(R.id.paymentID_edittext);

                String PaymentID = GeneratePaymentId();
                PaymentIDEditText.setText(PaymentID);

                TextView IntergratedTextView = (TextView) view.findViewById(R.id.address_integrated_value);

                String integrated = GetIntegratedAddress(PaymentID);

                IntergratedTextView.setText(integrated);

            }
        });

        ImageButton copyIntegratedAddress = (ImageButton) view.findViewById(R.id.button_copy_integrated);

        copyIntegratedAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                ClipboardManager clipboardManager = (ClipboardManager) getActivity().getSystemService(getActivity().CLIPBOARD_SERVICE);

                TextView IntergratedTextView = (TextView) view.findViewById(R.id.address_integrated_value);


                ClipData clip = ClipData.newPlainText("IntegratedAddress",IntergratedTextView.getText());
                clipboardManager.setPrimaryClip(clip);


            }
        });


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
