package net.as93.treasurehunt;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ViewHuntsFragment extends Fragment {

    public final static String TYPE_OF_HUNTS = "net.as93.treasurehunt.TYPE_OF_HUNTS";


    public ViewHuntsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_hunts, container, false);


        TextView tvTitle = (TextView) view.findViewById(R.id.viewHuntsTitle);
        char typeOfHunt = getArguments().getChar(TYPE_OF_HUNTS, 'a');
        if(typeOfHunt == 'a'){
            tvTitle.setText("All Treasure Hunts");
        }
        else if(typeOfHunt == 'm'){
            tvTitle.setText("My Treasure Hunts");
        }

        return view;
    }


    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(Uri uri);
    }

}
