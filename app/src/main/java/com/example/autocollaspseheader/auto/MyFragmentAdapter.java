package com.example.autocollaspseheader.auto;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


public class MyFragmentAdapter extends FragmentPagerAdapter {
    private String[] mTitle = {"this is one", "this is two", "this is three", "this is four"};


    public MyFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        CommonFragment fragment = new CommonFragment();
        fragment.setText(mTitle[i]);
        return fragment;
    }

    @Override
    public int getCount() {
        return mTitle.length;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mTitle[position];
    }
}
