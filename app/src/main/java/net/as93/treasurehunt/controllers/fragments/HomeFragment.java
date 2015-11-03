package net.as93.treasurehunt.controllers.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.view.View.OnClickListener;
import android.widget.Toast;


import net.as93.treasurehunt.R;
import net.as93.treasurehunt.controllers.dialogs.SetUsernameDialog;
import net.as93.treasurehunt.models.Username;

public class HomeFragment extends Fragment implements
        SharedPreferences.OnSharedPreferenceChangeListener, OnClickListener {

    public final static String TYPE_OF_HUNTS = "net.as93.treasurehunt.TYPE_OF_HUNTS";
    Username username;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);

        // Update Signed in as text
        updateSignedInText();

        // Get all buttons and key elements
        Button myHuntsButton = (Button) view.findViewById(R.id.btnViewMyHunts);
        Button allHuntsButton = (Button) view.findViewById(R.id.btnViewAllHunts);
        Button createHuntButton = (Button) view.findViewById(R.id.btnCreateHunt);
        TextView lblSignedInAs = (TextView) view.findViewById(R.id.lblSignedInAs);

        // Add the onItemSelected in this class as the buttons action listener
        myHuntsButton.setOnClickListener(this);
        allHuntsButton.setOnClickListener(this);
        createHuntButton.setOnClickListener(this);
        lblSignedInAs.setOnClickListener(this);

        // Subscribe to username changes
        username.getPrefsObject().registerOnSharedPreferenceChangeListener(this);

        return view;
    }


    /**
     * Updates the label on the home screen
     * with the name of current signed in user
     */
    public void updateSignedInText(){
        TextView lblSignedInAs = (TextView) view.findViewById(R.id.lblSignedInAs);
        if(getActivity()!= null) {
            username = new Username(getActivity());
            if (username.isUserRegistered()) {
                lblSignedInAs.setText("Signed in as " + username.fetchUsername());
            } else {
                lblSignedInAs.setText("Not Signed in");
            }
        }
    }

    /**
     * When SharedPrefence subscribed item (just username) is changed
     * call the updateSignedInText() to display correct name
     */
    @Override
    public void onSharedPreferenceChanged(SharedPreferences arg0, String arg1){
        updateSignedInText();
    }


    /**
     * Set the click listeners of gui items
     * @param element View that was pressed
     */
    @Override
    public void onClick(View element) {

        int elemId = element.getId(); // The ID of the element pressed

        // If the user isn't registered, they need to register first
        if(!username.isUserRegistered()){
            elemId = R.id.lblSignedInAs; // this will open up register dialog
            Toast.makeText( // show toast message
                    getActivity(),
                    "You must register a username first",
                    Toast.LENGTH_LONG).show();
        }

        // Determine which button was pressed and do relevant action
        switch (elemId){
            case (R.id.btnViewMyHunts): {
                Bundle args = new Bundle();
                args.putChar(TYPE_OF_HUNTS, 'm');
                openFragment(new ViewHuntsFragment(), args);
                break;
            }
            case (R.id.btnViewAllHunts): {
                Bundle args = new Bundle();
                args.putChar(TYPE_OF_HUNTS, 's');
                openFragment(new ViewHuntsFragment(), args);
                break;
            }
            case (R.id.btnCreateHunt): {
                openFragment(new CreateHuntFragment());
                break;
            }
            case (R.id.lblSignedInAs): {
                DialogFragment setUsernameDialog = SetUsernameDialog.newInstance(getActivity(), "");
                setUsernameDialog.show(getActivity().getSupportFragmentManager(), "");
                break;
            }

        }

    }


    /**
     * Switches visible fragment
     * @param fragment Fragment to display
     * @param arguments Bundle of arguments
     */
    private void openFragment(Fragment fragment, Bundle arguments){
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragment.setArguments(arguments);
        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }


    /**
     * Switches visible fragment, with no Bundle or arguments
     * @param fragment Fragment to display
     */
    private void openFragment(Fragment fragment){
        openFragment(fragment, new Bundle());
    }

}
