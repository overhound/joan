package buzzmove.placestest.moyela.tito.timetoolay.buzzmoveplaces.data;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class PlaceResults {

    @SerializedName("name")
    String name;
    @SerializedName("formatted_address")
    String address;
    @SerializedName("icon")
    String iconURL;
    @SerializedName("types")
    List<String> placeType;
    @SerializedName("geometry")
    Geometry geometry;
    @SerializedName("id")
    private String id;
    @SerializedName("place_id")
    private String placeId;
    @SerializedName("rating")
    private Double rating;

    public PlaceResults(String name, String address, String iconURL, ArrayList<String> placeType, Geometry geometry, String id, String placeId, Double rating, String reference) {

    }

    @SerializedName("reference")
    private String reference;



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIconURL() {
        return iconURL;
    }

    public void setIconURL(String iconURL) {
        this.iconURL = iconURL;
    }

    public List<String> getPlaceType() {
        return placeType;
    }

    public void setPlaceType(List<String> placeType) {
        this.placeType = placeType;
    }
}
