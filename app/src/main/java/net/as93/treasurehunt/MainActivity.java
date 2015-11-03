package net.as93.treasurehunt;

import android.app.Dialog;
import android.support.v4.app.DialogFragment;

//import android.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.widget.DrawerLayout;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;

import net.as93.treasurehunt.controllers.dialogs.SetUsernameDialog;
import net.as93.treasurehunt.controllers.fragments.CreateHuntFragment;
import net.as93.treasurehunt.controllers.fragments.HomeFragment;
import net.as93.treasurehunt.controllers.fragments.NavigationDrawerFragment;
import net.as93.treasurehunt.controllers.fragments.ViewHuntsFragment;


public class MainActivity extends AppCompatActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    public final static String TYPE_OF_HUNTS = "net.as93.treasurehunt.TYPE_OF_HUNTS";


    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
        mNavigationDrawerFragment.setIcon();
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, new HomeFragment())
                .commit();

        mNavigationDrawerFragment.selectItem(0);

    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        FragmentManager fragmentManager = getSupportFragmentManager();

        switch(position+1) {
            case 1: // The HOme Screen
                fragmentManager.beginTransaction()
                        .replace(R.id.container, new HomeFragment())
                        .commit();
                break;

            case 2: // View My Hunts
                Fragment viewMyHunts = new ViewHuntsFragment();

                Bundle argsMy = new Bundle();
                argsMy.putChar(TYPE_OF_HUNTS, 'm');
                viewMyHunts.setArguments(argsMy);

                fragmentManager.beginTransaction()
                        .replace(R.id.container, viewMyHunts)
                        .commit();
                break;

            case 3: // View All Hunts
                Fragment viewAllHunts = new ViewHuntsFragment();

                Bundle argsAll = new Bundle();
                argsAll.putChar(TYPE_OF_HUNTS, 'a');
                viewAllHunts.setArguments(argsAll);

                fragmentManager.beginTransaction()
                        .replace(R.id.container, viewAllHunts)
                        .commit();
                break;

            case 4: // Create New Hunt
                fragmentManager.beginTransaction()
                        .replace(R.id.container, new CreateHuntFragment())
                        .commit();
                break;

            case 5: // Update Username
                DialogFragment setUsernameDialog = SetUsernameDialog.newInstance(
                        this, "");
                setUsernameDialog.show(this.getSupportFragmentManager(), "");
                break;

            default:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, new HomeFragment())
                        .commit();
                break;
        }

    }


    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {

            DialogFragment setUsernameDialog = SetUsernameDialog.newInstance(
                    MainActivity.this, "");
            setUsernameDialog.show(getSupportFragmentManager(), "");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
