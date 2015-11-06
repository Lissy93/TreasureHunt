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
import net.as93.treasurehunt.models.Username;

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

        // Get the huntname and the creator from bundle
        huntName = getIntent().getExtras().getString("huntname");
        creator = getIntent().getExtras().getString("creator");

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_view_hunt, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
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
            switch(position){
                case 0: return  HuntSummaryFragment.newInstance(position + 1);

                case 1: return  ViewHuntLegsListFragment.newInstance(position + 1);

                case 2: return  ViewHuntLegsMapFragment.newInstance(position + 1);

                default: return  HuntSummaryFragment.newInstance(position + 1);
            }
        }

        @Override
        public int getCount() {
            return 3; // How many pages to d
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


    /**
     * Determines if the currently logged in user is the creator of this hunt
     * @return Boolean true if they are the creator, else false
     */
    public boolean isUserTheCreator(){
        Username username = new Username(this);
        return username.fetchUsername().equals(this.creator);
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
