package com.example.root.monerotest.InitActivity.GenerateWallet;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.root.monerotest.InitActivity.InitActivity;
import com.example.root.monerotest.InitActivity.InitFragment;
import com.example.root.monerotest.MainActivity;
import com.example.root.monerotest.R;

import java.io.File;

import static com.example.root.monerotest.InitActivity.InitFragment.EXTRA_PATH;
import static com.example.root.monerotest.MainActivity.FOLDER_NAME;

public class GenerateWalletFragment extends Fragment {


    public native boolean GenerateWallet(String Path,String WalletName,String Password);

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.gen_wallet_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        Button Generate = (Button) getView().findViewById(R.id.button_generate);

        Generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText WalletName = (EditText) getView().findViewById(R.id.WalletName_Value);

                EditText WalletPassword = (EditText) getView().findViewById(R.id.WalletPassword_Value);

                if(WalletName.getText().toString().isEmpty() || WalletPassword.getText().toString().isEmpty())
                {
                    Toast.makeText(getActivity() , "Wallet Name or Wallet Password is empty", Toast.LENGTH_LONG).show();

                }
                else {

                    File externalStorage = Environment.getExternalStorageDirectory();
                    if(externalStorage == null)
                        return;

                    File moneroDir = new File(externalStorage, FOLDER_NAME);



                   boolean status =  GenerateWallet(moneroDir.getAbsolutePath(),WalletName.getText().toString(),WalletPassword.getText().toString());

                    if(status) {
                        Toast.makeText(getActivity() , "Wallet Created", Toast.LENGTH_LONG).show();

                        Intent mainActivity = new Intent(getActivity(), MainActivity.class);
                        mainActivity.putExtra(InitFragment.EXTRA_PATH, "/sdcard/monero/"+WalletName.getText().toString());
                        mainActivity.putExtra("password", WalletPassword.getText().toString());
                        startActivity(mainActivity);
                        getActivity().finish();


                    }else {
                        Toast.makeText(getActivity() , "Wallet Name taken or error creating", Toast.LENGTH_LONG).show();
                    }
                }

            }
        });
    }
}
