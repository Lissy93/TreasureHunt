package net.as93.treasurehunt.controllers;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.widget.EditText;

import net.as93.treasurehunt.R;


public class AddLocationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_location);

        setHuntDetails();
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
}
