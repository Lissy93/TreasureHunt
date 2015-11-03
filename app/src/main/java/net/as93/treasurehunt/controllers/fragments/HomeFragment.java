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

import net.as93.treasurehunt.R;
import net.as93.treasurehunt.controllers.dialogs.SetUsernameDialog;
import net.as93.treasurehunt.models.Username;

public class HomeFragment extends Fragment implements SharedPreferences.OnSharedPreferenceChangeListener {

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


        Button myHuntsButton = (Button) view.findViewById(R.id.btnViewMyHunts);
        myHuntsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                Fragment viewMyHunts = new ViewHuntsFragment();

                Bundle argsAll = new Bundle();
                argsAll.putChar(TYPE_OF_HUNTS, 'm');
                viewMyHunts.setArguments(argsAll);

                fragmentManager.beginTransaction()
                        .replace(R.id.container, viewMyHunts)
                        .commit();
            }
        });

        Button allHuntsButton = (Button) view.findViewById(R.id.btnViewAllHunts);
        allHuntsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                Fragment viewAllHunts = new ViewHuntsFragment();
                Bundle argsAll = new Bundle();
                argsAll.putChar(TYPE_OF_HUNTS, 'a');
                viewAllHunts.setArguments(argsAll);
                fragmentManager.beginTransaction()
                        .replace(R.id.container, viewAllHunts)
                        .commit();
            }
        });

        Button createHuntButton = (Button) view.findViewById(R.id.btnCreateHunt);
        createHuntButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                Fragment createHunt = new CreateHuntFragment();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, createHunt)
                        .commit();
            }
        });


        TextView lblSignedInAs = (TextView) view.findViewById(R.id.lblSignedInAs);
        lblSignedInAs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment setUsernameDialog = SetUsernameDialog.newInstance(
                        getActivity(), "");
                setUsernameDialog.show(getActivity().getSupportFragmentManager(), "");
            }
        });

        username.getPrefsObject().registerOnSharedPreferenceChangeListener(this);

        return view;
    }


    /**
     * Updates the label on the home screen
     * with the name of current signed in user
     */
    public void updateSignedInText(){
        TextView lblSignedInAs = (TextView) view.findViewById(R.id.lblSignedInAs);
        username = new Username(getActivity());
        String strUsername = username.fetchUsername();
        if(strUsername.equals("Guest")){
            lblSignedInAs.setText("Not Signed in");
        }
        else {
            lblSignedInAs.setText("Signed in as " + strUsername);
        }
    }


    public void onSharedPreferenceChanged(SharedPreferences arg0, String arg1)
    {
        updateSignedInText();
    }

}
