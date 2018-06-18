package buzzmove.placestest.moyela.tito.timetoolay.buzzmoveplaces.data;

import com.google.gson.annotations.SerializedName;

class Southwest {
    @SerializedName("lng")
    private String lng;
    @SerializedName("lat")
    private String lat;

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
