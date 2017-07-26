package com.example.root.monerotest.InitActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.root.monerotest.InitActivity.GenerateWallet.GenerateWalletActivity;
import com.example.root.monerotest.InitActivity.RestoreWallet.RestoreWalletActivity;
import com.example.root.monerotest.MainActivity;
import com.example.root.monerotest.R;

public class InitFragment extends Fragment implements View.OnClickListener {


    public static final int OPEN_FILE_CODE = 123;
    public static final String EXTRA_PATH = "Extra_path";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.init_fragment, container, false);

        return v;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setListeners();
    }

    private void setListeners(){
        View v = getView();

        if( v == null)
            return;

        ImageButton openWallet = (ImageButton) v.findViewById(R.id.action_open_wallet);
        ImageButton createWallet = (ImageButton) v.findViewById(R.id.action_create_wallet);
        ImageButton recoverWallet = (ImageButton) v.findViewById(R.id.action_restore_wallet);



        openWallet.setOnClickListener(this);
        createWallet.setOnClickListener(this);
        recoverWallet.setOnClickListener(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == OPEN_FILE_CODE && resultCode == Activity.RESULT_OK){

            Uri uri = null;
            if(data != null){
                uri = data.getData();
                uri.getPath();

                Toast.makeText(getActivity(), "PATH: " + uri.getPath(), Toast.LENGTH_LONG).show();

                showAlertDialog();


//                Intent mainActivity = new Intent(getActivity(), MainActivity.class);
//                mainActivity.putExtra(EXTRA_PATH, uri.getPath());
//                startActivity(mainActivity);

            }
        }
    }

    private void showAlertDialog(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Enter the wallet password");

        // Set up the input
        final EditText input = new EditText(getActivity());

        builder.setView(input);


        builder.setPositiveButton("OK", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String result = input.getText().toString();

                //validate that is the correct password.
                verifyPassword(result);

                dialog.dismiss();
            }
        });

        builder.show();
    }

    private boolean verifyPassword(String _password){
        Toast.makeText(getActivity(), _password, Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.action_open_wallet:

                Intent findFile = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                findFile.setType("*/*");
                startActivityForResult(findFile, OPEN_FILE_CODE);
                break;

            case R.id.action_create_wallet:
                Intent createWallet = new Intent(getActivity(), GenerateWalletActivity.class);
                startActivity(createWallet);
                break;

            case R.id.action_restore_wallet:
                Intent restoreWallet = new Intent(getActivity(), RestoreWalletActivity.class);
                startActivity(restoreWallet);
                break;
        }
    }
}
