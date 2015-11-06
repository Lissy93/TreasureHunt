package net.as93.treasurehunt.controllers.fragments;

        import android.os.Bundle;
        import android.support.v4.app.Fragment;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ListView;

        import com.google.android.gms.maps.CameraUpdate;
        import com.google.android.gms.maps.CameraUpdateFactory;
        import com.google.android.gms.maps.GoogleMap;
        import com.google.android.gms.maps.SupportMapFragment;
        import com.google.android.gms.maps.model.LatLng;
        import com.google.android.gms.maps.model.LatLngBounds;
        import com.google.android.gms.maps.model.Marker;
        import com.google.android.gms.maps.model.MarkerOptions;

        import net.as93.treasurehunt.R;
        import net.as93.treasurehunt.models.Leg;
        import net.as93.treasurehunt.utils.apiRequests.ControllerThatMakesARequest;
        import net.as93.treasurehunt.utils.apiRequests.GetReqFetchLegs;

        import java.util.ArrayList;

/**
 * Created by Alicia on 15/10/2015.
 */

public class ViewHuntLegsMapFragment extends Fragment implements ControllerThatMakesARequest{
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private String huntName;
    private GoogleMap map;

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
        View rootView = inflater.inflate(R.layout.fragment_view_hunt_map, container, false);

        huntName = getActivity().getIntent().getExtras().getString("huntname");

        GetReqFetchLegs fetchAllLegs;
        fetchAllLegs = new GetReqFetchLegs(this, huntName);
        fetchAllLegs.execute();

         map = ((SupportMapFragment) getChildFragmentManager()
                 .findFragmentById(R.id.mapFragmentHunt)).getMap();

        return rootView;
    }

    @Override
    public void thereAreResults(Object results) {

        // The results
        ArrayList<Leg> formattedResults = (ArrayList<Leg>)results;

        // For calculating the bounds to zoom to
        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        // For each location le
        for(Leg leg: formattedResults){

            // Make Lat Long Object
            LatLng ll = new LatLng(
                    Double.valueOf(leg.getLatitude()),
                    Double.valueOf(leg.getLongitude())
            );

            // Add maker to map
            map.addMarker(new MarkerOptions().position(ll).title(leg.getName()));

            builder.include(ll);

        }

        LatLngBounds bounds = builder.build();

        int padding = 40; // offset from edges of the map in pixels
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);

        map.animateCamera(cu); // Set the view
    }
}