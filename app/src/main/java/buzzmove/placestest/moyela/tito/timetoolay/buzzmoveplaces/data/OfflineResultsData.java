package buzzmove.placestest.moyela.tito.timetoolay.buzzmoveplaces.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class OfflineResultsData {
    private ArrayList<LatLng> latLng;
    private ArrayList<PlaceResults> placeResults;
    private SharedPreferences prefs;
    private final String MARKER_DATA = "markerData";
    private final String RESULTS_DATA = "resultsData";

    public OfflineResultsData(Context context) {
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        latLng = new ArrayList<>();
        placeResults = new ArrayList<>();
    }

    public boolean savePositions() {
        Gson gson = new Gson();
        String json = gson.toJson(latLng); // Convert the array to json...
        return prefs.edit().putString(MARKER_DATA, json).commit();

    }

    private boolean saveResults() {
        Gson gson = new Gson();
        String json = gson.toJson(placeResults); // Convert the array to json...
        return prefs.edit().putString(RESULTS_DATA, json).commit();

    }

    public ArrayList<LatLng> getMarkerPositions() {
        Gson gson = new Gson();
        String json = prefs.getString(MARKER_DATA, null); //Retrieve previously saved data
        if (json != null) {
            Type type = new TypeToken<ArrayList<LatLng>>() {
            }.getType();
            latLng = gson.fromJson(json, type); //Restore previous data

        }


        return latLng;

    }

    public ArrayList<PlaceResults> getPlaceResults() {
        Gson gson = new Gson();
        String json = prefs.getString(RESULTS_DATA, null); //Retrieve previously saved data
        if (json != null) {
            Type type = new TypeToken<ArrayList<PlaceResults>>() {
            }.getType();
            placeResults = gson.fromJson(json, type); //Restore previous data

        }


        return placeResults;

    }

    public void addPositions(LatLng latLng) {
        this.latLng.add(latLng);

    }

    public boolean savePlaceResults(ArrayList<PlaceResults> placeResults) {
        this.placeResults.addAll(placeResults);
        return saveResults();
    }

}
