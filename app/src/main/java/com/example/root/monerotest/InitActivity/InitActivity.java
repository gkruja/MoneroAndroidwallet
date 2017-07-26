package com.example.root.monerotest.InitActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageButton;
import com.example.root.monerotest.MainActivity;
import com.example.root.monerotest.R;

public class InitActivity extends AppCompatActivity {

    static {
        System.loadLibrary("c++_shared");
        System.loadLibrary("native-lib");
    }
    Thread mSplashThread;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.init_activity);


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

    @Override
    protected void onStop() {
        super.onStop();
        if(mSplashThread != null){
            mSplashThread.interrupt();
        }
    }
}
