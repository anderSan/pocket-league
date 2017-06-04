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

public class ListAdapter_Player extends RecyclerView.Adapter<ListAdapter_Player.ViewHolder> {
	private Context context;
	private List<Item_Player> player_list;
    private View.OnClickListener itemClicked;
    private View.OnClickListener cbClicked;

	public ListAdapter_Player(Context context, List<Item_Player> data, View.OnClickListener itemClicked,
                              View.OnClickListener cbClicked) {
		this.context = context;
		this.player_list = data;
        this.itemClicked = itemClicked;
        this.cbClicked = cbClicked;
	}

    @Override
    public int getItemCount() {
        return player_list.size();
    }

    @Override
    public ListAdapter_Player.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View player_view = inflater.inflate(R.layout.list_item_player, parent, false);
        ViewHolder viewHolder = new ViewHolder(player_view);
        player_view.setOnClickListener(itemClicked);
        viewHolder.p_isFavorite.setOnClickListener(cbClicked);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ListAdapter_Player.ViewHolder viewHolder, int position) {
        Item_Player p = player_list.get(position);

        viewHolder.itemView.setTag(p.getId());
        viewHolder.p_color.setBackgroundColor(p.getColor());
        viewHolder.p_name.setText(p.getName());
        viewHolder.p_nickname.setText(p.getNickname());
        viewHolder.p_isFavorite.setChecked(p.getIsFavorite());
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView p_color;
        TextView p_name;
        TextView p_nickname;
        CheckBox p_isFavorite;

        ViewHolder(View itemView) {
            super(itemView);

            p_color = (TextView) itemView.findViewById(R.id.tv_p_color);
            p_name = (TextView) itemView.findViewById(R.id.tv_p_name);
            p_nickname = (TextView) itemView.findViewById(R.id.tv_p_nickname);
            p_isFavorite = (CheckBox) itemView.findViewById(R.id.cb_p_isfavorite);
        }
    }
}

