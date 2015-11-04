package net.as93.treasurehunt.utils.apiRequests;

import net.as93.treasurehunt.models.Hunt;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class ReqFetchHunts extends APIRequests {

    private String strUsername = null;

    /**
     * Constructor for fetch ALL hunts
     * @param callingParent ControllerThatMakesRequest instance
     */
    public ReqFetchHunts(ControllerThatMakesARequest callingParent) {
        super(callingParent);
    }

    /**
     * Constructor for fetch hunts just for a specific user
     * @param callingParent ControllerThatMakesRequest instance
     */
    public ReqFetchHunts(ControllerThatMakesARequest callingParent, String strUsername) {
        super(callingParent);
        this.strUsername = strUsername;
    }


    /**
     * Main request
     * @param params Object[] does nothing, but requred to Override
     * @return Object ArrayList of Hunts, the results
     */
    @Override
    protected Object doInBackground(Object[] params) {
        ArrayList<Hunt> result = new ArrayList<Hunt>();
        String statusMessage;
        try {

            URL url = new URL(getUrlForFetchingAllHunts());

            XmlPullParserFactory factory =
                    XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(url.openStream(), null);
            while (!isEndDoc(parser)) {
                if (isStartTag(parser, "hunt")) {
                    result.add(getItem(parser));
                }
                parser.next();
            }

        } catch (XmlPullParserException e) {
            statusMessage = "Failed to create parser";
        } catch (IOException e) {
            statusMessage = "IOException reading feed";
        }

        result = removeNullValues(result);

        return result;
    }


    /**
     * Determines if text is start tag
     * @param parser XMLPullParser instance
     * @param name String the tag name
     * @return Boolean wheather it is or isn't a tag
     * @throws XmlPullParserException
     */
    private boolean isStartTag(XmlPullParser parser, String name)
            throws XmlPullParserException {
        return (parser.getEventType() == XmlPullParser.START_TAG)
                && parser.getName().equalsIgnoreCase(name);
    }


    /**
     * Gets a Hunt instance for a certain XML tag
     * @param parser XMLPullParser instance
     * @return Hunt object fully populated (hopefully)
     * @throws XmlPullParserException
     * @throws IOException
     */
    private Hunt getItem(XmlPullParser parser)
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
     * Determines if previous tag is at the end of the document
     * @param parser XMLPullParser instance
     * @return Boolean true if end of document
     * @throws XmlPullParserException
     */
    private boolean isEndDoc(XmlPullParser parser)
            throws XmlPullParserException {
        return parser.getEventType() == XmlPullParser.END_DOCUMENT;
    }


    /**
     * Reads the text contents of a specified tag
     * @param parser XMLPullParser instance
     * @param name String name of the tag
     * @return String contents of the tag
     * @throws XmlPullParserException
     * @throws IOException
     */
    private String getTagText(XmlPullParser parser, String name)
            throws XmlPullParserException, IOException {
        String result = null;
        while (!isEndTag(parser, name) && !isEndDoc(parser)) {
            if (parser.getEventType() == XmlPullParser.TEXT) {
                result = parser.getText();
            }
            parser.next();
        }
        return result;
    }


    /**
     * Determines if at end of tag
     * @param parser XMLPullParser instance
     * @param name String name of tag
     * @return Boolean true if end of tag
     * @throws XmlPullParserException
     */
    private boolean isEndTag(XmlPullParser parser, String name)
            throws XmlPullParserException {
        return (parser.getEventType() == XmlPullParser.END_TAG)
                && parser.getName().equals(name);

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
     * Removes any empty of null values from ArrayList
     */
    private ArrayList<Hunt> removeNullValues(ArrayList<Hunt> startResults){
        while(startResults.remove(null)); //TODO can this be more efficient? what if there are 10,000 hunts on demo day?
        return startResults;
    }
}
