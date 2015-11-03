package net.as93.treasurehunt.controllers.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import net.as93.treasurehunt.R;
import net.as93.treasurehunt.controllers.ViewHunt;
import net.as93.treasurehunt.models.Hunt;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class ViewHuntsFragment extends Fragment {

    public final static String TYPE_OF_HUNTS = "net.as93.treasurehunt.TYPE_OF_HUNTS";

    private TextView statusTxt;
    private ListView itemsLst;
    private ArrayList<Hunt> hunts = new ArrayList<Hunt>();
    private ArrayAdapter<Hunt> itemsAdapter;


    public ViewHuntsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_hunts, container, false);


        TextView tvTitle = (TextView) view.findViewById(R.id.viewHuntsTitle);
        char typeOfHunt = getArguments().getChar(TYPE_OF_HUNTS, 'a');
        if(typeOfHunt == 'a'){
            tvTitle.setText("All Treasure Hunts");
        }
        else if(typeOfHunt == 'm'){
            tvTitle.setText("My Treasure Hunts");
        }

        itemsLst = (ListView) view.findViewById(R.id.items_lst);
        itemsAdapter = new ArrayAdapter<Hunt>(getActivity(), android.R.layout.simple_list_item_1, hunts);
        itemsLst.setAdapter(itemsAdapter);

        ReaderTask readerTask = new ReaderTask();
        try {
            URL url = new URL("http://sots.brookes.ac.uk/~p0073862/services/hunt/hunts");
            readerTask.execute(url);
        }
        catch (MalformedURLException ex){
            //this should not happen
            statusTxt.setText("Bad URL");
        }


        itemsLst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                launchBrowser(position);

                Intent intent = new Intent(getActivity(), ViewHunt.class);
                // get some data here yes
//                intent.putExtra(EXTRA_MESSAGE, message);
                startActivity(intent);
            }
        });

        return view;
    }


    private class ReaderTask extends AsyncTask<URL, Void, ArrayList<Hunt>> {

        private String statusMessage;
        private XmlPullParser parser;

        @Override
        protected ArrayList<Hunt> doInBackground(URL... params) {
            statusMessage = "AsyncTask is working";
            ArrayList<Hunt> result = new ArrayList<Hunt>();
            try {
                XmlPullParserFactory factory =
                        XmlPullParserFactory.newInstance();
                parser = factory.newPullParser();
                parser.setInput(params[0].openStream(), null);
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


        @Override
        protected void onPostExecute(ArrayList<Hunt> result) {
//            statusTxt.setText(statusMessage);
            hunts.clear();
            hunts.addAll(result);
            itemsAdapter.notifyDataSetChanged();
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

    }



//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//    }

}
