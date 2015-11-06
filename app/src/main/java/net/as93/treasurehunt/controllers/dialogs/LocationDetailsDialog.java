package net.as93.treasurehunt.controllers.dialogs;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import net.as93.treasurehunt.R;

public class LocationDetailsDialog extends DialogFragment {

    public static LocationDetailsDialog newInstance() {

        LocationDetailsDialog locationDetailsDialog = new LocationDetailsDialog();
        Bundle args = new Bundle();
        locationDetailsDialog.setArguments(args);

        return locationDetailsDialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_view_location, container, false);

        // Get UI elements
        TextView tvDescription = (TextView) view.findViewById(R.id.dlgDescription);
        TextView tvSummary = (TextView) view.findViewById(R.id.dlgSummary);
        TextView tvLocation = (TextView) view.findViewById(R.id.dlgLocation);
        TextView tvNextQuestion = (TextView) view.findViewById(R.id.dlgNextQuestion);
        TextView tvClue = (TextView) view.findViewById(R.id.dlgClue);
        EditText etAnswer = (EditText) view.findViewById(R.id.dlgAnswer);
        Button btnSubmitAnswer = (Button) view.findViewById(R.id.btnSubmitAnswer);

        // Make values
        Bundle args = getArguments(); // Get the arguments bundle passed to dialog
        String dlgTitle = args.getString("locationName");
        String dlgDescription = args.getString("locationDescription");
        String dlgSummary = "Position "+args.getString("locationPosition")+
                " in "+args.getString("locationParent");
        String dlgLocation = "Latitude: "+args.getString("locationLat") +
                "\nLongitude: "+args.getString("locationLng");
        String dlgNextQuestion = "";
        String dlgNextClue = "";
        if(!args.getBoolean("isLastLeg")){
            dlgNextQuestion = "Next Question: " + args.getString("locationQuestion");
            dlgNextClue = "Clue: " + args.getString("locationClue");
        }

        // Set values of elements
        getDialog().setTitle("You are at "+dlgTitle); // Title for dialog
        tvDescription.setText(dlgDescription);
        tvSummary.setText(dlgSummary);
        tvLocation.setText(dlgLocation);
        tvNextQuestion.setText(dlgNextQuestion);
        tvClue.setText(dlgNextClue);

        // If last location, get rid of text field and button
        if(args.getBoolean("isLastLeg")){
            etAnswer.setVisibility(View.GONE);
            btnSubmitAnswer.setVisibility(View.GONE);
            tvNextQuestion.setText("Congratulations, you are at the final location of this hunt!");
            tvNextQuestion.setTextColor(getResources().getColor(R.color.darkorange));
        }

        return view;
    }




}
