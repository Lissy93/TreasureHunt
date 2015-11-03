package net.as93.treasurehunt.utils.apiRequests;

import android.os.AsyncTask;

public class APIRequests extends AsyncTask {

    // The base URL constant for forming the end points
    private final String BASE_URL =
            "http://sots.brookes.ac.uk/~p0073862/services/hunt";

    // This is required to update the UI on postExecute
    ControllerThatMakesARequest parentFragment;


    /**
     * Constructor, just sets the class variable for parent fragment to
     * allow onPostExecute to call the update UI method
     * @param ctmr class that implements ControllerThatMakesARequest interface
     */
    public APIRequests(ControllerThatMakesARequest ctmr){
        this.parentFragment = ctmr;
    }


    /**
     * The actual network requests. This will always be overridden.
     * @param params Object of genericness
     * @return the result, well it will
     */
    @Override
    protected Object doInBackground(Object[] params) {
        return null; // This will always be overridden
    }


    /**
     * Executed after doInBackground is complete
     * Calls the update UI method on the parent fragment
     * @param result Object what was returned from doInBackground
     */
    @Override
    protected void onPostExecute(Object result) {
        parentFragment.thereAreResults(result); // Will update the UI with results
    }


    /**
     * Forms URL String for save hunts end point
     * @return String URL
     */
    protected final String getUrlForSaveHunts(){
        return BASE_URL+"/createhunt";
    }


    /**
     * Forms URL String for fetch all hunts end point
     * @return String URL
     */
    protected final String getUrlForFetchingAllHunts(){
        return BASE_URL+"/hunts";
    }

}
