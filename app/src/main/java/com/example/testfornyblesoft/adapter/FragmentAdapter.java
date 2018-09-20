package com.example.testfornyblesoft.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.testfornyblesoft.R;
import com.example.testfornyblesoft.fragment.ListFragment;
import com.example.testfornyblesoft.fragment.NavigationFragment;

public class FragmentAdapter extends FragmentStatePagerAdapter {
    private Context context;

    public FragmentAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return NavigationFragment.newInstance();
            case 1:
                return ListFragment.newInstance();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return context.getResources().getString(R.string.navigationFragmentName);
            case 1:
                return context.getResources().getString(R.string.listFragmentName);
            default:
                return super.getPageTitle(position);
        }
    }
}
