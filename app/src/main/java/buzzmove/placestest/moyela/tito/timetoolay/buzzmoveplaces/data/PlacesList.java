package buzzmove.placestest.moyela.tito.timetoolay.buzzmoveplaces.data;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class PlacesList {

    @SerializedName("results")
    private ArrayList<PlaceResults> placeResults;
    @SerializedName("next_page_token")
    private String nextPageToken;

    public String getNextPageToken() {
        return nextPageToken;
    }

    public void setNextPageToken(String nextPageToken) {
        this.nextPageToken = nextPageToken;
    }

    public ArrayList<PlaceResults> getPlaceResults() {
        return placeResults;
    }

    public void setPlaceResults(ArrayList<PlaceResults> placeResults) {
        this.placeResults = placeResults;
    }

}
