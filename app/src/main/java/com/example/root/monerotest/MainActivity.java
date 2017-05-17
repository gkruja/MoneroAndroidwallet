package com.example.root.monerotest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'native-lib' library on application startup.
    static {

        System.loadLibrary("c++_shared");
        System.loadLibrary("openssl_crypto");
        System.loadLibrary("ssl");
        System.loadLibrary("crypto");
        System.loadLibrary("lmdb");

        System.loadLibrary("unbound");
        System.loadLibrary("blockchain_db");
        System.loadLibrary("cryptonote_core");
        System.loadLibrary("cryptonote_protocol");
        System.loadLibrary("cryptonote_basic");
        System.loadLibrary("daemonizer");
        System.loadLibrary("mnemonics");
        System.loadLibrary("common");
        System.loadLibrary("ringct");
        System.loadLibrary("p2p");
        System.loadLibrary("rpc");
        System.loadLibrary("wallet");



        System.loadLibrary("easylogging");

        System.loadLibrary("native-lib");





    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Example of a call to a native method
        TextView tv = (TextView) findViewById(R.id.sample_text);
        tv.setText(stringFromJNI());
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();
}
