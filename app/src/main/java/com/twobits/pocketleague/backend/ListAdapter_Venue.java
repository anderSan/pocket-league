package com.twobits.pocketleague.backend;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.twobits.pocketleague.R;

import java.util.List;

public class ListAdapter_Venue extends ArrayAdapter<Item_Venue> {
	private static final String LOGTAG = "ListAdapter_Venue";
	private Context context;
	private List<Item_Venue> venue_list;

	public ListAdapter_Venue(Context context, int layoutResourceId, List<Item_Venue> data) {
        super(context, layoutResourceId, data);
		this.context = context;
		this.venue_list = data;
	}

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder_Venue holder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item_venue, null);

            holder = new ViewHolder_Venue();
            holder.v_id = (TextView) convertView.findViewById(R.id.tv_v_id);
            holder.v_name = (TextView) convertView.findViewById(R.id.tv_v_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder_Venue) convertView.getTag();
        }

        holder.v_id.setText(venue_list.get(position).getId());
        holder.v_name.setText(venue_list.get(position).getName());

        return convertView;
    }

    @Override
    public int getCount() {
        return venue_list.size();
    }

    @Override
    public Item_Venue getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }
}

class ViewHolder_Venue {
    int position;
    TextView v_id;
    TextView v_name;
}