package buzzmove.placestest.moyela.tito.timetoolay.buzzmoveplaces.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import buzzmove.placestest.moyela.tito.timetoolay.buzzmoveplaces.R;
import buzzmove.placestest.moyela.tito.timetoolay.buzzmoveplaces.controller.FindLocationDataService;
import buzzmove.placestest.moyela.tito.timetoolay.buzzmoveplaces.controller.RecyclerAdapter;
import buzzmove.placestest.moyela.tito.timetoolay.buzzmoveplaces.controller.RetroFitInstance;
import buzzmove.placestest.moyela.tito.timetoolay.buzzmoveplaces.data.PlaceResults;
import buzzmove.placestest.moyela.tito.timetoolay.buzzmoveplaces.data.PlacesList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {


    private final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private final int PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 2;
    protected GeoDataClient geoDataClient;
    private GoogleApiClient googleApiClient;
    private boolean locationPermissionGranted;
    private EditText searchField;
    private LinearLayout bottomSheetResults;
    private Location lastLocation;
    private double latitude, longitude;
    private GoogleMap map;
    private Marker marker;
    private LocationRequest locationRequest;
    private FloatingActionButton showLocationFab;
    private BottomSheetBehavior bottomSheetBehavior;
    private RecyclerView recyclerViewResults;
    private RecyclerAdapter recyclerAdapter;
    private ArrayList<PlaceResults> results;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        init();


    }

    private void init() {

        // Set up XML
        searchField = findViewById(R.id.search_field);
        showLocationFab = findViewById(R.id.get_location);

        bottomSheetResults = findViewById(R.id.bottom_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetResults);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        // Construct a GeoDataClient.
        geoDataClient = Places.getGeoDataClient(this);
        // Construct a PlaceDetectionClient.


        results = new ArrayList<>();
        recyclerViewResults = findViewById(R.id.recycler_view_results);
        recyclerAdapter = new RecyclerAdapter(this, results);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerViewResults.addItemDecoration(itemDecoration);
        recyclerViewResults.setLayoutManager(layoutManager);
        recyclerViewResults.setAdapter(recyclerAdapter);

        getLocationPermission();

        initMapFragment();
        initSearchListener();
        initBottomSheetCallback();
        buildMapsApiClient();
    }

    private void initBottomSheetCallback() {
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

                switch (newState) {
                    case BottomSheetBehavior.STATE_DRAGGING:
                        showLocationFab.animate().scaleX(0).scaleY(0).setDuration(300).start(); // hides the floating action button
                        break;
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        showLocationFab.animate().scaleX(1).scaleY(1).setDuration(300).start(); // shows the floating action button
                        break;
                    case BottomSheetBehavior.STATE_HIDDEN:
                        showLocationFab.animate().scaleX(1).scaleY(1).setDuration(300).start(); // shows the floating action button
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

    }

    /**
     * Ignore the cast error. This is just an androidx bug; it is still transformed by the jetifier.
     */
    private void initMapFragment() {
        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        Objects.requireNonNull(supportMapFragment).getMapAsync(this);

    }

    private void initSearchListener() {
        searchField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                switch (actionId) {
                    case EditorInfo.IME_ACTION_SEARCH:
                        launchSearchFlow(String.valueOf(searchField.getText()));
                        hideKeyboard();
                        return false;
                    default:
                        return false;
                }

            }
        });
    }

    private void launchSearchFlow(String text) {
        FindLocationDataService service = RetroFitInstance.getRetrofitInstance().create(FindLocationDataService.class);
        Call<PlacesList> call = service.getPlaceResults(text, String.valueOf(latitude + "," + longitude), getString(R.string.places_api_key));
        call.enqueue(new Callback<PlacesList>() {
            @Override
            public void onResponse(@NonNull Call<PlacesList> call, @NonNull Response<PlacesList> response) {

                if (response.isSuccessful()) {
                    updateUI();
                    for (int i = 0; i < response.body().getPlaceResults().size(); i++) {

                        MarkerOptions markerOptions = new MarkerOptions();
                        PlaceResults results = response.body().getPlaceResults().get(i);
                        double lat = Double.parseDouble(results.getGeometry().getLocation().getLat());
                        double lng = Double.parseDouble(results.getGeometry().getLocation().getLng());
                        String placeName = results.getName();
                        LatLng latLng = new LatLng(lat, lng);
                        markerOptions.position(latLng)
                                .title(placeName)
                                .snippet(String.valueOf(i))
                                .icon(getMarkerIcon(getResources().getColor(R.color.colorAccent)));
//
                        marker = map.addMarker(markerOptions);

                        map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                        map.animateCamera(CameraUpdateFactory.zoomTo(11));
                    }
                    generateDataList(response.body().getPlaceResults());

                }
            }

            @Override
            public void onFailure(@NonNull Call<PlacesList> call, @NonNull Throwable t) {
                Toast.makeText(MainActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void generateDataList(ArrayList<PlaceResults> results) {
        this.results.clear();
        this.results.addAll(results);
        recyclerAdapter.notifyDataSetChanged();
    }

    public BitmapDescriptor getMarkerIcon(int color) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        return BitmapDescriptorFactory.defaultMarker(hsv[0]);
    }

    private void updateUI() {
        map.clear();
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    private void hideKeyboard() {
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);

        if (inputManager != null) {
            inputManager.hideSoftInputFromWindow(Objects.requireNonNull(getCurrentFocus()).getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }


    public void onLocation(View view) {
        map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude())));
    }

    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        locationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationPermissionGranted = true;
                    initMapClient();
                }
            }
            case PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationPermissionGranted = true;
                    initMapClient();

                }
            }
        }


    }

    @SuppressLint("MissingPermission")
    private void initMapClient() {
        if (googleApiClient == null)
            buildMapsApiClient();
        map.setMyLocationEnabled(true);
    }

    private synchronized void buildMapsApiClient() {
        if (locationPermissionGranted) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
            googleApiClient.connect();
        }
    }


    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                try {
                    Fragment fragment = LocationDetailFragment.newInstance(results.get(Integer.parseInt(marker.getSnippet())));
                    getSupportFragmentManager().beginTransaction()
                            .addToBackStack("locationDetail")
                            .setCustomAnimations(R.anim.slide_up, FragmentTransaction.TRANSIT_NONE, FragmentTransaction.TRANSIT_NONE, R.anim.slide_down)
                            .replace(R.id.container_fragment, fragment, "locationDetailScreen")
                            .commit();
                } catch (NumberFormatException e) {
                    Toast.makeText(MainActivity.this, "Your location", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });
        if (locationPermissionGranted) {
            buildMapsApiClient();
            map.setMyLocationEnabled(true);

        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onConnected(Bundle bundle) {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(1000)
                .setFastestInterval(1000)
                .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (locationPermissionGranted)
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);

    }

    @Override
    public void onConnectionSuspended(int i) {
        googleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        lastLocation = location;
        if (marker != null) {
            marker.remove();
        }
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        LatLng latLng = new LatLng(latitude, longitude);
        MarkerOptions markerOptions = new MarkerOptions()
                .position(latLng)
                .title("Your location")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));

        marker = map.addMarker(markerOptions);
        map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        map.animateCamera(CameraUpdateFactory.zoomTo(11));
        if (googleApiClient != null)
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);

    }
}
