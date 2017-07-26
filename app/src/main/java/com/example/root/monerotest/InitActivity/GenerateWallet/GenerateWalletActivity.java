package com.example.root.monerotest.InitActivity.GenerateWallet;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.root.monerotest.R;

public class GenerateWalletActivity extends AppCompatActivity {
    static {
        System.loadLibrary("c++_shared");
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.gen_wallet_activity);
    }


}
