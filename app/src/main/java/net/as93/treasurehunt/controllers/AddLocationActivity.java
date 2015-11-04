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
import net.as93.treasurehunt.utils.apiRequests.ReqAddLocation;
import net.as93.treasurehunt.utils.apiRequests.ReqSaveHunt;

import java.util.HashMap;


public class AddLocationActivity extends AppCompatActivity
        implements ControllerThatMakesARequest{

    ControllerThatMakesARequest dis = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_location);

        setHuntDetails();
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        Button btnSaveLeg = (Button) findViewById(R.id.btnSaveLocation);
        btnSaveLeg.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ReqAddLocation rt = new ReqAddLocation(getValuesFromForm(), dis);
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
        txtLegNumber.setText(legNumber+"");
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

        results.put("name",
            ((EditText)this.findViewById(R.id.txtName)).getText().toString()
        );
        results.put("location",
            ((EditText)this.findViewById(R.id.txtLocation)).getText().toString()
        );
        results.put("position",
            ((EditText)this.findViewById(R.id.txtPosition)).getText().toString()
        );
        results.put("description",
            ((EditText)this.findViewById(R.id.txtDescription)).getText().toString()
        );
        results.put("latitude",
            ((EditText)this.findViewById(R.id.txtLatitude)).getText().toString()
        );
        results.put("longitude",
            ((EditText)this.findViewById(R.id.txtLongitude)).getText().toString()
        );
        results.put("question",
            ((EditText)this.findViewById(R.id.txtQuestion)).getText().toString()
        );
        results.put("answer",
            ((EditText)this.findViewById(R.id.txtAnswer)).getText().toString()
        );
        results.put("clue",
            ((EditText)this.findViewById(R.id.txtClue)).getText().toString()
        );

        return results;

    }

    @Override
    public void thereAreResults(Object results) {
        Toast.makeText(getApplicationContext(), ((String)results), Toast.LENGTH_SHORT).show();
    }
}
