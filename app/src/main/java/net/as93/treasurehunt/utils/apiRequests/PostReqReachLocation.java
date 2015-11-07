package net.as93.treasurehunt.utils.apiRequests;

import java.nio.charset.StandardCharsets;


public class PostReqReachLocation extends PostRequest {

    String huntName;
    String username;
    String locationName;
    String dateTime;

    public PostReqReachLocation(ControllerThatMakesARequest ctmr,
                                String huntName, String username,
                                String locationName, String dateTime) {
        super(ctmr);
        this.huntName = huntName;
        this.username = username;
        this.locationName = locationName;
        this.dateTime = dateTime;
    }

    @Override
    protected Object doInBackground(Object[] params) {
        return reuasablePostRequest(makeParams(), getEndPoints().getUrlForReachLocation());
    }


    /**
     * Generates a byte array containing the URL params from the class variables
     * @return byte[] url prarams ready for sending
     */
    private byte[] makeParams(){
        String urlParameters  = "huntname="+huntName
                +"&username="+username
                +"&locationname="
                +locationName
                +"&date="+dateTime;
        return urlParameters.getBytes(StandardCharsets.UTF_8);
    }
}
