package net.as93.treasurehunt.controllers.dialogs;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.support.v4.app.DialogFragment;import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import net.as93.treasurehunt.R;
import net.as93.treasurehunt.models.Username;

public class SetUsernameDialog extends DialogFragment {

    public static final String FORECAST_STRING = "FORECAST_STRING";

    Username unObject;

    public static SetUsernameDialog newInstance(Context context, String forecast) {

        SetUsernameDialog setUsernameDialog = new SetUsernameDialog();
        Bundle args = new Bundle();
        args.putString(FORECAST_STRING, forecast);
        setUsernameDialog.setArguments(args);

        return setUsernameDialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_set_username, container, false);
        TextView forecastView = (TextView) view.findViewById(R.id.txtUsername);

        final Button btnUpdate = (Button) view.findViewById(R.id.btnUpdateUsername);
        final EditText txtUsername = (EditText) view.findViewById(R.id.txtUsername);


        unObject = new Username(getActivity());
        forecastView.setText(unObject.fetchUsername());


        btnUpdate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Check if username is valid and save is, success = true if username was saved.
                Boolean success = unObject.updateUsername(txtUsername.getText().toString());

                if (success) {
                    showToast("Your username has been updated to " + txtUsername.getText());
                    getDialog().dismiss(); // Close the dialog
                }
                else{
                    showToast("This username is not valid, pick another");
                }


            }
        });

        return view;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog =  super.onCreateDialog(savedInstanceState);
        dialog.setTitle("Update Saved Username");
        return dialog;
    }

    private void showToast(String message){
        Toast.makeText(
                getActivity(),
                message,
                Toast.LENGTH_SHORT)
                .show();
    }


}
