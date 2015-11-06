package net.as93.treasurehunt.utils.apiRequests;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public class GetReqPlayersOnHunt extends GetRequest {

    private String huntName = null;

    /**
     * Constructor for fetching all players on a hunt
     * @param callingParent ControllerThatMakesRequest instance
     */
    public GetReqPlayersOnHunt(ControllerThatMakesARequest callingParent, String huntName) {
        super(callingParent);
        this.huntName = huntName;
    }


    /**
     * Gets a user string for a certain XML tag
     * @param parser XMLPullParser instance
     * @return Hunt object fully populated (hopefully)
     * @throws XmlPullParserException
     * @throws IOException
     */
    @Override
    protected String getItem(XmlPullParser parser) throws XmlPullParserException, IOException {
        String username = "???";
        while (!isEndTag(parser, "users") && !isEndDoc(parser)) {
            username = getTagText(parser, "user");
            parser.next();
        }
        return username;
    }


    /**
     * Main request
     * @param params Object[] does nothing, but requred to Override
     * @return Object ArrayList of Hunts, the results
     */
    @Override
    protected Object doInBackground(Object[] params) {
        return reusableGetRequest("user", getEndPoints().getUrlForFetchingPlayersOnHunt(huntName));
    }

}
