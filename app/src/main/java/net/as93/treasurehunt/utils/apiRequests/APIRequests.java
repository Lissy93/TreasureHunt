package net.as93.treasurehunt.utils.apiRequests;

import android.os.AsyncTask;

public class APIRequests extends AsyncTask {

    // This is required to update the UI on postExecute
    ControllerThatMakesARequest parentFragment;

    // For accessing the URL end points
    APIEndPoints endPoints = new APIEndPoints();


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
     * Getter for accessing the API end points
     * @return Instance of APIEndPoints
     */
    protected APIEndPoints getEndPoints(){
        return endPoints;
    }


}
