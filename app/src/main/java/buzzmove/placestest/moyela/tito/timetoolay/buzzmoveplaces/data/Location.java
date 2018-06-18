package buzzmove.placestest.moyela.tito.timetoolay.buzzmoveplaces.data;

import com.google.gson.annotations.SerializedName;

public class Location {

    @SerializedName("lat")
    private String lat;
    @SerializedName("lng")
    private String lng;

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }
}
