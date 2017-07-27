package com.example.root.monerotest.InitActivity.RestoreWallet;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.root.monerotest.MainActivity;
import com.example.root.monerotest.R;

import java.io.File;

public class RestoreWalletFragment extends Fragment {

    public native boolean GeneratefromMnemonic(String path,String mnemonic,String language,String walletname, String password,boolean testnet);

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.restore_wallet_fragment, container, false);
        return v;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final View view = getView();
        if(view == null)
            return;

        view.findViewById(R.id.button_recover).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText mmonicField = (EditText) view.findViewById(R.id.Recover_WalletMnemonic_Value);
                EditText nameField = (EditText) view.findViewById(R.id.Recover_WalletName_Value);
                EditText passwordField = (EditText) view.findViewById(R.id.Recover_WalletPassword_Value);

                if(mmonicField.getText().toString().isEmpty() ||
                        nameField.getText().toString().isEmpty() ||
                        passwordField.getText().toString().isEmpty()){
                    return;
                }

                File external = Environment.getExternalStorageDirectory();

                File moneroDir = new File(external, MainActivity.FOLDER_NAME);

                String name = nameField.getText().toString();
                String mnimonic = mmonicField.getText().toString();
                String password = passwordField.getText().toString();

                File newWallet = new File(moneroDir, name);

                if(newWallet.exists()){
                    //TODO: wallet already exists with that name
                    Toast.makeText(getActivity(), "Wallet already exists with that name", Toast.LENGTH_SHORT).show();
                    return;
                }

                GeneratefromMnemonic(moneroDir.getAbsolutePath(), mnimonic, "English", name, password, true);

                Toast.makeText(getActivity(), "Wallet restored!", Toast.LENGTH_LONG).show();
                getActivity().finish();
            }
        });
    }
}
