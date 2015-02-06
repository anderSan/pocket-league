package com.twobits.pocketleague.backend;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.twobits.pocketleague.R;

import java.util.List;

public class ListAdapter_Venue extends ArrayAdapter<Item_Venue> {
    public static final String LOGTAG = "ListAdapter_Venue";
    private Context context;
    private List<Item_Venue> venue_list;
    private View.OnClickListener cbClicked;

    public ListAdapter_Venue(Context context, int layoutResourceId, List<Item_Venue> data,
                             View.OnClickListener cbClicked) {
        super(context, layoutResourceId, data);
        this.context = context;
        this.venue_list = data;
        this.cbClicked = cbClicked;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder_Venue holder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context
                    .LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item_venue, parent, false);

            holder = new ViewHolder_Venue();
            holder.v_name = (TextView) convertView.findViewById(R.id.tv_v_name);
            holder.v_isFavorite = (CheckBox) convertView.findViewById(R.id.cb_v_isfavorite);
            holder.v_isFavorite.setOnClickListener(cbClicked);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder_Venue) convertView.getTag();
        }

        Item_Venue item = venue_list.get(position);
        holder.v_name.setText(item.getName());
        holder.v_isFavorite.setChecked(item.getIsFavorite());
        holder.v_isFavorite.setTag(item.getId());

        return convertView;
    }

    @Override
    public int getCount() {
        return venue_list.size();
    }
}

class ViewHolder_Venue {
    TextView v_name;
    CheckBox v_isFavorite;
}