package net.as93.treasurehunt.controllers.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import net.as93.treasurehunt.R;
import net.as93.treasurehunt.models.Leg;
import net.as93.treasurehunt.utils.apiRequests.ControllerThatMakesARequest;
import net.as93.treasurehunt.utils.apiRequests.GetReqFetchLegs;

import java.util.ArrayList;


public class ViewHuntLegsListFragment extends Fragment implements ControllerThatMakesARequest{
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    private String huntName;
    private ArrayList<Leg> legs = new ArrayList<>(); // List of location legs for adapter
    private ArrayAdapter<Leg> itemsAdapter; // List adapter

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

        // Call method that calls method that executes the fetch leg request method
        updateLegs();

        return rootView;
    }



    /**
     * Call the fetch legs request and sets result to adapter to display
     */
    private void updateLegs(){
        GetReqFetchLegs fetchAllLegs;
        fetchAllLegs = new GetReqFetchLegs(this, huntName);
        fetchAllLegs.execute();
    }


    /**
     * This method is called after results are returned from the async
     * @param results and ArrayList of Leg objects
     */
    @Override
    public void thereAreResults(Object results) {
        ArrayList<Leg> formattedResults = (ArrayList<Leg>)results;
        legs.clear();
        legs.addAll(formattedResults);
        itemsAdapter.notifyDataSetChanged();
    }
}