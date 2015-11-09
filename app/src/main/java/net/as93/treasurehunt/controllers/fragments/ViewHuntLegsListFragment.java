package net.as93.treasurehunt.controllers.fragments;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import net.as93.treasurehunt.R;
import net.as93.treasurehunt.controllers.ViewHunt;
import net.as93.treasurehunt.controllers.dialogs.LocationDetailsDialog;
import net.as93.treasurehunt.models.Leg;
import net.as93.treasurehunt.models.Username;
import net.as93.treasurehunt.utils.GetAllLocations;
import net.as93.treasurehunt.utils.GetReachedLocations;
import net.as93.treasurehunt.utils.IGetLocations;
import net.as93.treasurehunt.utils.IReturnResponseCode;
import net.as93.treasurehunt.utils.IShowNumberOfLocations;
import net.as93.treasurehunt.utils.NumberOfLocationsInHunt;

import java.util.ArrayList;


public class ViewHuntLegsListFragment extends Fragment
        implements  IShowNumberOfLocations, IGetLocations{
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    private String huntName;
    private ArrayList<Leg> legs = new ArrayList<>(); // List of location legs for adapter
    private ArrayAdapter<Leg> itemsAdapter; // List adapter
    private TextView lblNumOfLegs;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static ViewHuntLegsListFragment newInstance(int sectionNumber) {
        ViewHuntLegsListFragment fragment = new ViewHuntLegsListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public ViewHuntLegsListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_view_hunt_list, container, false);

        // Fetch the hunt name from intent bundle
        huntName = getActivity().getIntent().getExtras().getString("huntname");


        // Create the list ready for putting legs in it
        final ListView itemsLst = (ListView) rootView.findViewById(R.id.items_legs);
        itemsAdapter = new ArrayAdapter<Leg>(getActivity(), android.R.layout.simple_list_item_1, legs);
        itemsLst.setAdapter(itemsAdapter);

        lblNumOfLegs = (TextView) rootView.findViewById(R.id.lblNumOfLegs);

        // Call method that calls method that executes the fetch leg request method
        updateLegs();

        // Add click listeners to each location in hunt
        itemsLst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showClue(position);
            }
        });

        new NumberOfLocationsInHunt(this, huntName);

        return rootView;
    }



    /**
     * Call the fetch legs request and sets result to adapter to display
     */
    private void updateLegs(){
        new GetAllLocations(this, huntName);
        if(((ViewHunt)getActivity()).isUserTheCreator()){
            // User is the creator of the hunt? If so show all locations

        }
        else{ // The user did not create this hunt. Only show locations they have visited
//            new GetReachedLocations(this, huntName, ((new Username(getActivity())).fetchUsername()));
        }

    }


    /**
     * Makes the Bundle of arguments to pass to the dialog that displays
     * Location details.
     * @param position int the position in the legs list
     * @return Bundle populated and ready to go
     */
    private Bundle makeDialogBundle(int position){
        Bundle args = new Bundle();
        args.putString("locationName", legs.get(position).getName());
        args.putString("locationDescription", legs.get(position).getDescription());
        args.putString("locationLat", legs.get(position).getLatitude());
        args.putString("locationLng", legs.get(position).getLongitude());
        args.putString("locationParent", huntName);
        args.putString("locationPosition", legs.get(position).getPosition());
        args.putBoolean("isLastLeg", isLastLeg(position));
        if(!isLastLeg(position)){
            args.putString("locationQuestion", legs.get(position).getQuestion());
            args.putString("locationClue", legs.get(position).getClue());
            args.putString("locationAnswer", legs.get(position).getAnswer());
        }
        return args;
    }


    /**
     * Returns true if the position number indicates that this is the last
     * location leg in this hunt
     * @param position int the index in legs array
     * @return Boolean true if last element
     */
    private boolean isLastLeg(int position){
        return legs.size() == position + 1;
    }


    /**
     * Shows a message depending on how many locations were found for given hunt
     * @param size
     */
    private void showResultsMessage(int size){
        String message;
        if(size == 0){ message = "No locations found for this hunt"; }
        else{ message = size + " locations found for this hunt";  }
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }



    @Override
    public void numberOfLocationsReturned(int num) {
        lblNumOfLegs.setText("Displaying " + legs.size() + " out of " + num + " Locations");
    }


    /**
     * Shows the dialog box with details, questiona and clue
     * @param position int the location position in hunt
     */
    private void showClue(int position){
        DialogFragment locationDetails = LocationDetailsDialog.newInstance();
        locationDetails.setArguments(makeDialogBundle(position));
        locationDetails.show(getFragmentManager(), "");
    }

    @Override
    public void locationsReturned(Object results) {
        ArrayList<Leg> formattedResults = (ArrayList<Leg>)results;

//        for(int i = 0; i< formattedResults.size(); i++){
//            if(!isLocationConqured(formattedResults.get(i))){
//                formattedResults.remove(i);
//            }
//        }

        legs.clear();
        legs.addAll(formattedResults);
        itemsAdapter.notifyDataSetChanged();
//        showResultsMessage(formattedResults.size()); // Show message
        try {
            ((ViewHunt) getActivity()).setNumLocations(formattedResults.size());
        }
        catch (Exception e){
            ((ViewHunt) getActivity()).setNumLocations(0);

        }
    }


    private boolean isLocationConqured(Leg location){
        ArrayList<String> completedLocations;
        try{
            completedLocations = ((ViewHunt)getActivity()).getCompletedLocationNames();}
        catch (Exception e){
            completedLocations = new ArrayList<>();
        }
        if(completedLocations!=null) {
            for (String doneLocation : completedLocations) {
                if (location.getName().equals(doneLocation)) {
                    return true;
                }
            }
        }
        return false;
    }


}