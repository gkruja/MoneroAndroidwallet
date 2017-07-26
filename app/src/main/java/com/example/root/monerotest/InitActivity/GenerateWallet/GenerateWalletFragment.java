package com.example.root.monerotest.InitActivity.GenerateWallet;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.root.monerotest.R;

import java.io.File;

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

                    GenerateWallet(moneroDir.getAbsolutePath(),WalletName.getText().toString(),WalletPassword.getText().toString());


                }




            }
        });
    }
}
