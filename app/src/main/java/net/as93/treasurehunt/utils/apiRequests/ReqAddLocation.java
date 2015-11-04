package net.as93.treasurehunt.utils.apiRequests;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;


public class ReqAddLocation extends APIRequests {

    HashMap legDetails;

    /**
     * Constructor, sets the class variables
     * @param formValues a HashMap of all the values from the nine form elements
     * @param callingParent ControllerThatMakesRequest instance
     */
    public ReqAddLocation(HashMap formValues, ControllerThatMakesARequest callingParent) {
        super(callingParent);
        this.legDetails = formValues;
    }

    @Override
    protected Object doInBackground(Object[] params) {
        return reuasablePostRequest(makeParams(), getUrlForSaveLocation());
    }


    /**
     * Generates a byte array containing the URL params from the class variables
     * @return byte[] url prarams ready for sending
     */
    private byte[] makeParams(){
        String urlParameters  = "";
        urlParameters += "huntname="+legDetails.get("name");
        urlParameters += "&locationname="+legDetails.get("location");
        urlParameters += "&position="+legDetails.get("position");
        urlParameters += "&description="+legDetails.get("description");
        urlParameters += "&latitude="+legDetails.get("latitude");
        urlParameters += "&longitude="+legDetails.get("longitude");
        urlParameters += "&question="+legDetails.get("question");
        urlParameters += "&answer="+legDetails.get("answer");
        urlParameters += "&clue="+legDetails.get("clue");

        return urlParameters.getBytes(StandardCharsets.UTF_8);
    }
}
