package net.as93.treasurehunt.utils.apiRequests;

import net.as93.treasurehunt.models.Leg;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public class GetReqFetchLegs extends GetRequest {

    String huntName;

    /**
     * Constructor
     * @param callingParent ControllerThatMakesRequest instance
     */
    public GetReqFetchLegs(ControllerThatMakesARequest callingParent, String huntName) {
        super(callingParent);
        this.huntName = huntName;
    }


    /**
     * Gets a Leg instance for a certain XML tag
     * @param parser XMLPullParser instance
     * @return Leg object fully populated (hopefully)
     * @throws XmlPullParserException
     * @throws IOException
     */
    @Override
    protected Leg getItem(XmlPullParser parser)
            throws XmlPullParserException, IOException {

        String valName = null;
        String valPosition = null;
        String valDescription = null;
        String valLatitude = null;
        String valLongitude = null;
        String valQuestion = null;
        String valAnswer = null;
        String valClue = null;

        while (!isEndTag(parser, "location") && !isEndDoc(parser)) {
            if (isStartTag(parser, "name")) {
                valName = getTagText(parser, "name");
            }
            else if (isStartTag(parser, "position")) {
                valPosition = getTagText(parser, "position");
            }
            else if (isStartTag(parser, "description")) {
                valDescription = getTagText(parser, "description");
            }
            else if (isStartTag(parser, "latitude")) {
                valLatitude = getTagText(parser, "latitude");
            }
            else if (isStartTag(parser, "longitude")) {
                valLongitude = getTagText(parser, "longitude");
            }
            else if (isStartTag(parser, "question")) {
                valQuestion = getTagText(parser, "question");
            }
            else if (isStartTag(parser, "answer")) {
                valAnswer = getTagText(parser, "answer");
            }
            else if (isStartTag(parser, "clue")) {
                valClue = getTagText(parser, "clue");
            }

            parser.next();
        }
        if (isEndTag(parser, "location")) {
            return new Leg(valName, valPosition, valDescription, valLatitude, valLongitude, valQuestion, valAnswer, valClue);

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
        return reusableGetRequest("location", getUrlForFetchingLegsOfHunt(huntName));
    }

}
