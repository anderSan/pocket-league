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

public class ListAdapter_Team extends ArrayAdapter<Item_Team> {
    private Context context;
    private List<Item_Team> team_list;
    private View.OnClickListener cbClicked;

    public ListAdapter_Team(Context context, int layoutResourceId, List<Item_Team> data,
                            View.OnClickListener cbClicked) {
        super(context, layoutResourceId, data);
        this.context = context;
        this.team_list = data;
        this.cbClicked = cbClicked;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder_Team holder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context
                    .LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item_team, parent, false);

            holder = new ViewHolder_Team();
            holder.t_name = (TextView) convertView.findViewById(R.id.tv_t_name);
            holder.t_isFavorite = (CheckBox) convertView.findViewById(R.id.cb_t_isfavorite);
            holder.t_isFavorite.setOnClickListener(cbClicked);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder_Team) convertView.getTag();
        }

        Item_Team item = team_list.get(position);
        holder.t_name.setText(item.getName());
        holder.t_isFavorite.setChecked(item.getIsFavorite());
        holder.t_isFavorite.setTag(item.getId());

        return convertView;
    }

    @Override
    public int getCount() {
        return team_list.size();
    }
}

class ViewHolder_Team {
    TextView t_name;
    CheckBox t_isFavorite;
}