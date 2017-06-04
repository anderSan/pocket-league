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

public class ListAdapter_Venue extends RecyclerView.Adapter<ListAdapter_Venue.ViewHolder> {
    private Context context;
    private List<Item_Venue> venue_list;
    private View.OnClickListener itemClicked;
    private View.OnClickListener cbClicked;

    public ListAdapter_Venue(Context context, List<Item_Venue> data, View.OnClickListener itemClicked, View.OnClickListener cbClicked) {
        this.context = context;
        this.venue_list = data;
        this.itemClicked = itemClicked;
        this.cbClicked = cbClicked;
    }

    @Override
    public int getItemCount() {
    return venue_list.size();
}

    @Override
    public ListAdapter_Venue.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View venue_view = inflater.inflate(R.layout.list_item_venue, parent, false);
        ViewHolder viewHolder = new ViewHolder(venue_view);
        venue_view.setOnClickListener(itemClicked);
        viewHolder.v_isFavorite.setOnClickListener(cbClicked);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ListAdapter_Venue.ViewHolder viewHolder, int position) {
        Item_Venue v = venue_list.get(position);

        viewHolder.itemView.setTag(v.getId());
        viewHolder.v_name.setText(v.getName());
        viewHolder.v_isFavorite.setChecked(v.getIsFavorite());
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView v_name;
        CheckBox v_isFavorite;

        ViewHolder(View itemView) {
            super(itemView);

            v_name = (TextView) itemView.findViewById(R.id.tv_v_name);
            v_isFavorite = (CheckBox) itemView.findViewById(R.id.cb_v_isfavorite);
        }
    }
}