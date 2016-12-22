package kz.sagrad.ufix;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by adik on 12/22/16.
 */

public class ImageScrollFragmentAdapter extends FragmentPagerAdapter {

    private Context mContext;
    ArrayList<String> list;

    public ImageScrollFragmentAdapter(FragmentManager fm, ArrayList<String> list) {
        super(fm);
        this.list = list;
    }

    @Override
    public Fragment getItem(int position) {
        if (list == null) return PageFragment.newInstance("no photo", 0, 0);
        return PageFragment.newInstance(list.get(position), position, list.size());
    }

    @Override
    public int getCount() {
        if (list == null) return 0;
        return list.size();
    }

//    @Override
//    public CharSequence getPageTitle(int position) {
//        return TAB_TITLES[position];
//    }
}
