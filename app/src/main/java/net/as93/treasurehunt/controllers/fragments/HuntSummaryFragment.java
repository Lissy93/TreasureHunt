package net.as93.treasurehunt.controllers.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import net.as93.treasurehunt.R;
import net.as93.treasurehunt.controllers.AddLocationActivity;
import net.as93.treasurehunt.controllers.ViewHunt;
import net.as93.treasurehunt.controllers.dialogs.LocationDetailsDialog;
import net.as93.treasurehunt.models.Leg;
import net.as93.treasurehunt.models.Username;
import net.as93.treasurehunt.utils.calls.FetchRegisteredUsers;
import net.as93.treasurehunt.utils.calls.GetAllLocations;
import net.as93.treasurehunt.utils.calls.GetReachedLocations;
import net.as93.treasurehunt.utils.calls.IGetLocations;
import net.as93.treasurehunt.utils.calls.IGetStrings;
import net.as93.treasurehunt.utils.calls.RegisterUserOnHunt;

import java.util.ArrayList;


public class HuntSummaryFragment extends Fragment implements IGetLocations, IGetStrings{

    // Hunt Details
    private String huntName;
    private String username;
    private boolean playerRegistered = false;
    private ArrayList<Leg> locations = null;
    private boolean huntComplete = false;

    // Buttons
    private Button btnRegisterOnHunt;
    private Button btnAddLocation;


    public static HuntSummaryFragment newInstance(int sectionNumber) {
        return new HuntSummaryFragment();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        huntName = ((ViewHunt) getActivity()).getHuntName(); // Set hunt name
        this.username = (new Username(getActivity()).fetchUsername()); // Set username
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_hunt_sumary, container, false);

        // Set values for labels
        TextView lblHuntTitle = (TextView) rootView.findViewById(R.id.lblHuntName);
        lblHuntTitle.setText(huntName);
        TextView lblCreatedBy = (TextView) rootView.findViewById(R.id.lblCreatedBy);
        lblCreatedBy.setText("Created by " + ((ViewHunt) getActivity()).getCreator());

        // Set buttons
        btnAddLocation = (Button) rootView.findViewById(R.id.btnAddLocation);
        btnRegisterOnHunt = (Button) rootView.findViewById(R.id.btnRegisterOnHunt);

        // Don't show the Add Location button if this user didn't create this hunt
        if(!((ViewHunt) getActivity()).isUserTheCreator()){
            btnAddLocation.setVisibility(View.GONE);
        }

        // Show the add new leg screen when button is pressed
        btnAddLocation.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showAddNewLocationDialog();
            }
        });

        // Either register on hunt, or show next clue
        btnRegisterOnHunt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (playerRegistered) {
                    showNextClue();
                } else {
                    registerPlayerOnHunt();
                }
            }
        });

        checkIfPlayerRegisteredOnHunt();

        new GetAllLocations(this, huntName);

        return rootView;
    }


    /**
     * Registers the current user on the current hunt
     */
    private void registerPlayerOnHunt(){
        String huntName = ((ViewHunt) getActivity()).getHuntName();
        new RegisterUserOnHunt(username, huntName, this);
    }


    /**
     * Called when the Async task is finished and the user (should be)
     * successfully registered on the hunt.
     * Notifies the user with the result of the register request
     * Then shows the next clue dialog
     * @param results String the status code (hopefully 200)
     */
    public void userRegisterRequestComplete(String results){
        if (results.equals("200")) {
            Toast.makeText(getActivity(),
                    "Successfully Registered on Hunt",
                    Toast.LENGTH_SHORT).show();
            changeButtonToContinue();
            showNextClue();
        }
        else{
            Toast.makeText(getActivity(),
                    "Error, you may already be registered on this hunt",
                    Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * Determines if player is registered on hunt or not
     * When Async is finished, it will call updatePlayerRegistered() below
     */
    protected void checkIfPlayerRegisteredOnHunt(){
        new FetchRegisteredUsers(huntName, this);
    }


    /**
     * Called when the checkIfPlayerRegisteredOnHunt() is complete
     * Checks if current user is a registered player on current hunt
     * If the player is registered, then it will update the register button
     * to become a show next clue button
     * @param results String ArrayList of users registered on hunt
     */
    public void updatePlayerRegistered(Object results) {
        if (((ArrayList<String>) results).size() != 0) {
            ArrayList<String> formattedResults = ((ArrayList<ArrayList<String>>) results).get(0);
            if (isPlayerRegistered(formattedResults)) {
                changeButtonToContinue();
            }
        }

        if(playerRegistered) new GetReachedLocations(this, huntName, username); // if they r then fetch locations


    }


    /**
     * Determines if a user is registered on a hunt or not
     * @param results the results returned from the webservice
     * @return boolean, true if they are registered
     */
    protected boolean isPlayerRegistered(ArrayList<String> results) {
        for (String player : results) {
            if (player!=null && player.equals(username)) {
                playerRegistered = true;
                break;
            }
        }
        return playerRegistered;
    }


    /**
     * Determines what the next unreached location is,
     * and displays dialog box
     */
    private void showNextClue(){
        new GetReachedLocations(this, huntName, username);
    }


    /**
     * Just changes the text in the 'Register on Hunt' button
     * to become 'Show Next Clue'.
     * The same method gets called when the button is pressed still
     */
    private void changeButtonToContinue(){
        playerRegistered = true;
        btnRegisterOnHunt.setText("Show Next Clue");
    }


    /**
     * Displays the dialog box that allows the user to add a new location
     * to the current hunt. The hunt name and last location position are passed.
     * This should only be able to be called if the user is the user who created
     * this hunt
     */
    private void showAddNewLocationDialog(){
        Intent intent = new Intent(getActivity(), AddLocationActivity.class);
        intent.putExtra("huntname", huntName);
        intent.putExtra("leg", ((ViewHunt) getActivity()).getNumLocations() + 1);
        startActivity(intent);
    }




    /**
     * Shows the dialog box with details, questiona and clue
     * @param location int the location position in hunt
     */
    private void showTheQuestionDialog(Leg location){
        DialogFragment locationDetails = LocationDetailsDialog.newInstance();
        locationDetails.setArguments(makeDialogBundle(location));
        locationDetails.show(getFragmentManager(), "");
    }

    /**
     * Makes the Bundle of arguments to pass to the dialog that displays
     * Location details.
     * @param leg  object
     * @return Bundle populated and ready to go
     */
    private Bundle makeDialogBundle(Leg leg){
        Bundle args = new Bundle();
        args.putString("locationName", leg.getName());
        args.putString("locationDescription", leg.getDescription());
        args.putString("locationLat", leg.getLatitude());
        args.putString("locationLng", leg.getLongitude());
        args.putString("locationParent", huntName);
        args.putString("locationPosition", leg.getPosition());
        args.putBoolean("isLastLeg", huntComplete);
            args.putString("locationQuestion", leg.getQuestion());
            args.putString("locationClue", leg.getClue());
            args.putString("locationAnswer", leg.getAnswer());
        return args;
    }

    @Override
    public void locationsReturned(Object results) {
        locations = (ArrayList<Leg>)results;
    }

    @Override
    public void stringsReturned(ArrayList<String> results) {

        ((ViewHunt)getActivity()).setCompletedLocationNames(results);

        if(results.size()==locations.size()) {
            huntComplete = true;
            btnRegisterOnHunt.setText("Hunt Complete!");
        }

        if(locations.size()>0) {
            Leg legToReturn = locations.get(locations.size() - 1);
            for (Leg leg : locations) {
                boolean found = false;
                for (String visitedPlace : results) {
                    if (leg.getName().equals(visitedPlace)) {
                        found = true;
                    }
                }
                if (!found) {
                    legToReturn = leg;
                    break;
                }
            }
            showTheQuestionDialog(legToReturn);
        }
        else{
            Toast.makeText(
                    getActivity(),
                    "Error: The creator of this hunt has not yet added any locations",
                    Toast.LENGTH_SHORT).show();
        }


    }
}


