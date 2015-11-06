package net.as93.treasurehunt.utils.apiRequests;

import net.as93.treasurehunt.models.Leg;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public class GetReqReachedLocations extends GetRequest {

    private String huntName;
    private String playerName;

    /**
     * Constructor
     * @param callingParent ControllerThatMake  sRequest instance
     */
    public GetReqReachedLocations(ControllerThatMakesARequest callingParent, String huntName, String playerName) {
        super(callingParent);
        this.huntName = huntName;
        this.playerName = playerName;
    }


    /**
     * Gets a Leg instance for a certain XML tag
     * @param parser XMLPullParser instance
     * @return Leg object fully populated (hopefully)
     * @throws XmlPullParserException
     * @throws IOException
     */
    @Override
    protected String getItem(XmlPullParser parser)
            throws XmlPullParserException, IOException {
        String valName = null;
        while (!isEndTag(parser, "location") && !isEndDoc(parser)) {
            if (isStartTag(parser, "name"))  valName = getTagText(parser, "name");
            parser.next();
        }
        if (isEndTag(parser, "location")) {
            return valName;
        } else {
            return null;
        }
    }


    /**
     * Main request
     * @param params Object[] does nothing, but requred to Override
     * @return Object ArrayList of Legs, the results
     */
    @Override
    protected Object doInBackground(Object[] params) {
        return reusableGetRequest("location", getEndPoints().getUrlForFetchingCompletedLegsOfHunt(huntName, playerName));
    }

}
