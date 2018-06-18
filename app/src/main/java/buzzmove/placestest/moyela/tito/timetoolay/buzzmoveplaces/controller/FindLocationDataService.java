package buzzmove.placestest.moyela.tito.timetoolay.buzzmoveplaces.controller;

import buzzmove.placestest.moyela.tito.timetoolay.buzzmoveplaces.data.PlacesList;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface FindLocationDataService {
    @GET("maps/api/place/textsearch/json")
    Call<PlacesList> getPlaceResults(@Query("query") String input, @Query("location") String latlng, @Query("key") String key);
}
