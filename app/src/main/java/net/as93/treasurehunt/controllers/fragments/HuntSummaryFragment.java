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
import net.as93.treasurehunt.utils.apiRequests.ControllerThatMakesARequest;
import net.as93.treasurehunt.utils.apiRequests.PostReqRegisterPlayer;

import java.util.ArrayList;


public class HuntSummaryFragment extends Fragment{

    private String huntName;
    private String username;
    private boolean playerRegistered = false;
    private static final String ARG_SECTION_NUMBER = "section_number";
    private Button btnRegisterOnHunt;

    public static HuntSummaryFragment newInstance(int sectionNumber) {
        HuntSummaryFragment fragment = new HuntSummaryFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public HuntSummaryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Fetch the hunt name from intent bundle
        huntName = ((ViewHunt) getActivity()).getHuntName();

        this.username = (new Username(getActivity()).fetchUsername());

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

        // Show the add new leg screen when button is pressed
        Button btnAddLocation = (Button) rootView.findViewById(R.id.btnAddLocation);
        btnAddLocation.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddLocationActivity.class);
                intent.putExtra("huntname", huntName);
                intent.putExtra("leg", ((ViewHunt) getActivity()).getNumLocations() + 1);
                startActivity(intent);
            }
        });

        btnRegisterOnHunt = (Button) rootView.findViewById(R.id.btnRegisterOnHunt);
        btnRegisterOnHunt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(playerRegistered){
                    showNextClue();
                }
                else{
                    registerPlayerOnHunt();
                }
            }
        });

        checkIfPlayerRegisteredOnHunt();

        return rootView;
    }


    private void registerPlayerOnHunt(){
        String huntName = ((ViewHunt) getActivity()).getHuntName();
        new RegisterUserOnHunt(username, huntName, this);
    }

    public void userRegisterRequestComplete(String results){
        if (results.equals("200")) {
            Toast.makeText(getActivity(), "Successfully Registered on Hunt", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(getActivity(), "Error, you may already be registered on this hunt", Toast.LENGTH_SHORT).show();

        }
    }


    protected void checkIfPlayerRegisteredOnHunt(){
        new FetchRegisteredUsers(huntName, this);
    }


    /**
     *
     * @param results
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


    private void showNextClue(){

    }

    private void changeButtonToContinue(){
        btnRegisterOnHunt.setText("Show Next Clue");
    }

}
