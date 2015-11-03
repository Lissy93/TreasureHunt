package net.as93.treasurehunt.controllers.fragments;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import net.as93.treasurehunt.R;
import net.as93.treasurehunt.models.Username;
import net.as93.treasurehunt.utils.autocomplete.PlaceJsonParser;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class CreateHuntFragment extends Fragment implements GoogleApiClient.OnConnectionFailedListener {

    GoogleMap mapStart;
    GoogleMap mapFinish;
    GoogleApiClient mGoogleApiClient;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_create_hunt, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        final CreateHuntFragment act = this;

        TextWatcher tw = new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                placesTask = new PlacesTask(act);
                placesTask.execute(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };

        mapStart = ((SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.mapFragmentInnerStart)).getMap();

        mapFinish = ((SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.mapFragmentInnerFinish)).getMap();

        /* Google maps and places */
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addApi(Places.GEO_DATA_API)
                .build();

        txtStartLocation = (AutoCompleteTextView) getActivity().findViewById(R.id.placesAutocompleteStart);
        txtStartLocation.setThreshold(1);
        txtStartLocation.addTextChangedListener(tw);

        endPlaces = (AutoCompleteTextView) getActivity().findViewById(R.id.placesAutocompleteFinish);
        endPlaces.setThreshold(1);
        endPlaces.addTextChangedListener(tw);


        final Button button = (Button) getActivity().findViewById(R.id.btnNext);
        final EditText txtHuntName = (EditText) getActivity().findViewById(R.id.huntName);


        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String huntName = txtHuntName.getText().toString();
                String username = new Username(getActivity()).fetchUsername();

                if(huntName.length()<1){
                    Toast.makeText(getActivity(),
                            "Please enter a suitable hunt name",
                            Toast.LENGTH_LONG).show();
                }
                else {
                    PostTask rt = new PostTask(username, huntName);
                    rt.execute();
                }
            }
        });

    }

    AutoCompleteTextView txtStartLocation;
    AutoCompleteTextView endPlaces;
    PlacesTask placesTask;
    ParserTask parserTask;

    public String TAG = "DEBUG DUCK: ";

    /** A method to download json data from url */
    private String downloadUrl(String strUrl) throws IOException{
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            iStream = urlConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
            StringBuilder sb = new StringBuilder();
            String line;
            while( ( line = br.readLine()) != null){
                sb.append(line);
            }
            data = sb.toString();
            br.close();
        }catch(Exception e){
            Log.d(TAG, "Exception while downloading url"+e.toString());
        }finally{
            assert iStream != null;
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }


    public void updateMap(final String placeId, final GoogleMap map){
        Log.i(TAG, " starting place lookup");
        Places.GeoDataApi.getPlaceById(mGoogleApiClient, placeId)
                .setResultCallback(new ResultCallback<PlaceBuffer>() {
                    @Override
                    public void onResult(PlaceBuffer places) {

                        if (places.getStatus().isSuccess()) {

                            // Get place, name and lat+long
                            final Place myPlace = places.get(0);
                            Log.i(TAG, "Place found: " + myPlace.getName());
                            String placeName = myPlace.getName().toString();
                            LatLng latlng = myPlace.getLatLng();

                            // Select the map then clear it of previous markers
                            map.clear();

                            // Add new marker for the selected place
                            map.addMarker(new MarkerOptions()
                                    .position(latlng)
                                    .title(placeName));

                            // Zoom into the new marker
                            CameraPosition cameraPosition = new CameraPosition.Builder().target(latlng).zoom(15.0f).build();
                            CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
                            map.moveCamera(cameraUpdate);

                            // Hide the soft keyboard
                            EditText myEditText = (EditText) getActivity().findViewById(R.id.placesAutocompleteStart);
                            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
                                    Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(myEditText.getWindowToken(), 0);
                        }
                        else{ Log.i(TAG, "Hmm, place doesn't seem to be found...");  }
                        places.release();
                    }
                });

    }


    // Fetches all places from GooglePlaces AutoComplete Web Service
    private class PlacesTask extends AsyncTask<String, Void, String>{
        CreateHuntFragment mActivity;

        public PlacesTask(CreateHuntFragment activity) {
            mActivity = activity;
        }

        @Override
        protected String doInBackground(String... place) {
            String data = "";
            String key = "key=AIzaSyC6NOgE2-jDwRWeEf0Wy_3rKLtyhQcrbVI";
            String input="";

            try {
                input = "input=" + URLEncoder.encode(place[0], "utf-8");
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            }

            String types = "types=geocode";
            String sensor = "sensor=false";
            String parameters = input+"&"+types+"&"+sensor+"&"+key;
            String output = "json";
            String url = "https://maps.googleapis.com/maps/api/place/autocomplete/"+output+"?"+parameters;

            try{ //Fetch data from google web services
                data = downloadUrl(url);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }

            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            parserTask = new ParserTask(mActivity);
            parserTask.execute(result);
        }
    }
    /** A class to parse the Google Places in JSON format */
    private class ParserTask extends AsyncTask<String, Integer, List<HashMap<String,String>>>{
        JSONObject jObject;
        CreateHuntFragment mActivity;

        public ParserTask(CreateHuntFragment activity) {
            mActivity = activity;
        }


        @Override
        protected List<HashMap<String, String>> doInBackground(String... jsonData) {
            List<HashMap<String, String>> places = null;
            PlaceJsonParser placeJsonParser = new PlaceJsonParser();
            try{
                jObject = new JSONObject(jsonData[0]);
                places = placeJsonParser.parse(jObject);
            }catch(Exception e){
                Log.d("Exception",e.toString());
            }
            return places;
        }

        @Override
        protected void onPostExecute(List<HashMap<String, String>> result) {
            String[] from = new String[] { "description"};
            int[] to = new int[] { android.R.id.text1 };
            SimpleAdapter adapter = new SimpleAdapter(getActivity().getBaseContext(), result, android.R.layout.simple_list_item_1, from, to);

            txtStartLocation.setAdapter(adapter);
            txtStartLocation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                    String placeId = getPlaceID(arg0, arg2);
                    mActivity.updateMap(placeId, mapStart); // Call update map, and pass place id
                }
            });
            endPlaces.setAdapter(adapter);
            endPlaces.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                    String placeId = getPlaceID(arg0, arg2);
                    mActivity.updateMap(placeId, mapFinish); // Call update map, and pass place id
                }
            });

        }
    }

    private String getPlaceID(AdapterView<?> arg0, int arg2){
        // Find place id
        String placeId = "";
        HashMap mMap = (HashMap) arg0.getAdapter().getItem(arg2);
        for (Object o : mMap.keySet()) {
            String key = (String) o;
            String value = (String) mMap.get(key);
            if (key.equals("_id")) {
                placeId = value;
            }
        }
        return placeId;
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e(TAG, "Google Places API connection failed with error code: "
                + connectionResult.getErrorCode());

        Toast.makeText(getActivity(),
                "Google Places API connection failed with error code:" +
                        connectionResult.getErrorCode(),
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mGoogleApiClient != null)
            mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }

    class PostTask extends AsyncTask<String, String, String>{

        String huntName;
        String username;

        public PostTask(String strUsername, String strHuntName) {
            this.huntName = strHuntName;
            this.username = strHuntName;
        }

        String strUrl = "http://sots.brookes.ac.uk/~p0073862/services/hunt/createhunt/";

        @Override
        protected String doInBackground(String... uri) {
            int responseCode = 0;

            HttpURLConnection conn = null;

            String urlParameters  = "username="+username+"&huntname="+huntName;
            byte[] postData       = urlParameters.getBytes(StandardCharsets.UTF_8);
            int    postDataLength = postData.length;

            try {
                URL url = new URL(strUrl);
                conn= (HttpURLConnection) url.openConnection();
                conn.setDoOutput(true);
                conn.setInstanceFollowRedirects(false);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestProperty("charset", "utf-8");
                conn.setRequestProperty("Content-Length", Integer.toString(postDataLength));
                conn.setUseCaches(false);
                DataOutputStream wr = new DataOutputStream( conn.getOutputStream());
                wr.write( postData );

                responseCode = conn.getResponseCode();

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                assert conn != null;
                conn.disconnect();
            }

            return responseCode+"";
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if(result.equals("200")){
                huntWasSavedSccessfully();
            }
            else{
                huntNameWasTaken();
            }
        }
    }

    private void huntNameWasTaken(){
        Toast.makeText(getActivity(), "A Hunt with that name already exists", Toast.LENGTH_SHORT).show();
    }

    private void huntWasSavedSccessfully(){
        Toast.makeText(getActivity(), "Hunt was saved successfully", Toast.LENGTH_SHORT).show();
    }

}
