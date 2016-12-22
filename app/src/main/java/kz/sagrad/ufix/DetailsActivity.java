package kz.sagrad.ufix;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;

public class DetailsActivity extends AppCompatActivity {
    public static String TAG = "DetailsActivity";
    OrderItem orderItem;
    DetailsActivity thisActivity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        String id = getIntent().getExtras().getString("id");
        for (OrderItem item : UFix.orderItems) {
            if (item.id.equals(id))
                orderItem = item;
        }
        if (orderItem == null)
            return;
//        for (String photoID : orderItem.photos)
        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        ArrayList<String> items = (orderItem.photos.size() > 0) ?
                orderItem.photos : null;

        ImageView photosLL = (ImageView) findViewById(R.id.photos_ll);
        if (orderItem.photos.size() > 0) {
            CameraAndPictures.getPicFromFirebase(orderItem.photos.get(0), photosLL);
        } else {
            viewPager.setAdapter(new ImageScrollFragmentAdapter(getSupportFragmentManager(), items));
            photosLL.setImageResource(R.drawable.tire_wrench);
        }
        viewPager.setAdapter(new ImageScrollFragmentAdapter(getSupportFragmentManager(),
                items));
        viewPager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(thisActivity, PhotoMasterActivity.class);
                intent.putExtra("PHOTOS", orderItem.photos);
                startActivity(intent);
//                startActivity(new Intent(thisActivity, PhotoMasterActivity.class));
//                Toast.makeText(thisActivity, "Photo is clicked", Toast.LENGTH_SHORT).show();
            }
        });

        //orderItem
        ((TextView) findViewById(R.id.form_name1)).setText(orderItem.ownerName);
        ((TextView) findViewById(R.id.form_auto1)).setText(orderItem.autoBrand + " , " + orderItem.year);
        ((TextView) findViewById(R.id.form_description1)).setText(orderItem.details);
        ((TextView) findViewById(R.id.form_phone1)).setText(orderItem.phone);
        findViewById(R.id.form_phone1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uri = "tel:" + orderItem.phone;
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse(uri));
                if (ActivityCompat.checkSelfPermission(thisActivity, android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                    startActivity(intent);
                }
            }
        });
        ((TextView) findViewById(R.id.form_city1)).setText(orderItem.city);

        (findViewById(R.id.button_make_offer)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeAnOffer(thisActivity, (LinearLayout) findViewById(R.id.priceLL));
            }
        });
        readOffers();
    }

    private void readOffers() {
        UFix.ref.child("youfix/offers/" + orderItem.id).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                try {
                    Offer offer = dataSnapshot.getValue(Offer.class);
                    addOffer(offer, (LinearLayout) findViewById(R.id.priceLL));
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void makeAnOffer(final Context context, final LinearLayout priceLL) {

        final String phone = UFix.sharedPref.getString("phone", "");
        final String name = UFix.sharedPref.getString("name", "");
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final ScrollView filterScrollView = new ScrollView(context);
        final LinearLayout ll = new LinearLayout(context);
        ll.setOrientation(LinearLayout.VERTICAL);
        filterScrollView.addView(ll);

        final EditText priceEditText = new EditText(context);
        priceEditText.setRawInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED);
        priceEditText.setHint("Цена в тенге");

        final EditText phoneEditText = new EditText(context);
        phoneEditText.setRawInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED);
        phoneEditText.setHint("Ваш номер телефона");
        phoneEditText.setText(phone);

        final EditText commentEditText = new EditText(context);
        commentEditText.setHint("Комментарий");

        ll.addView(priceEditText);
        ll.addView(phoneEditText);
        ll.addView(commentEditText);

        builder
                .setMessage("Сделайте своё предложение по ремонту")
                .setView(filterScrollView)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        if (!("" + phoneEditText.getText()).trim().equals(""))
                            UFix.savePref("phone", "" + phoneEditText.getText());
                        Offer offer = new Offer();
                        offer.price = "" + priceEditText.getText();
                        offer.phone = UFix.sharedPref.getString("phone", "");
                        offer.comment = "" + commentEditText.getText();
                        offer.email = UFix.sharedPref.getString("email", "");
                        offer.name = UFix.sharedPref.getString("name", "");
                        UFix.ref.child("youfix/offers/" + orderItem.id + "/").push().setValue(offer);
                    }
                }).show();
    }

    public void addOffer(final Offer offer, LinearLayout oll) {
        LayoutInflater li = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        FrameLayout frameLayout = (FrameLayout) li.inflate(R.layout.bubble, null);

        TextView anotherOffer = (TextView) frameLayout.findViewById(R.id.textInBubble);
        anotherOffer.setText(offer.name + ":" + offer.price + " тенге\n" + offer.comment);

        ImageView call = (ImageView) frameLayout.findViewById(R.id.user_img);

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uri = "tel:" + offer.phone;
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse(uri));
                if (ActivityCompat.checkSelfPermission(thisActivity, android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                    startActivity(intent);
                }
            }
        });
        oll.addView(frameLayout);
    }
    public void showMaster(View view){
        Intent intent = new Intent(thisActivity, PhotoMasterActivity.class);
        intent.putExtra("PHOTOS", orderItem.photos);
        startActivity(intent);
    }
}
