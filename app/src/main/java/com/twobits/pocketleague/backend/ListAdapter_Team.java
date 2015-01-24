package com.twobits.pocketleague.backend;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.twobits.pocketleague.R;

import java.util.List;

public class ListAdapter_Team extends ArrayAdapter<Item_Team> {
    private static final String LOGTAG = "ListAdapter_Team";
	private Context context;
	private List<Item_Team> team_list;

	public ListAdapter_Team(Context context, int layoutResourceId, List<Item_Team> data) {
		super(context, layoutResourceId, data);
        this.context = context;
		this.team_list = data;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder_Team holder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item_team, null);

            holder = new ViewHolder_Team();
            holder.t_id = (TextView) convertView.findViewById(R.id.tv_t_id);
            holder.t_name = (TextView) convertView.findViewById(R.id.tv_t_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder_Team) convertView.getTag();
        }

        holder.t_id.setText(team_list.get(position).getId());
        holder.t_name.setText(team_list.get(position).getName());

        return convertView;
	}

    @Override
    public int getCount() {
        return team_list.size();
    }

    @Override
    public Item_Team getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

	}

class ViewHolder_Team {
    int position;
    TextView t_id;
    TextView t_name;
}