package net.as93.treasurehunt.utils.apiRequests;

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
    public ReqSaveHunt(String strUsername, String strHuntName,
                       ControllerThatMakesARequest callingParent){
        super(callingParent);
        this.huntName = strHuntName;
        this.username = strUsername;
    }


    @Override
    protected Object doInBackground(Object[] params) {
        return reuasablePostRequest(makeParams(), getUrlForSaveHunts());
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
