package buzzmove.placestest.moyela.tito.timetoolay.buzzmoveplaces.controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import buzzmove.placestest.moyela.tito.timetoolay.buzzmoveplaces.R;
import buzzmove.placestest.moyela.tito.timetoolay.buzzmoveplaces.data.PlaceResults;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.CustomViewHolder> {

    private ArrayList<PlaceResults> dataList;
    private Context context;

    public RecyclerAdapter(Context context, ArrayList<PlaceResults> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    public RecyclerAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.location_list_item, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        holder.name.setText(dataList.get(position).getName());
        holder.address.setText(dataList.get(position).getAddress());
        holder.type.setText(convertType(dataList.get(position).getPlaceType()));
        Picasso.with(context).load(dataList.get(position).getIconURL()).placeholder(R.drawable.ic_location_pin).into(holder.icon);
    }

    private String convertType(List<String> type) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < type.size(); i++) {
            if (i < type.size() - 1) {
                stringBuilder.append(type.get(i)).append(", ");
            } else {
                stringBuilder.append(type.get(i));
            }
        }
        return stringBuilder.toString().replaceAll("_", " ");
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {

        public final View view;

        TextView name;
        TextView address;
        TextView type;
        ImageView icon;

        CustomViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            name = view.findViewById(R.id.title);
            address = view.findViewById(R.id.address);
            type = view.findViewById(R.id.place_type);
            icon = view.findViewById(R.id.icon);
        }
    }
}