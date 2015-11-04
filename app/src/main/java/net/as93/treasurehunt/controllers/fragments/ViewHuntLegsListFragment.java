package net.as93.treasurehunt.controllers.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import net.as93.treasurehunt.R;
import net.as93.treasurehunt.controllers.AddLocationActivity;

/**
 * Created by Alicia on 15/10/2015.
 */

public class ViewHuntLegsListFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private String KEY_HUNTNAME = "huntname";
    private String KEY_LEG = "leg";


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

        Button btnAddLocation = (Button) rootView.findViewById(R.id.btnAddLocation);
        btnAddLocation.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddLocationActivity.class);
                intent.putExtra(KEY_HUNTNAME, "awesome hunt");
                intent.putExtra(KEY_LEG, 1);
                startActivity(intent);
            }
        });


        return rootView;
    }
}