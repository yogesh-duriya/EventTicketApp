package com.endive.eventplanner.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.endive.eventplanner.fragments.BrowseCategoriesFragment;
import com.endive.eventplanner.fragments.SomethingInterestingFragment;

/**
 * Created by Arpit on 17-Feb-16.
 */
public class HomePagerAdapter extends FragmentPagerAdapter {

    public HomePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int index) {

        switch (index) {
            case 0:
                return new BrowseCategoriesFragment();
            case 1:
                return new SomethingInterestingFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return 2;
    }
}
