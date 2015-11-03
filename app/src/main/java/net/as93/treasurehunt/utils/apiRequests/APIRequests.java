package net.as93.treasurehunt.utils.apiRequests;

import android.os.AsyncTask;

public class APIRequests extends AsyncTask {

    private final String BASE_URL =
            "http://sots.brookes.ac.uk/~p0073862/services/hunt";

    ControllerThatMakesARequest parentFragment; // This is required to update the UI on postExecute

    public APIRequests(ControllerThatMakesARequest ctmr){
        this.parentFragment = ctmr;
    }

    protected final String getUrlForSaveHunts(){
        return BASE_URL+"/createhunt";
    }

    protected final String getUrlForFetchingAllHunts(){
        return BASE_URL+"/hunts";
    }


    @Override
    protected void onPostExecute(Object result) {
        super.onPostExecute(result);
        parentFragment.thereAreResults(result);

    }

    @Override
    protected Object doInBackground(Object[] params) {
        return null;
    }
}
