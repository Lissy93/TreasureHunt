package net.as93.treasurehunt.utils.apiRequests;

import java.nio.charset.StandardCharsets;


public class PostReqRegisterPlayer extends PostRequest {

    private String playerName;
    private String huntName;

    /**
     * Constructor
     * @param playerName The string value of the players username
     * @param huntName The string value of the hunt name
     * @param callingParent ControllerThatMakesRequest instance
     */
    public PostReqRegisterPlayer(String playerName, String huntName, ControllerThatMakesARequest callingParent) {
        super(callingParent);
        this.playerName = playerName;
        this.huntName = huntName;
    }

    @Override
    protected Object doInBackground(Object[] params) {
        return reuasablePostRequest(makeParams(), getEndPoints().getUrlForRegisteringPlayer());
    }


    /**
     * Generates a byte array containing the URL params from the class variables
     * @return byte[] url prarams ready for sending
     */
    private byte[] makeParams(){
        String urlParameters  = "";
        urlParameters += "huntname="+huntName;
        urlParameters += "&username="+playerName;

        return urlParameters.getBytes(StandardCharsets.UTF_8);
    }
}
