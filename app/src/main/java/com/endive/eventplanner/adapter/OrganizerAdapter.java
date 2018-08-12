package com.endive.eventplanner.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.endive.eventplanner.fragments.CurrentFragment;
import com.endive.eventplanner.fragments.PrevFragment;
import com.endive.eventplanner.fragments.UpcomingFragment;

/**
 * Created by upasna.mishra on 10/30/2017.
 */

public class OrganizerAdapter extends FragmentPagerAdapter {

    private int mCurrentPosition = -1;

    public OrganizerAdapter(FragmentManager fm) {
        super(fm);
    }



    @Override
    public Fragment getItem(int index) {

        switch (index) {
            case 0:
                return new CurrentFragment();
            case 1:
                return new UpcomingFragment();
            case 2:
                return new PrevFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return 3;
    }
}
