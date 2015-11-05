package net.as93.treasurehunt.controllers;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import net.as93.treasurehunt.R;
import net.as93.treasurehunt.utils.apiRequests.ControllerThatMakesARequest;
import net.as93.treasurehunt.utils.apiRequests.PostReqAddLocation;

import java.util.HashMap;


public class AddLocationActivity extends AppCompatActivity
        implements ControllerThatMakesARequest{

    ControllerThatMakesARequest dis = this; // dis is this
    HashMap<String, EditText> elements; // A HasMap of EditText form elements

    // String list of all form elements on screen
    private String[] elementNames = { "name", "location", "position",
        "description", "latitude", "longitude", "question", "answer", "clue" };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_location);
        populateFormElementsMap();
        setHuntDetails();
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        Button btnSaveLeg = (Button) findViewById(R.id.btnSaveLocation);
        btnSaveLeg.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                PostReqAddLocation rt = new PostReqAddLocation(getValuesFromForm(), dis);
                rt.execute();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_view_hunt, menu);
        return true;
    }


    /**
     * Function populates the FormElements HashMap class variable
     * with reference to each EditText form element on screen
     */
    private void populateFormElementsMap(){
        elements = new HashMap<>(); // Initialise it
        for(String element: elementNames){
            elements.put(element,(
                (EditText) this.findViewById(getResources().getIdentifier(
                    "txt"+element.substring(0, 1).toUpperCase() + element.substring(1),
                    "id", getPackageName()
                    )
            )));
        }
    }


    /**
     * Sets the Hunt name and leg number in the text fields
     * Gets the data from the intent sent on previous screen
     * Disables text fields after text is changed
     */
    private void setHuntDetails(){
        // Get the hunt name and leg number from the intent
        String huntName = getIntent().getExtras().getString("huntname");
        int legNumber = getIntent().getExtras().getInt("leg");

        // Get the edit text fields that will need modifying
        EditText txtHuntName = (EditText) this.findViewById(R.id.txtName);
        EditText txtLegNumber = (EditText) this.findViewById(R.id.txtPosition);

        // Set the text for hunt name and leg number
        txtLegNumber.setText(legNumber + "");
        txtHuntName.setText(huntName);

        // Disable the text fields, since the user won't need to change anymore
        txtHuntName.setEnabled(false);
        txtLegNumber.setEnabled(false);
    }


    /**
     * Gets the text value from each of the EditText fields
     * @return HashMap of values
     */
    private HashMap getValuesFromForm(){

        HashMap<String, String> results = new HashMap<>(); // To return

        for(String element: elementNames) {
            results.put(element, elements.get(element).getText().toString());
        }

        return results;
    }


    /**
     * Resets each EditText, and populates the hunt name and new position
     * Called after a location leg is saved successfully
     */
    private void updateFieldsAfterSave(){
        // Get the hunt name and leg, as they will need to stay
        String currentHuntName = elements.get("name").getText().toString();
        String currentLeg = elements.get("position").getText().toString();

        // Clear all text fields values
        for (EditText textField : elements.values()) {
            textField.setText("");
        }

        // Get the new position
        String nextLeg = (Integer.parseInt(currentLeg)+1)+"";

        // Put the hunt name and leg back into edittext
        elements.get("name").setText(currentHuntName);
        elements.get("position").setText(nextLeg);
    }


    /**
     * Called when results are returned from the web service
     * @param results String the response code returned from web service
     */
    @Override
    public void thereAreResults(Object results) {
        if(results.equals("200")){ // Success!
            this.recreate();
            updateFieldsAfterSave();
            showToast("Location saved successfully");
        }
        else{ // That didn't work :'(
            showToast("There was an error with the information you entered");
        }
    }


    /**
     * Just shows a Toast, less code repetition to put it here
     * @param message String the message to show
     */
    private void showToast(String message){
        Toast.makeText(
                getApplicationContext(),
                message,
                Toast.LENGTH_LONG
        ).show();
    }
}
