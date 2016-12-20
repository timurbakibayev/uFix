package kz.sagrad.ufix;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;

public class AddNew extends AppCompatActivity {
    OrderItem newOrderItem = new OrderItem();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendOrder();
            }
        });

        ((Button)findViewById(R.id.form_button_photo)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UFix.photoTakeTo = "NewItemActivity";
                takePhoto();
            }
        });

        ((Button)findViewById(R.id.form_submit)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendOrder();
            }
        });

        if (!UFix.sharedPref.getString("city", "").equals("")) {
            String savedCity = UFix.sharedPref.getString("city", "");
            String[] cities = getResources().getStringArray(R.array.cities_array);
            for (int i = 0; i < cities.length; i++)
                if (cities[i].equals(savedCity))
                    ((Spinner)findViewById(R.id.form_city)).setSelection(i);
        }

        if (!UFix.sharedPref.getString("phone","").equals(""))
            ((EditText)findViewById(R.id.form_phone)).setText(UFix.sharedPref.getString("phone",""));
        if (!UFix.sharedPref.getString("autoBrand","").equals(""))
            ((EditText)findViewById(R.id.form_auto)).setText(UFix.sharedPref.getString("autoBrand",""));
        if (!UFix.sharedPref.getString("year","").equals(""))
            ((EditText)findViewById(R.id.form_year)).setText(UFix.sharedPref.getString("year",""));

        grantPermissions();
    }


    public void grantPermissions() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (this.checkSelfPermission(android.Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED &&
                    this.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_GRANTED
                    ) {
                //Log.v(TAG,"Permission is granted");
                return;
            } else {
                //Log.v(TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return ;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            //Log.v(TAG,"Permission is granted");
            return ;
        }
    }


    static final int REQUEST_IMAGE_CAPTURE = 1;

    private void takePhoto() {
        CameraAndPictures.bitmap = null;
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File file = new File(Environment.getExternalStorageDirectory()+File.separator + "image.jpg");
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            final String id = CameraAndPictures.savePictureToFirebase();
            if (CameraAndPictures.bitmap != null) {
                ImageView newImage = new ImageView(this);
                newImage.setImageBitmap(CameraAndPictures.bitmap);
                ((LinearLayout)findViewById(R.id.photos_ll)).addView(newImage);
                newOrderItem.photos.add(id);
            }
            //TODO: here just add the id to the object of this window
        }
    }

    private void sendOrder() {
        String autoBrand = ((EditText)findViewById(R.id.form_auto)).getText().toString();
        String description = ((EditText)findViewById(R.id.form_description)).getText().toString();
        String phone = ((EditText)findViewById(R.id.form_phone)).getText().toString();
        if (phone.trim().equals("")) {
            Toast.makeText(AddNew.this, "Пожалуйста, введите номер телефона", Toast.LENGTH_SHORT).show();
            return;
        }
        String city = ((Spinner)findViewById(R.id.form_city)).getSelectedItem().toString();
        String year = ((EditText)findViewById(R.id.form_year)).getText().toString();

        UFix.savePref("autoBrand",autoBrand);
        UFix.savePref("year",year);

        newOrderItem.ownerId = UFix.androidID;
        newOrderItem.autoBrand = autoBrand;
        newOrderItem.city = city;
        newOrderItem.year = year;
        newOrderItem.phone = phone;
        newOrderItem.content = autoBrand + ", " + year;
        String details = autoBrand + ", " + year + "\n\n";
        details = details + description;
        newOrderItem.details = details;
        if (!UFix.sharedPref.getString("name","").equals(""))
            newOrderItem.ownerName = UFix.sharedPref.getString("name","");

        UFix.ref.child("youfix/orders/" + newOrderItem.id).setValue(newOrderItem);
        (new AlertDialog.Builder(this)).setMessage("Ваша заявка отправлена")
                //.setMessage(getContext().getString(R.string.enter_amount))
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        finish();
                    }
                }).show();
    }


}
