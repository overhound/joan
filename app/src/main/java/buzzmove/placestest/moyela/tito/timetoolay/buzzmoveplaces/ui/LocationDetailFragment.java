package buzzmove.placestest.moyela.tito.timetoolay.buzzmoveplaces.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import buzzmove.placestest.moyela.tito.timetoolay.buzzmoveplaces.R;
import buzzmove.placestest.moyela.tito.timetoolay.buzzmoveplaces.data.PlaceResults;

public class LocationDetailFragment extends Fragment {

    private PlaceResults placeResults;
    private ImageView closeButton;

    static LocationDetailFragment newInstance(PlaceResults placeResults) {
        LocationDetailFragment fragment = new LocationDetailFragment();
        fragment.initResults(placeResults);
        return fragment;
    }

    private void initResults(PlaceResults placeResults) {

        this.placeResults = placeResults;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.location_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);

    }

    private void init(View view) {
        ImageView icon = view.findViewById(R.id.icon_detail);
        TextView name = view.findViewById(R.id.name_detail);
        TextView address = view.findViewById(R.id.address_detail);
        closeButton = view.findViewById(R.id.close_button);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        if (placeResults != null) {
            name.setText(placeResults.getName());
            address.setText(placeResults.getAddress());
            Picasso.with(requireActivity()).load(placeResults.getIconURL()).placeholder(R.drawable.ic_location_pin).into(icon);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

}
