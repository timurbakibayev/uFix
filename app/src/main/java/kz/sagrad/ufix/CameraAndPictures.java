package kz.sagrad.ufix;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.io.File;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;

/**
 * Created by Timur on 29-Jun-16.
 */
public class CameraAndPictures {
    public static Bitmap bitmap;

    public static String savePictureToFirebase() {
        File file = new File(Environment.getExternalStorageDirectory() + File.separator + "image.jpg");
        bitmap = decodeSampledBitmapFromFile(file.getAbsolutePath(), 1000, 400);
        String id = UFix.generateNewId();
        UFix.ref.child("images/" + id).setValue(encodeToBase64(bitmap, Bitmap.CompressFormat.JPEG, 100));
        return id;
    }

    public static void getPicFromFirebase(String picId, final LinearLayout rootView) {
        UFix.ref.child("images/" + picId).
                addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        String gotData = snapshot.getValue(String.class);
                        if (gotData != null) {
                            final Bitmap thePicture = CameraAndPictures.decodeBase64(gotData);
                            final ImageView theImageView = new ImageView(rootView.getContext());
                            theImageView.setImageBitmap(thePicture);
                            theImageView.setMinimumWidth(rootView.getWidth());
                            theImageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
//                            theImageView.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//
//                                    final AlertDialog.Builder builder = new AlertDialog.Builder(rootView.getContext());
//                                    final AlertDialog dialog = builder.create();
//                                    final ScrollView filterScrollView = new ScrollView(rootView.getContext());
//
//                                    ImageView theImageViewBigger = new ImageView(rootView.getContext());
//                                    theImageViewBigger.setImageBitmap(thePicture);
//                                    UFix.recycle.add(thePicture);
//
//                                    filterScrollView.addView(theImageViewBigger);
//
//                                    builder
//                                            //.setMessage(getContext().getString(R.string.enter_amount))
//                                            .setView(filterScrollView)
//                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                                                public void onClick(DialogInterface dialog, int whichButton) {
//
//                                                }
//                                            }).show();
//                                }
//                            });
                            rootView.addView(theImageView);
//                            TextView textView = new TextView(rootView.getContext());
//                            textView.setTextSize(20);
//                            textView.setText(" ");
//                            rootView.addView(textView);
                            //((LinearLayout) rootView.findViewById(R.id.mainLayout)).addView(theImageView);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }


    public static void getPicFromFirebase(String picId, final ImageView imageView) {
        UFix.ref.child("images/" + picId).
                addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        String gotData = snapshot.getValue(String.class);
                        if (gotData != null) {
                            final Bitmap thePicture = CameraAndPictures.decodeBase64(gotData);
                            imageView.setImageBitmap(thePicture);

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    public static Bitmap decodeSampledBitmapFromFile(String path, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        final int height = options.outHeight;
        final int width = options.outWidth;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        int inSampleSize = 1;


        if (height > reqHeight) {
            inSampleSize = Math.round((float) height / (float) reqHeight);
        }
        int expectedWidth = width / inSampleSize;

        if (expectedWidth > reqWidth) {
            inSampleSize = Math.round((float) width / (float) reqWidth);
        }

        options.inSampleSize = inSampleSize;

        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(path, options);
    }

    public static String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality) {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
    }

    public static Bitmap decodeBase64(String input) {
        byte[] decodedBytes = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }
}
