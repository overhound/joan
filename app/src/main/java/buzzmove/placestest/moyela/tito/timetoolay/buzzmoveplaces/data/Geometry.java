package buzzmove.placestest.moyela.tito.timetoolay.buzzmoveplaces.data;


import com.google.gson.annotations.SerializedName;

public class Geometry {
    @SerializedName("location")
    private Location location;
    @SerializedName("viewport")
    private Viewport viewPort;

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Viewport getViewPort() {
        return viewPort;
    }

    public void setViewPort(Viewport viewPort) {
        this.viewPort = viewPort;
    }
}
