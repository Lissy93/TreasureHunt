package net.as93.treasurehunt.controllers;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;

import net.as93.treasurehunt.R;


public class AddLocationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_location);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_view_hunt, menu);
        return true;
    }
}
