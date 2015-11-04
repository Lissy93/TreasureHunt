package net.as93.treasurehunt.utils.apiRequests;

import android.os.AsyncTask;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

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
     * All post requests use this method to make post request
     * @param postData Byte array of params
     * @param strUrl String url
     * @return Object results
     */
    protected Object reuasablePostRequest(byte[] postData, String strUrl){
        int responseCode = 0;

        HttpURLConnection conn = null;

        int    postDataLength = postData.length;

        try {
            URL url = new URL(strUrl); // Create URL object from endpoint
            conn= (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setInstanceFollowRedirects(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("charset", "utf-8");
            conn.setRequestProperty("Content-Length", Integer.toString(postDataLength));
            conn.setUseCaches(false);
            DataOutputStream wr = new DataOutputStream( conn.getOutputStream());
            wr.write( postData );
            responseCode = conn.getResponseCode();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            assert conn != null;
            conn.disconnect();
        }

        return responseCode+""; // Return the response code (200 = all cool)
    }


    /**
     * Forms URL String for save hunts end point
     * @return String URL
     */
    protected final String getUrlForSaveHunts(){
        return BASE_URL+"/createhunt";
    }


    /**
     * Forms URL String for save location or leg to a hunt
     * @return String URL
     */
    protected final String getUrlForSaveLocation(){
        return BASE_URL+"/addlocation";
    }


    /**
     * Forms URL String for fetch all hunts end point
     * @return String URL
     */
    protected final String getUrlForFetchingAllHunts(){
        return BASE_URL+"/hunts";
    }

}
