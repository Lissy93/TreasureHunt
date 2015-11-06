package net.as93.treasurehunt.controllers.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import net.as93.treasurehunt.R;
import net.as93.treasurehunt.controllers.ViewHunt;
import net.as93.treasurehunt.models.Leg;
import net.as93.treasurehunt.models.Username;
import net.as93.treasurehunt.utils.GetAllLocations;
import net.as93.treasurehunt.utils.GetReachedLocations;
import net.as93.treasurehunt.utils.IGetLocations;
import net.as93.treasurehunt.utils.apiRequests.ControllerThatMakesARequest;
import net.as93.treasurehunt.utils.apiRequests.GetReqFetchLegs;

import java.util.ArrayList;
import java.util.Random;

public class ViewHuntLegsMapFragment extends Fragment implements IGetLocations {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private GoogleMap map;
    private static View view;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static ViewHuntLegsMapFragment newInstance(int sectionNumber) {
        ViewHuntLegsMapFragment fragment = new ViewHuntLegsMapFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public ViewHuntLegsMapFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {
            view = inflater.inflate(R.layout.fragment_view_hunt_map, container, false);
        } catch (InflateException e) {
        /* map is already there, just return view as it is */
        }

        String huntName = getActivity().getIntent().getExtras().getString("huntname");

        new GetAllLocations(this, huntName);

        if(((ViewHunt)getActivity()).isUserTheCreator()){
            // User is the creator of the hunt? If so show all locations
        }
        else{ // The user did not create this hunt. Only show locations they have visited
//            new GetReachedLocations(this, huntName, ((new Username(getActivity())).fetchUsername()));
        }

         map = ((SupportMapFragment) getChildFragmentManager()
                 .findFragmentById(R.id.mapFragmentHuntMap)).getMap();

        return view;
    }




    /**
     * This method does everything related to displaying the results on the
     * Google Map. It puts markers for each location, with the location name
     * and description, it also creates a polyline path joining together points
     * @param results Object
     */
    private void putResultsOnTheMap(Object results){
        // The results
        ArrayList<Leg> formattedResults = (ArrayList<Leg>)results;

        // For calculating the bounds to zoom to
        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        // Will store a list of latitude and longitudes, for drawing polylines
        LatLng[] llList = new LatLng[formattedResults.size()];

        // For each location location
        int c = 0; // counter
        for(Leg leg: formattedResults){

            // Make Lat Long Object
            LatLng ll;
            try {
                ll = new LatLng(
                        Double.valueOf(leg.getLatitude()),
                        Double.valueOf(leg.getLongitude())
                );
            }
            catch (Exception e){
                ll = new LatLng(
                        51.7+((new Random().nextInt(0 - 10 + 1))/1000),
                        -1.0+((new Random().nextInt(0 - 10 + 1))/1000)
                );
            }
            llList[c] = ll; // Add the latitude and longitude to list for later

            // Add maker to map
            map.addMarker(
                    new MarkerOptions()
                            .position(ll).title(leg.getName()).snippet(leg.getDescription()
                    ));

            builder.include(ll);
            c++;
        }

        if(c>0) { // Only do this is we have actually got results
            LatLngBounds bounds = builder.build();
            int padding = 40; // offset from edges of the map in pixels
            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
            map.animateCamera(cu); // Set the view

            Polyline line = map.addPolyline(new PolylineOptions()
                    .add(llList)
                    .width(5)
                    .color(Color.RED));
            line.setGeodesic(true);
            updateNumMarkerText("Displaying "+c+" locations");
        }
        else{
            // There were no hunts :'(
            updateNumMarkerText("There aren't yet any locations to display for this hunt");
        }
    }

    /**
     * Updates the status text below the title
     * To inform the user how many location markers will be displayed
     * So that they don't think the app is broke when the map is empty...
     * @param newText
     */
    private void updateNumMarkerText(String newText){
        TextView lblNumberOfMarkers = (TextView) view.findViewById(R.id.lblNumberOfMarkers);
        lblNumberOfMarkers.setText(newText);
    }

    @Override
    public void locationsReturned(Object results) {
        putResultsOnTheMap(results);
    }
}

