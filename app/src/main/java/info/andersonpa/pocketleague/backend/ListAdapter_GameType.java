package info.andersonpa.pocketleague.backend;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import info.andersonpa.pocketleague.R;

public class ListAdapter_GameType extends RecyclerView.Adapter<ListAdapter_GameType.ViewHolder> {
    private Context context;
    private List<Item_GameType> gametype_list;
    private View.OnClickListener itemClicked;

    public ListAdapter_GameType(Context context, List<Item_GameType> data, View.OnClickListener itemClicked) {
        this.context = context;
        this.gametype_list = data;
        this.itemClicked = itemClicked;
    }

    @Override
    public int getItemCount() {
        return gametype_list.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View gt_view = inflater.inflate(R.layout.grid_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(gt_view);
        gt_view.setOnClickListener(itemClicked);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ListAdapter_GameType.ViewHolder viewHolder, int position) {
        Item_GameType gt = gametype_list.get(position);

        viewHolder.itemView.setTag(gt.getGameType());
        viewHolder.gt_name.setText(gt.getName());
        viewHolder.gt_drawable.setImageDrawable(ContextCompat.getDrawable(context, gt.getDrawableId()));
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView gt_name;
        ImageView gt_drawable;

        ViewHolder(View itemView) {
            super(itemView);
            gt_name = (TextView) itemView.findViewById(R.id.grid_text);
            gt_drawable = (ImageView) itemView.findViewById(R.id.grid_image);
        }
    }
}