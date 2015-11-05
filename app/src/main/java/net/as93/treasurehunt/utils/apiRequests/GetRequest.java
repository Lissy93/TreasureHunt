package net.as93.treasurehunt.utils.apiRequests;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Alicia on 05/11/2015.
 */
public class GetRequest extends APIRequests {


    /**
     * Constructor, just sets the class variable for parent fragment to
     * allow onPostExecute to call the update UI method
     * @param ctmr class that implements ControllerThatMakesARequest interface
     */
    public GetRequest(ControllerThatMakesARequest ctmr) {
        super(ctmr);
    }


    /**
     * The main method, a generic get request block
     * @param tagName the name of the tag we're trying to get
     * @return
     */
    protected Object reusableGetRequest(String tagName, String strUrl){
        ArrayList<Object> result = new ArrayList<>();
        String statusMessage = "success";
        try {

            URL url = new URL(strUrl);

            XmlPullParserFactory factory =
                    XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(url.openStream(), null);
            while (!isEndDoc(parser)) {
                if (isStartTag(parser, tagName)) {
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
    protected boolean isStartTag(XmlPullParser parser, String name)
            throws XmlPullParserException {
        return (parser.getEventType() == XmlPullParser.START_TAG)
                && parser.getName().equalsIgnoreCase(name);
    }


    /**
     * Gets a Object instance for a certain XML tag
     * @param parser XMLPullParser instance
     * @return Object fully populated (hopefully)
     * @throws XmlPullParserException
     * @throws IOException
     */
    protected Object getItem(XmlPullParser parser)
            throws XmlPullParserException, IOException {
        return null;
    }


    /**
     * Determines if previous tag is at the end of the document
     * @param parser XMLPullParser instance
     * @return Boolean true if end of document
     * @throws XmlPullParserException
     */
    protected boolean isEndDoc(XmlPullParser parser)
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
    protected String getTagText(XmlPullParser parser, String name)
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
    protected boolean isEndTag(XmlPullParser parser, String name)
            throws XmlPullParserException {
        return (parser.getEventType() == XmlPullParser.END_TAG)
                && parser.getName().equals(name);

    }


    /**
     * Removes any empty of null values from ArrayList
     */
    private ArrayList<Object> removeNullValues(ArrayList<Object> startResults){
        while(startResults.remove(null)); //TODO can this be more efficient? what if there are 10,000 hunts on demo day?
        return startResults;
    }
}
