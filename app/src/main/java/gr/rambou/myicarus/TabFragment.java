package gr.rambou.myicarus;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class TabFragment extends Fragment {

    public static TabLayout tabLayout;
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    public static ViewPager mViewPager;
    public static int int_items = 3;
    private Icarus mParam1;
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mParam1 = (Icarus) getArguments().getSerializable("myicarus");

        /**
         *Inflate tab_layout and setup Views.
         */
        View x = inflater.inflate(R.layout.tab_layout, null);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());

        // Set up the ViewPager with the sections adapter.
        tabLayout = (TabLayout) x.findViewById(R.id.tabs);
        mViewPager = (ViewPager) x.findViewById(R.id.viewpager);

        /**
         *Set an Apater for the View Pager
         */
        mViewPager.setAdapter(mSectionsPagerAdapter);

        /**
         * Now , this is a workaround ,
         * The setupWithViewPager dose't works without the runnable .
         * Maybe a Support Library Bug .
         */
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(mViewPager);
            }
        });

        return x;
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        /**
         * Return fragment with respect to Position .
         */
        @Override
        public Fragment getItem(int position) {

            Fragment frg = null;
            ArrayList<Lesson> arraylist = null;
            Bundle gradesBundle = new Bundle();
            gradesBundle.putSerializable("myicarus", mParam1);

            switch (position) {
                case 0:
                    frg = new MarksAllFragment();
                    arraylist = mParam1.getAll_Lessons();
                    break;
                case 1:
                    frg = new MarksSemesterFragment();
                    arraylist = mParam1.getExams_Lessons();
                    break;
                case 2:
                    frg = new CoursesPassedFragment();
                    arraylist = mParam1.getSucceed_Lessons();
                    break;
            }

            if (arraylist != null)
                gradesBundle.putSerializable("arraylist", arraylist);
            frg.setArguments(gradesBundle);
            return frg;
        }

        @Override
        public int getCount() {
            return int_items;
        }

        /**
         * This method returns the title of the tab according to the position.
         */
        @Override
        public CharSequence getPageTitle(int position) {

            switch (position) {
                case 0:
                    return getString(R.string.courses_all);
                case 1:
                    return getString(R.string.courses_semester);
                case 2:
                    return getString(R.string.courses_passed);
            }
            return null;
        }
    }

}
