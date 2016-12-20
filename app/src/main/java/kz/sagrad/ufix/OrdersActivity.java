package kz.sagrad.ufix;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;

public class OrdersActivity extends AppCompatActivity {
    OrdersActivity thisActivity = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addNew = new Intent(thisActivity,AddNew.class);
                thisActivity.startActivity(addNew);
            }
        });

        BaseAdapter ordersAdapter = new OrdersAdapter(this);
        UFix.ordersAdapter = ordersAdapter;
        UFix.orderItems = new ArrayList<>();
        ((ListView)findViewById(R.id.ordersListView)).setAdapter(ordersAdapter);
        startReadingOrders();
        grantPermissions();
    }

    public void grantPermissions() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (this.checkSelfPermission(Manifest.permission.CALL_PHONE)
                    == PackageManager.PERMISSION_GRANTED
                    ) {
                //Log.v(TAG,"Permission is granted");
                return;
            } else {
                //Log.v(TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 1);
                return ;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            //Log.v(TAG,"Permission is granted");
            return ;
        }
    }


    private void startReadingOrders() {
        UFix.ref.child("youfix/orders").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                try {
                    OrderItem orderItem = dataSnapshot.getValue(OrderItem.class);
                    UFix.orderItems.add(orderItem);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (UFix.ordersAdapter != null)
                        UFix.ordersAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                try {
                    OrderItem orderItem = dataSnapshot.getValue(OrderItem.class);
                    for (int i = 0; i < UFix.orderItems.size(); i++) {
                        OrderItem item = UFix.orderItems.get(i);
                        if (item.id.equals(orderItem.id))
                            UFix.orderItems.set(i,orderItem);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (UFix.ordersAdapter != null)
                        UFix.ordersAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                try {
                    OrderItem orderItem = dataSnapshot.getValue(OrderItem.class);
                    for (int i = 0; i < UFix.orderItems.size(); i++) {
                        OrderItem item = UFix.orderItems.get(i);
                        if (item.id.equals(orderItem.id)) {
                            UFix.orderItems.remove(i);
                            break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (UFix.ordersAdapter != null)
                        UFix.ordersAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
