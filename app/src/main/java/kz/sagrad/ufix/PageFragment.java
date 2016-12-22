package kz.sagrad.ufix;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by adik on 12/22/16.
 */

public class PageFragment extends Fragment {
    public static final String IMAGE = "ARG_PAGE";
    public static final String CURRENT = "CURRENT";
    public static final String SIZE = "SIZE";
    private String mPage;

    public static PageFragment newInstance(String page, int current, int size) {
        Bundle args = new Bundle();
        args.putString(IMAGE, page);
        args.putInt(CURRENT, current);
        args.putInt(SIZE, size);
        PageFragment pageFragment = new PageFragment();
        pageFragment.setArguments(args);
        return pageFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getString(IMAGE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_page, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageView imageView = (ImageView) view.findViewById(R.id.page);
        TextView pageCount = (TextView) view.findViewById(R.id.page_count);
        mPage = getArguments().getString(IMAGE);
        pageCount.setText((getArguments().getInt(CURRENT) + 1) + "/" + getArguments().getInt(SIZE));
        if (mPage.equalsIgnoreCase("no photo")) {
            imageView.setImageResource(R.drawable.tire_wrench);
        } else {
            CameraAndPictures.getPicFromFirebase(mPage, imageView);
        }
    }
}
