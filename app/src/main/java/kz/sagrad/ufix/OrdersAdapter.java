package kz.sagrad.ufix;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Timur_hnimdvi on 20-Dec-16.
 */
public class OrdersAdapter extends BaseAdapter {
    Context context;
    LayoutInflater lInflater;

    public OrdersAdapter(Context context) {
        this.context = context;
        lInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return UFix.orderItems.size();
    }

    @Override
    public OrderItem getItem(int i) {
        return UFix.orderItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, final View convertView, ViewGroup viewGroup) {
        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.order_item, viewGroup, false);
        }

        final OrderItem p = getItem(i);
        if (!p.photos.isEmpty())
        CameraAndPictures.getPicFromFirebase(p.photos.get(0), (ImageView)view.findViewById(R.id.imageCallInListView));
        ((TextView)view.findViewById(R.id.textView1)).setText(p.autoBrand + "," + p.year);
        ((TextView)view.findViewById(R.id.textView2)).setText(p.details);
        ((TextView)view.findViewById(R.id.textView3)).setText(String.valueOf(p.photos.size()));
//        ((TextView)view.findViewById(R.id.textView3)).setText(p.photos.size() + " фото");

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent details = new Intent(context,DetailsActivity.class);
                details.putExtra("id",p.id);
                context.startActivity(details);
            }
        });


//        ((ImageView)view.findViewById(R.id.imageCallInListView)).setOnClickListener(new View.OnClickListener() {
//                                                                                        @Override
//                                                                                        public void onClick(View view) {
//                                                                                            Coupon newCoupon = Coupon.randomCoupon(p);
//                                                                                            //give the coupon to everyone!
//                                                                                            NOTI noti = new NOTI();
//                                                                                            noti.trySendNotification(p.name,newCoupon.couponOffer);
//                                                                                            for (User user : MEGA.users)
//                                                                                                MEGA.ref.child("users").child(user.id).child("coupons").push().setValue(newCoupon);
//                                                                                        }
//                                                                                    }
//        );

        return view;
    }
}