package net.as93.treasurehunt.utils.apiRequests;

import android.os.AsyncTask;

public class APIRequests extends AsyncTask<String, String, String> {

    private final String BASE_URL =
            "http://sots.brookes.ac.uk/~p0073862/services/hunt";

    ControllerThatMakesARequest parentFragment; // This is required to update the UI on postExecute

    public APIRequests(ControllerThatMakesARequest ctmr){
        this.parentFragment = ctmr;
    }

    public final String getUrlForSaveHunts(){
        return BASE_URL+"/createhunt";
    }

    @Override
    protected String doInBackground(String... params) {
        return null; // This will be overridden in the subclasses
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        parentFragment.thereAreResults(result);
    }

}
