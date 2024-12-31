package com.example.fardinlab31;



import android.content.Context;
import android.media.metrics.Event;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CustomEventAdapter extends ArrayAdapter<item> {

    private final Context context;
    private final ArrayList<item> values;

    public CustomEventAdapter(@NonNull Context context, @NonNull ArrayList<item> objects) {
        super(context, -1, objects);
        this.context = context;
        this.values = objects;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.row_item, parent, false);

        TextView serial = rowView.findViewById(R.id.tvSN);
        TextView itemName = rowView.findViewById(R.id.tvItemName);
        TextView Date = rowView.findViewById(R.id.tvDate);
        TextView cost = rowView.findViewById(R.id.tvCost);

        serial.setText(String.valueOf(position+1));
        itemName.setText(values.get(position).itemName.toString());
        Date.setText(getFormattedDate(values.get(position).date));
        cost.setText(values.get(position).cost.toString());

        return rowView;
    }

    private String getFormattedDate(long ms) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(ms);

        String formattedDate = sdf.format(date);
        return formattedDate;
    }
}
