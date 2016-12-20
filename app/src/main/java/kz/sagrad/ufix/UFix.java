package kz.sagrad.ufix;

import android.app.Application;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.widget.BaseAdapter;

import com.google.firebase.database.DatabaseReference;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;

/**
 * Created by Timur_hnimdvi on 20-Dec-16.
 */
public class UFix extends Application {
    public static ArrayList<Bitmap> recycle = new ArrayList<>();
    public static BaseAdapter ordersAdapter;
    public static ArrayList<OrderItem> orderItems = new ArrayList<>();
    public static DatabaseReference ref;
    public static String photoTakeTo = "";
    public static SharedPreferences sharedPref;
    public static String TAG = "UFix";
    public static String androidID = "";
    public static String generateNewId() {
        SecureRandom random = new SecureRandom();
        return(new BigInteger(64, random).toString(32));
    }

    public static void savePref(String key, String value) {
        SharedPreferences.Editor prefsEditor = sharedPref.edit();
        prefsEditor.putString(key, value);
        prefsEditor.commit();
    }

    public static void recycleBitmaps() {
        for (Bitmap bitmap : recycle) {
            if (bitmap != null && !bitmap.isRecycled())
                bitmap.recycle();
        }
        recycle = new ArrayList<>();
    }
}
