package info.andersonpa.pocketleague.backend;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

import info.andersonpa.pocketleague.R;

public class ListAdapter_Team extends RecyclerView.Adapter<ListAdapter_Team.ViewHolder> {
    private Context context;
    private List<Item_Team> team_list;
    private View.OnClickListener itemClicked;
    private View.OnClickListener cbClicked;

    public ListAdapter_Team(Context context, List<Item_Team> data, View.OnClickListener itemClicked,
                            View.OnClickListener cbClicked) {
        this.context = context;
        this.team_list = data;
        this.itemClicked = itemClicked;
        this.cbClicked = cbClicked;
    }

    @Override
    public int getItemCount() {
        return team_list.size();
    }

    @Override
    public ListAdapter_Team.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View team_view = inflater.inflate(R.layout.list_item_team, parent, false);
        ViewHolder viewHolder = new ViewHolder(team_view);
        team_view.setOnClickListener(itemClicked);
        viewHolder.t_isFavorite.setOnClickListener(cbClicked);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ListAdapter_Team.ViewHolder viewHolder, int position) {
        Item_Team t = team_list.get(position);

        viewHolder.itemView.setTag(t.getId());
        viewHolder.t_color.setBackgroundColor(t.getColor());
        viewHolder.t_name.setText(t.getName());
        viewHolder.t_isFavorite.setChecked(t.getIsFavorite());
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView t_color;
        TextView t_name;
        CheckBox t_isFavorite;

        ViewHolder(View itemView) {
            super(itemView);

            t_color = (TextView) itemView.findViewById(R.id.tv_t_color);
            t_name = (TextView) itemView.findViewById(R.id.tv_t_name);
            t_isFavorite = (CheckBox) itemView.findViewById(R.id.cb_t_isfavorite);
        }
    }
}