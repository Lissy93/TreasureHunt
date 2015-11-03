package net.as93.treasurehunt.utils.apiRequests;

import net.as93.treasurehunt.models.Hunt;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class ReqFetchAllHunts extends APIRequests implements ControllerThatMakesARequest {

    private String statusMessage;
    private XmlPullParser parser;


    /**
     * Constructor, sets the class variables
     * @param callingParent ControllerThatMakesRequest instance
     */
    public ReqFetchAllHunts(ControllerThatMakesARequest callingParent) {
        super(callingParent);
    }

    protected Object doInBackground(Object[] params) {
        ArrayList<Hunt> result = new ArrayList<Hunt>();
        try {

            URL url = new URL(getUrlForFetchingAllHunts());

            XmlPullParserFactory factory =
                    XmlPullParserFactory.newInstance();
            parser = factory.newPullParser();
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

        return result;
    }

    private boolean isStartTag(XmlPullParser parser, String name)
            throws XmlPullParserException {
        return (parser.getEventType() == XmlPullParser.START_TAG)
                && parser.getName().equalsIgnoreCase(name);
    }

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
            return new Hunt(huntName, creator);
        } else {
            return null;
        }
    }

    private boolean isEndDoc(XmlPullParser parser)
            throws XmlPullParserException {
        return parser.getEventType() == XmlPullParser.END_DOCUMENT;
    }

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

    private boolean isEndTag(XmlPullParser parser, String name)
            throws XmlPullParserException {
        return (parser.getEventType() == XmlPullParser.END_TAG)
                && parser.getName().equals(name);

    }

    @Override
    public void thereAreResults(Object results) {

    }
}
