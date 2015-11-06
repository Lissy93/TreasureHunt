package net.as93.treasurehunt.controllers;

import java.util.Locale;

import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import net.as93.treasurehunt.R;
import net.as93.treasurehunt.controllers.dialogs.SetUsernameDialog;
import net.as93.treasurehunt.controllers.fragments.HuntSummaryFragment;
import net.as93.treasurehunt.controllers.fragments.ViewHuntLegsListFragment;
import net.as93.treasurehunt.controllers.fragments.ViewHuntLegsMapFragment;

public class ViewHunt extends AppCompatActivity {

    SectionsPagerAdapter mSectionsPagerAdapter;
    int numLocations = 0;
    String huntName;
    String creator;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_hunt);

        // Create the adapter that will return a fragment for each primary sections
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        huntName = getIntent().getExtras().getString("huntname");
        creator = getIntent().getExtras().getString("creator");

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar.
        getMenuInflater().inflate(R.menu.menu_view_hunt, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            DialogFragment setUsernameDialog = SetUsernameDialog.newInstance(
                    ViewHunt.this, "");
            setUsernameDialog.show(getSupportFragmentManager(), "");
        }

        return super.onOptionsItemSelected(item);
    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment
            switch(position){
                case 0: return  HuntSummaryFragment.newInstance(position + 1);

                case 1: return  ViewHuntLegsListFragment.newInstance(position + 1);

                case 2: return  ViewHuntLegsMapFragment.newInstance(position + 1);

                default: return  HuntSummaryFragment.newInstance(position + 1);
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_section1).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section2).toUpperCase(l);
                case 2:
                    return getString(R.string.title_section3).toUpperCase(l);
            }
            return null;
        }
    }

    public int getNumLocations() {
        return numLocations;
    }

    public void setNumLocations(int numLocations) {
        this.numLocations = numLocations;
    }

    public String getHuntName() {
        return huntName;
    }

    public String getCreator() {
        return creator;
    }
}
