package com.example.farmia_mcc;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class Viewpager_Adapter extends FragmentPagerAdapter {

    private Context context;
    int total_tabs;

    public Viewpager_Adapter(@NonNull FragmentManager fm, Context context, int total_tabs) {
        super(fm);
        this.context = context;
        this.total_tabs = total_tabs;
    }

    @Override
    public int getCount() {
        return total_tabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                Signuptab_fragment signuptab_fragment = new Signuptab_fragment();
                return signuptab_fragment;
            case 1:
                Logintab_fragment logintab_fragment = new Logintab_fragment();
                return logintab_fragment;
            default:
                return null;
        }
    }

}
