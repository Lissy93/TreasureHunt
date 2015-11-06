package net.as93.treasurehunt.controllers.fragments;

import android.content.Intent;
import android.os.Bundle;
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
import net.as93.treasurehunt.models.Username;
import net.as93.treasurehunt.utils.FetchRegisteredUsers;
import net.as93.treasurehunt.utils.RegisterUserOnHunt;

import java.util.ArrayList;


public class HuntSummaryFragment extends Fragment{

    // Hunt Details
    private String huntName;
    private String username;
    private boolean playerRegistered = false;

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

        // Show the add new leg screen when button is pressed
        btnAddLocation.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showAddNewLocationDialog();
            }
        });

        // Either register on hunt, or show next clue
        btnRegisterOnHunt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(playerRegistered){ showNextClue(); }
                else{ registerPlayerOnHunt(); }
            }
        });

        checkIfPlayerRegisteredOnHunt();

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
            showAddNewLocationDialog();
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
    public void updatePlayerRegistered(Object results){
        ArrayList<String> formattedResults = (ArrayList<String>)results;
        if(isPlayerRegistered(formattedResults)){
            changeButtonToContinue();
        }
    }


    /**
     * Determines if a user is registered on a hunt or not
     * @param results the results returned from the webservice
     * @return boolean, true if they are registered
     */
    protected boolean isPlayerRegistered(ArrayList<String> results) {
        for (String player : results) {
            if (player.equals(username)) {
                playerRegistered = true;
            }
        }
        return playerRegistered;
    }


    /**
     * Determines what the next unreached location is,
     * and displays dialog box
     */
    private void showNextClue(){
        // TODO
    }


    /**
     * Just changes the text in the 'Register on Hunt' button
     * to become 'Show Next Clue'.
     * The same method gets called when the button is pressed still
     */
    private void changeButtonToContinue(){
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
}
