package net.as93.treasurehunt.controllers.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import net.as93.treasurehunt.R;
import net.as93.treasurehunt.controllers.ViewHunt;
import net.as93.treasurehunt.models.Hunt;
import net.as93.treasurehunt.models.Username;
import net.as93.treasurehunt.utils.apiRequests.ControllerThatMakesARequest;
import net.as93.treasurehunt.utils.apiRequests.GetReqFetchHunts;

import java.util.ArrayList;

public class ViewHuntsFragment extends Fragment
        implements ControllerThatMakesARequest,
        SharedPreferences.OnSharedPreferenceChangeListener{

    public final static String TYPE_OF_HUNTS = // Tag used for getting args
            "net.as93.treasurehunt.TYPE_OF_HUNTS";

    private ArrayList<Hunt> hunts = new ArrayList<>(); // List of hunts for adapter
    private ArrayAdapter<Hunt> itemsAdapter; // List adapter
    char typeOfHunt = 'a'; // (a | m) a = show all hunts, m = show just my hunts

    public ViewHuntsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate layout
        View view = inflater.inflate(R.layout.fragment_view_hunts, container, false);

        // Set title text
        TextView tvTitle = (TextView) view.findViewById(R.id.viewHuntsTitle);
        typeOfHunt = getArguments().getChar(TYPE_OF_HUNTS, 'a');
        if (typeOfHunt == 'a') {
            tvTitle.setText("All Treasure Hunts");
        } else if (typeOfHunt == 'm') {
            tvTitle.setText("My Treasure Hunts");
        }

        // Create the list ready for putting hunts in it
        final ListView itemsLst = (ListView) view.findViewById(R.id.items_lst);
        itemsAdapter = new ArrayAdapter<Hunt>(getActivity(), android.R.layout.simple_list_item_1, hunts);
        itemsLst.setAdapter(itemsAdapter);

        // Call method that calls method that executes the fetch hunt request method
        updateHunts();

        // Add click listeners to each item in lit of hunts
        itemsLst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ViewHunt.class);
                intent.putExtra("huntname", itemsAdapter.getItem(position).getTitle());
                startActivity(intent);
            }
        });

        // Set Pref listener on Username, so if it changes list of hunts can update
        new Username(getActivity())
                .getPrefsObject()
                .registerOnSharedPreferenceChangeListener(this);

        return view;
    }


    /**
     * Call the fetch hunt request and sets result to adapter to display
     * Checks if displaying all hunts of just users hunts
     * If displaying just users hunts, find the username and pass it as a param
     */
    private void updateHunts(){
        GetReqFetchHunts fetchAllHunts;
        if (typeOfHunt == 'm') {
            fetchAllHunts = new GetReqFetchHunts(
                    this, new Username(getActivity()).fetchUsername());
        }
        else  {
            fetchAllHunts = new GetReqFetchHunts(this);
        }
        fetchAllHunts.execute();
    }


    /**
     * This method is called after results are returned from the async
     * @param results and ArrayList of Hunt objects
     */
    @Override
    public void thereAreResults(Object results) {
        ArrayList<Hunt> formattedResults = (ArrayList<Hunt>)results;
        hunts.clear();
        hunts.addAll(formattedResults);
        itemsAdapter.notifyDataSetChanged();
        }


    /**
     * Update the list of hunts when the user modifies their username
     * @param sharedPreferences SharedPreferences instance
     * @param key String key to watch
     */
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        updateHunts();
    }
}
