package net.as93.treasurehunt.controllers.dialogs;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import net.as93.treasurehunt.R;
import net.as93.treasurehunt.controllers.ViewHunt;
import net.as93.treasurehunt.models.Username;
import net.as93.treasurehunt.utils.IReturnResponseCode;
import net.as93.treasurehunt.utils.RegisterUserOnHunt;
import net.as93.treasurehunt.utils.SubmitReachLocation;

import java.util.ArrayList;

public class LocationDetailsDialog extends DialogFragment implements IReturnResponseCode{

    String ans;
    String dlgTitle;
    String huntName = "";
    IReturnResponseCode dis;

    public static LocationDetailsDialog newInstance() {

        LocationDetailsDialog locationDetailsDialog = new LocationDetailsDialog();
        Bundle args = new Bundle();
        locationDetailsDialog.setArguments(args);

        return locationDetailsDialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        dis = this;
        huntName = ((ViewHunt)getActivity()).getHuntName();

        View view = inflater.inflate(R.layout.dialog_view_location, container, false);

        // Get UI elements
        TextView tvDescription = (TextView) view.findViewById(R.id.dlgDescription);
        TextView tvSummary = (TextView) view.findViewById(R.id.dlgSummary);
        TextView tvLocation = (TextView) view.findViewById(R.id.dlgLocation);
        TextView tvNextQuestion = (TextView) view.findViewById(R.id.dlgNextQuestion);
        TextView tvClue = (TextView) view.findViewById(R.id.dlgClue);
        final EditText etAnswer = (EditText) view.findViewById(R.id.dlgAnswer);
        Button btnSubmitAnswer = (Button) view.findViewById(R.id.btnSubmitAnswer);

        // Make values
        Bundle args = getArguments(); // Get the arguments bundle passed to dialog
        dlgTitle = args.getString("locationName");
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
        ans = args.getString("locationAnswer");

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

        // If location is complete then don't let the user resubmit it
        if(isLocationComplete()){
            etAnswer.setText(ans);
            etAnswer.setEnabled(false);
            btnSubmitAnswer.setEnabled(false);
        }

        // Button listener on submit
        Button btnGuess = (Button)view.findViewById(R.id.btnSubmitAnswer);
        btnGuess.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(formatAnswer(etAnswer.getText().toString()).equals(formatAnswer(ans))){
                    //Guessed Correctly
                    new SubmitReachLocation(dis, huntName, (new Username(getActivity()).fetchUsername()),dlgTitle, "2015-10-27T20:45:35");
                }
                else{ // Guessed incorrectly
                    Toast.makeText(getActivity(), "Incorrect Answer, try again", Toast.LENGTH_SHORT).show();
                    etAnswer.setText("");
                }
            }
        });

        return view;
    }

    private String formatAnswer(String answer){
        return answer.replaceAll("[^A-Za-z0-9]+","").toLowerCase();
    }


    @Override
    public void requestComplete(int responseCode) {
        if(responseCode== 200){
            Toast.makeText(getActivity(), "Correct!", Toast.LENGTH_SHORT).show();
            this.dismiss();
        }
        else{
            Toast.makeText(getActivity(), "Correct - Error Updating", Toast.LENGTH_SHORT).show();
        }
    }


    private boolean isLocationComplete(){
        ArrayList<String> completedLocations;
        try{
            completedLocations = ((ViewHunt)getActivity()).getCompletedLocationNames();}
        catch (Exception e){
            completedLocations = new ArrayList<>();
        }
        if(completedLocations!=null) {
            for (String doneLocation : completedLocations) {
                if (dlgTitle.equals(doneLocation)) {
                    return true;
                }
            }
        }
        return false;
    }
}
