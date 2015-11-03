package net.as93.treasurehunt.utils.apiRequests;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;


public class ReqSaveHunt extends APIRequests {

    String huntName; // The user chosen unique text identifier for hunt
    String username; // Username (from prefs)

    /**
     * Constructor, sets the class variables
     * @param strUsername String users name
     * @param strHuntName String hunt name
     * @param callingParent ControllerThatMakesRequest instance
     */
    public ReqSaveHunt(String strUsername, String strHuntName, ControllerThatMakesARequest callingParent) {
        super(callingParent);
        this.huntName = strHuntName;
        this.username = strUsername;
    }

    @Override
    protected String doInBackground(String... uri) {
        int responseCode = 0;

        HttpURLConnection conn = null;

        byte[] postData = makeParams();
        int    postDataLength = postData.length;

        try {
            URL url = new URL(getUrlForSaveHunts()); // Create URL object from endpoint
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


    /**
     * Generates a byte array containing the URL params from the class variables
     * @return byte[] url prarams ready for sending
     */
    private byte[] makeParams(){
        String urlParameters  = "username="+username+"&huntname="+huntName;
        return urlParameters.getBytes(StandardCharsets.UTF_8);
    }
}
