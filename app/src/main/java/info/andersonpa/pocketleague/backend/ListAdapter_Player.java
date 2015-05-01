package info.andersonpa.pocketleague.backend;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import info.andersonpa.pocketleague.R;

import java.util.List;

public class ListAdapter_Player extends ArrayAdapter<Item_Player> {
	private Context context;
	private List<Item_Player> player_list;
    private View.OnClickListener cbClicked;

	public ListAdapter_Player(Context context, int layoutResourceId, List<Item_Player> data,
                              View.OnClickListener cbClicked) {
        super(context, layoutResourceId, data);
		this.context = context;
		this.player_list = data;
        this.cbClicked = cbClicked;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder_Player holder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item_player, parent, false);

            holder = new ViewHolder_Player();
            holder.p_color = (TextView) convertView.findViewById(R.id.tv_p_color);
            holder.p_name = (TextView) convertView.findViewById(R.id.tv_p_name);
            holder.p_nickname = (TextView) convertView.findViewById(R.id.tv_p_nickname);
            holder.p_isfavorite = (CheckBox) convertView.findViewById(R.id.cb_p_isfavorite);
            holder.p_isfavorite.setOnClickListener(cbClicked);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder_Player) convertView.getTag();
        }

        Item_Player item = player_list.get(position);
        holder.p_color.setBackgroundColor(item.getColor());
        holder.p_name.setText(item.getName());
        holder.p_nickname.setText(item.getNickname());
        holder.p_isfavorite.setChecked(item.getIsFavorite());
        holder.p_isfavorite.setTag(item.getId());

		return convertView;
	}

    @Override
    public int getCount() {
        return player_list.size();
    }
}

class ViewHolder_Player {
    TextView p_color;
    TextView p_name;
    TextView p_nickname;
    CheckBox p_isfavorite;

}