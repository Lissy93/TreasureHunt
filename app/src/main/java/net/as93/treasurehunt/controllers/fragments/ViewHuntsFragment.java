package net.as93.treasurehunt.controllers.fragments;

import android.content.Intent;
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
import net.as93.treasurehunt.utils.apiRequests.ControllerThatMakesARequest;
import net.as93.treasurehunt.utils.apiRequests.ReqFetchAllHunts;

import java.util.ArrayList;

public class ViewHuntsFragment extends Fragment implements ControllerThatMakesARequest{

    public final static String TYPE_OF_HUNTS = "net.as93.treasurehunt.TYPE_OF_HUNTS";

    private ArrayList<Hunt> hunts = new ArrayList<Hunt>();
    private ArrayAdapter<Hunt> itemsAdapter;


    public ViewHuntsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_hunts, container, false);


        TextView tvTitle = (TextView) view.findViewById(R.id.viewHuntsTitle);
        char typeOfHunt = getArguments().getChar(TYPE_OF_HUNTS, 'a');
        if (typeOfHunt == 'a') {
            tvTitle.setText("All Treasure Hunts");
        } else if (typeOfHunt == 'm') {
            tvTitle.setText("My Treasure Hunts");
        }

        ListView itemsLst = (ListView) view.findViewById(R.id.items_lst);
        itemsAdapter = new ArrayAdapter<Hunt>(getActivity(), android.R.layout.simple_list_item_1, hunts);
        itemsLst.setAdapter(itemsAdapter);

        ReqFetchAllHunts readerTask = new ReqFetchAllHunts(this);
        readerTask.execute();



        itemsLst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ViewHunt.class);
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void thereAreResults(Object results) {
        ArrayList<Hunt> formatedResults = (ArrayList<Hunt>)results;
        hunts.clear();
        hunts.addAll(formatedResults);
        itemsAdapter.notifyDataSetChanged();
        }
}
