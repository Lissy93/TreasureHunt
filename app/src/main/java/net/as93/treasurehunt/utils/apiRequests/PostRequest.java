package net.as93.treasurehunt.utils.apiRequests;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Handles all POST requests, like a BOSS
 */
public class PostRequest extends APIRequests {
    /**
     * Constructor, just sets the class variable for parent fragment to
     * allow onPostExecute to call the update UI method
     *
     * @param ctmr class that implements ControllerThatMakesARequest interface
     */
    public PostRequest(ControllerThatMakesARequest ctmr) {
        super(ctmr);
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

        return responseCode+"";
    }
}
