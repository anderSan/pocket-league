package com.twobits.pocketleague.backend;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.twobits.pocketleague.R;

import java.util.List;

public class ListAdapter_Player extends ArrayAdapter<Item_Player> {
	private static final String LOGTAG = "ListAdapter_Player";
	private Context context;
	private List<Item_Player> player_list;

	public ListAdapter_Player(Context context, int layoutResourceId, List<Item_Player> data) {
        super(context, layoutResourceId, data);
		this.context = context;
		this.player_list = data;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder_Player holder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item_player, null);

            holder = new ViewHolder_Player();
            holder.p_id = (TextView) convertView.findViewById(R.id.tv_p_id);
            holder.p_name = (TextView) convertView.findViewById(R.id.tv_p_name);
            holder.p_nickname = (TextView) convertView.findViewById(R.id.tv_p_nickname);
            holder.p_color = (TextView) convertView.findViewById(R.id.tv_p_color);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder_Player) convertView.getTag();
        }

        holder.p_id.setText(player_list.get(position).getId());
        holder.p_name.setText(player_list.get(position).getName());
        holder.p_nickname.setText(player_list.get(position).getNickname());
        holder.p_color.setBackgroundColor(player_list.get(position).getColor());

		return convertView;
	}

    @Override
    public int getCount() {
        return player_list.size();
    }

    @Override
    public Item_Player getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }
}

class ViewHolder_Player {
    int position;
    TextView p_id;
    TextView p_name;
    TextView p_nickname;
    TextView p_color;
}