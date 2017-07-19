package com.example.root.monerotest.QRGenerator;


import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.root.monerotest.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


public class QRGeneratorActivity extends AppCompatActivity{

    public static final int STORAGE_REQ = 0x10;

    private String mFilename;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.qr_generator_activity);


        if(getSupportActionBar() != null){
            //getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.colorPrimary)));
            getSupportActionBar().setTitle(R.string.qr_bitmap);
        }

        getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            QRGeneratorFragment.newInstance(),
                            QRGeneratorFragment.TAG)
                .commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.action_save_to_storage){

            //Check for permissions.
            if(checkPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)){

                //TODO: handle the storage of the bitmap

                //TODO: request the name of the image.

                createAlertDialog();

                return true;
            }else{
                //Request permission if not granted.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        STORAGE_REQ);
            }


            return false;
        }
        return super.onOptionsItemSelected(item);
    }



    private void createAlertDialog(){

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        alertDialog.setTitle("File name");
        alertDialog.setMessage("Enter name bitmap");

        final EditText input = new EditText(this);

        alertDialog.setView(input);

        alertDialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mFilename = input.getText().toString();

                        Bitmap qrBitmap = getQRBitmap();
                        if(qrBitmap == null)
                            return;

                        String path = storeBitmapWithFilename(mFilename, qrBitmap);

                        if(path != null && path.isEmpty())
                            path = "error";

                        Toast.makeText(QRGeneratorActivity.this, path,
                                Toast.LENGTH_SHORT).show();
                    }
                });


        alertDialog.show();
    }


    private Bitmap getQRBitmap(){
        QRGeneratorFragment fragment = (QRGeneratorFragment) getFragmentManager().
                                        findFragmentById(R.id.fragment_container);

        if(fragment == null)
            return null;


        return fragment.getQRBitmap();
    }

    private String storeBitmapWithFilename(String _filename, Bitmap _bitmap){

        String dirname = "QRCodeMonero";
        String extension = ".jpg";
        File externalStorage = Environment.getExternalStorageDirectory();

        if(externalStorage == null)
            return null;


        File QRCodesfolder = new File(externalStorage, dirname);

        if(!QRCodesfolder.mkdirs()){
            Log.d("Debug", "dir already exists");
        }


        File qrBitmap = new File(QRCodesfolder, _filename+extension);
        if(qrBitmap.exists()){
            qrBitmap.delete();
        }

        try (FileOutputStream fos = new FileOutputStream(qrBitmap)){
            _bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            return qrBitmap.getAbsolutePath();
        }catch (IOException e){e.printStackTrace();}

        return null;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == STORAGE_REQ){

            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                //Permissiong granted.
            }else{
                //Permission denied.
            }

        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private static boolean checkPermission(Context _context, String _permission){
        int permissionCheck = ContextCompat.checkSelfPermission(_context, _permission);
        return permissionCheck == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.qr_generator_menu, menu);

        // change color for icon 0
        Drawable yourdrawable = menu.getItem(0).getIcon(); // change 0 with 1,2 ...
        yourdrawable.mutate();
        yourdrawable.setColorFilter(getResources().getColor(R.color.menu_font_color), PorterDuff.Mode.SRC_IN);
        return true;
    }
}
