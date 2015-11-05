package net.as93.treasurehunt.utils.apiRequests;


import net.as93.treasurehunt.models.Hunt;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public class GetReqFetchHunts extends GetRequest {

    private String strUsername = null;

    /**
     * Constructor for fetch ALL hunts
     * @param callingParent ControllerThatMakesRequest instance
     */
    public GetReqFetchHunts(ControllerThatMakesARequest callingParent) {
        super(callingParent);
    }

    /**
     * Constructor for fetch hunts just for a specific user
     * @param callingParent ControllerThatMakesRequest instance
     */
    public GetReqFetchHunts(ControllerThatMakesARequest callingParent, String strUsername) {
        super(callingParent);
        this.strUsername = strUsername;
    }


    /**
     * Gets a Hunt instance for a certain XML tag
     * @param parser XMLPullParser instance
     * @return Hunt object fully populated (hopefully)
     * @throws XmlPullParserException
     * @throws IOException
     */
    @Override
    protected Hunt getItem(XmlPullParser parser)
            throws XmlPullParserException, IOException {
        String huntName = "???";
        String creator = null;
        while (!isEndTag(parser, "hunt") && !isEndDoc(parser)) {
            if (isStartTag(parser, "name")) {
                huntName = getTagText(parser, "name");
            } else if (isStartTag(parser, "creator")) {
                creator = getTagText(parser, "creator");
            }
            parser.next();
        }
        if (isEndTag(parser, "hunt")) {
            if(filterByUsername(creator)){
                return new Hunt(huntName, creator);
            }
            else{ return null; }
        } else {
            return null;
        }
    }




    /**
     * If a username is specified then only return Hunts
     * that were created by that user
     * @return Boolean weather or not to include Hunt
     */
    private boolean filterByUsername(String creator) {
        return strUsername == null || strUsername.equals(creator);
    }


    /**
     * Main request
     * @param params Object[] does nothing, but requred to Override
     * @return Object ArrayList of Hunts, the results
     */
    @Override
    protected Object doInBackground(Object[] params) {
        return reusableGetRequest("hunt", getUrlForFetchingAllHunts());
    }

}
