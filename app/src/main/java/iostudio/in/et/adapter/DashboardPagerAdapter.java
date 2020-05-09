package iostudio.in.et.adapter;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

import iostudio.in.et.fragment.ContactFragment;
import iostudio.in.et.fragment.HomeFragment;
import iostudio.in.et.fragment.MeetingFragment;

/**
 * Created by anantshah on 23/04/18.
 */

public class DashboardPagerAdapter extends FragmentStatePagerAdapter {

    public DashboardPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        if (position == 0) {
            return new ContactFragment();
        }else  if (position == 1) {
            return new HomeFragment();
        } else if (position == 2) {
            return new MeetingFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }

}

