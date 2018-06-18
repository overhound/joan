package buzzmove.placestest.moyela.tito.timetoolay.buzzmoveplaces.data;

import com.google.gson.annotations.SerializedName;

public class Viewport {
    @SerializedName("southwest")
    private Southwest southwest;
    @SerializedName("northeast")
    private NorthEast northEast;

    public Southwest getSouthwest() {
        return southwest;
    }

    public void setSouthwest(Southwest southwest) {
        this.southwest = southwest;
    }

    public NorthEast getNorthEast() {
        return northEast;
    }

    public void setNorthEast(NorthEast northEast) {
        this.northEast = northEast;
    }
}
