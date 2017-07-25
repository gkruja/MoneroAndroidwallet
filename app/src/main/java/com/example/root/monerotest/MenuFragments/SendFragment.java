package com.example.root.monerotest.MenuFragments;


import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.root.monerotest.MainActivity;
import com.example.root.monerotest.R;



public class SendFragment extends Fragment {

    public static final String TAG = "sendFragment.TAG";

    public static final String EXTRA_ADDRESS = "address";
    public static final String EXTRA_PAYMENT_ID= "payment_id";
    public static final String EXTRA_AMOUNT = "amount";
    public static final String EXTRA_INTEGRATED = "integrated_address";

    public native String SendTransfer(String Address, double Amount, int mixin);

    public native boolean CheckPaymentID(String PaymentID);

    public native boolean CheckAddress(String Address);

    public static SendFragment newInstance() {
        return new SendFragment();
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final View view  = getView();

//        Button Send = (Button) view.findViewById(R.id.button);
//
//        Send.setOnClickListener(new View.OnClickListener(){
//            public void onClick(View v){
//
//                EditText amount = (EditText) view.findViewById(R.id.amount);
//
//                EditText address = (EditText) view.findViewById(R.id.address);
//
//                if(!address.getText().toString().isEmpty() && !amount.getText().toString().isEmpty())
//                {
//                    SendTransfer(address.getText().toString(),Double.parseDouble(amount.getText().toString()),4);
//
//                    Toast.makeText(getActivity(),"Address:"+address.getText().toString(),Toast.LENGTH_LONG).show();
//
//                }else{
//                    Toast.makeText(getActivity(),"Somefeild not filled",Toast.LENGTH_LONG).show();
//
//                }
//            }
//        });
    }

    public boolean areFieldsValid(){

        if(getView() == null)
            return false;

        EditText amountField = (EditText) getView().findViewById(R.id.amountValue);
        EditText addressField = (EditText) getView().findViewById(R.id.addressValue);
        EditText paymentIDField = (EditText) getView().findViewById(R.id.paymentID_edittext);

        EditText descriptionField = (EditText) getView().findViewById(R.id.description);
        //TODO: validate that the fields have the right getText(). return true if so.

        if(addressField.getText().toString().isEmpty() ) {
            //address field empty
            Toast.makeText(getActivity(),"Address not filled",Toast.LENGTH_LONG).show();
            return false;

        }

        if( amountField.getText().toString().isEmpty()){
            // amount field empty
            Toast.makeText(getActivity(),"Amount not filled",Toast.LENGTH_LONG).show();
            return false;
        }

//        if(!paymentIDField.getText().toString().isEmpty()){
//            // payment id field filled check payment id id valid
//            if(!CheckPaymentID(paymentIDField.getText().toString()))
//            {
//                Toast.makeText(getActivity(),"payment id has invalid format, expected 16 or 64 character hex string: "+paymentIDField.getText().toString(),Toast.LENGTH_LONG).show();
//                return false;
//            }
//        }

        if(!CheckAddress(addressField.getText().toString())){
            Toast.makeText(getActivity(),"Address given is not a valid address",Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    public String getAmount(){
        if(getView() == null)
            return null;
        EditText amountField = (EditText) getView().findViewById(R.id.amountValue);
        return amountField.getText().toString();
    }

    public String getAddress(){
        if(getView() == null)
            return null;
        EditText addressField = (EditText) getView().findViewById(R.id.addressValue);
        return addressField.getText().toString();
    }

    public String getPaymentID(){
        if(getView() == null)
            return null;
        EditText paymentIDField = (EditText) getView().findViewById(R.id.paymentID_edittext);
        return paymentIDField.getText().toString();
    }

    @Override
    public void onResume() {
        super.onResume();

        MainActivity activity = (MainActivity) getActivity();

        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View customActionBar = inflater.inflate(R.layout.ab_send, null);

        activity.setCustomActionBar(customActionBar);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.send_fragment, container, false);
    }



    public void setdata(String Address, String PaymentID,double Amount){
        if(getView() == null)
            return;

        EditText editext_address = (EditText) getView().findViewById(R.id.addressValue);

        editext_address.setText(Address);

        EditText editext_payid = (EditText) getView().findViewById(R.id.paymentID_edittext);

        editext_payid.setText(PaymentID);

        EditText amount = (EditText) getView().findViewById(R.id.amountValue);
        amount.setText(Double.toString( Amount));
    }

    public void setdata(String Address, String PaymentID){
        if(getView() == null)
            return;

        EditText editext_address = (EditText) getView().findViewById(R.id.addressValue);

        editext_address.setText(Address);

        EditText editext_payid = (EditText) getView().findViewById(R.id.paymentID_edittext);

        editext_payid.setText(PaymentID);
    }

    public void setdata(String Address, double Amount){
        if(getView() == null)
            return;

        EditText editext_address = (EditText) getView().findViewById(R.id.addressValue);

        editext_address.setText(Address);

        EditText editext_payid = (EditText) getView().findViewById(R.id.paymentID_edittext);

        editext_payid.setText("");

        EditText amount = (EditText) getView().findViewById(R.id.amountValue);
        amount.setText(Double.toString( Amount));
    }

    public void setdata(String Address){

        if(getView() == null)
            return;

        EditText editext_address = (EditText) getView().findViewById(R.id.addressValue);

        editext_address.setText(Address);
        EditText editext_payid = (EditText) getView().findViewById(R.id.paymentID_edittext);

        editext_payid.setText("");

        EditText amount = (EditText) getView().findViewById(R.id.amountValue);
        amount.setText("");
    }
}


