package com.example.root.monerotest.InitActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

import com.example.root.monerotest.InitActivity.GenerateWallet.GenerateWalletActivity;
import com.example.root.monerotest.InitActivity.RestoreWallet.RestoreWalletActivity;
import com.example.root.monerotest.MainActivity;
import com.example.root.monerotest.R;

public class InitActivity extends AppCompatActivity implements View.OnClickListener {

    Thread mSplashThread;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.init_activity);
        setListeners();

         mSplashThread = new Thread(){
            @Override
            public void run() {
                try {
                    super.run();
                    sleep(30000000);
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    Intent startActivity = new Intent(InitActivity.this, MainActivity.class);
                    startActivity(startActivity);
                    finish();
                }
            }
        };

        //mSplashThread.start();
    }


    private void setListeners(){

        ImageButton openWallet = (ImageButton) findViewById(R.id.action_open_wallet);
        ImageButton createWallet = (ImageButton) findViewById(R.id.action_create_wallet);
        ImageButton recoverWallet = (ImageButton) findViewById(R.id.action_restore_wallet);



        openWallet.setOnClickListener(this);
        createWallet.setOnClickListener(this);
        recoverWallet.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.action_open_wallet:

                break;

            case R.id.action_create_wallet:
                Intent createWallet = new Intent(this, GenerateWalletActivity.class);
                startActivity(createWallet);
                break;

            case R.id.action_restore_wallet:
                Intent restoreWallet = new Intent(this, RestoreWalletActivity.class);
                startActivity(restoreWallet);
                break;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mSplashThread != null){
            mSplashThread.interrupt();
        }
    }
}
