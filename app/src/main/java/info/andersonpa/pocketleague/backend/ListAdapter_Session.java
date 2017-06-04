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

public class ListAdapter_Session extends RecyclerView.Adapter<ListAdapter_Session.ViewHolder> {
	private Context context;
	private List<Item_Session> session_list;
    private View.OnClickListener itemClicked;
    private View.OnClickListener cbClicked;

	public ListAdapter_Session(Context context, List<Item_Session> data, View.OnClickListener itemClicked, View.OnClickListener cbClicked) {
		this.context = context;
		this.session_list = data;
        this.itemClicked = itemClicked;
        this.cbClicked = cbClicked;
	}

	@Override
    public int getItemCount() {
        return session_list.size();
    }

    @Override
    public ListAdapter_Session.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View session_view = inflater.inflate(R.layout.list_item_session, parent, false);
        ViewHolder viewHolder = new ViewHolder(session_view);
        session_view.setOnClickListener(itemClicked);
        viewHolder.s_isFavorite.setOnClickListener(cbClicked);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ListAdapter_Session.ViewHolder viewHolder, int position) {
        Item_Session s = session_list.get(position);

        viewHolder.itemView.setTag(s.getId());
        viewHolder.s_name.setText(s.getName());
        viewHolder.s_type.setText(s.getSessionType().toString());
        viewHolder.s_isFavorite.setChecked(s.getIsFavorite());
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView s_name;
        TextView s_type;
        CheckBox s_isFavorite;

        ViewHolder(View itemView) {
            super(itemView);

            s_name = (TextView) itemView.findViewById(R.id.tv_s_name);
            s_type = (TextView) itemView.findViewById(R.id.tv_s_type);
            s_isFavorite = (CheckBox) itemView.findViewById(R.id.cb_s_isfavorite);
        }
    }
}