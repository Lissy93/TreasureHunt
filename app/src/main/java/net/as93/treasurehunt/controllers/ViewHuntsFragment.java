package net.as93.treasurehunt.controllers;

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
import net.as93.treasurehunt.models.Item;

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
    private ArrayList<Item> items = new ArrayList<Item>();
    private ArrayAdapter<Item> itemsAdapter;


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
        itemsAdapter = new ArrayAdapter<Item>(getActivity(), android.R.layout.simple_list_item_1, items);
        itemsLst.setAdapter(itemsAdapter);

        ReaderTask readerTask = new ReaderTask();
        try {
            URL url = new URL("http://feeds.feedburner.com/NhsChoicesBehindTheHeadlines?format=xml");
            readerTask.execute(url);
        }
        catch (MalformedURLException ex){
            //this should not happen
            statusTxt.setText("Bad URL");
        }


        itemsLst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                launchBrowser(position);
            }
        });

        return view;
    }


    private void launchBrowser(int position) {
        Item item = items.get(position);
        String link = item.getLink();
        Uri uri = Uri.parse(link);
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(launchBrowser);
    }



    private class ReaderTask extends AsyncTask<URL, Void, ArrayList<Item>> {

        private String statusMessage;
        private XmlPullParser parser;

        @Override
        protected ArrayList<Item> doInBackground(URL... params) {
            statusMessage = "AsyncTask is working";
            ArrayList<Item> result = new ArrayList<Item>();
            try {
                XmlPullParserFactory factory =
                        XmlPullParserFactory.newInstance();
                parser = factory.newPullParser();
                parser.setInput(params[0].openStream(), null);
                while (!isEndDoc(parser)) {
                    if (isStartTag(parser, "item")) {
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
        protected void onPostExecute(ArrayList<Item> result) {
//            statusTxt.setText(statusMessage);
            items.clear();
            items.addAll(result);
            itemsAdapter.notifyDataSetChanged();
        }

        private boolean isStartTag(XmlPullParser parser, String name)
                throws XmlPullParserException {
            return (parser.getEventType() == XmlPullParser.START_TAG)
                    && parser.getName().equalsIgnoreCase(name);
        }

        private Item getItem(XmlPullParser parser)
                throws XmlPullParserException, IOException {
            String title = "???";
            String link = null;
            while (!isEndTag(parser, "item") && !isEndDoc(parser)) {
                if (isStartTag(parser, "title")) {
                    title = getTagText(parser, "title");
                } else if (isStartTag(parser, "link")) {
                    link = getTagText(parser, "link");
                }
                parser.next();
            }
            if (isEndTag(parser, "item")) {
                return new Item(title, link);
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
