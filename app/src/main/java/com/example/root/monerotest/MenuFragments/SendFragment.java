package com.example.root.monerotest.MenuFragments;


import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.root.monerotest.R;



public class SendFragment extends Fragment {

    public native String SendTransfer(String Address, double Amount, int mixin);
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
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.send_fragment, container, false);



    }
}
