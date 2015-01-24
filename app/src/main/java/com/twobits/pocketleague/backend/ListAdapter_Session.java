package com.twobits.pocketleague.backend;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.twobits.pocketleague.R;

import java.util.List;

public class ListAdapter_Session extends ArrayAdapter<Item_Session> {
    private static final String LOGTAG = "ListAdapter_Session";
	private Context context;
	private List<Item_Session> session_list;
    private View.OnClickListener cbClicked;

	public ListAdapter_Session(Context context, int layoutResourceId, List<Item_Session> data,
                               View.OnClickListener cbClicked) {
        super(context, layoutResourceId, data);
		this.context = context;
		this.session_list = data;
        this.cbClicked = cbClicked;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder_Session holder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item_session, null);

            holder = new ViewHolder_Session();
            holder.s_name = (TextView) convertView.findViewById(R.id.tv_s_name);
            holder.s_type = (TextView) convertView.findViewById(R.id.tv_s_type);
            holder.s_isfavorite = (CheckBox) convertView.findViewById(R.id.cb_s_isfavorite);
            holder.s_isfavorite.setOnClickListener(cbClicked);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder_Session) convertView.getTag();
        }

        Item_Session item = session_list.get(position);
        holder.s_name.setText(item.getName());
        holder.s_type.setText(item.getSessionType().toString());
        holder.s_isfavorite.setChecked(item.getIsFavorite());
        holder.s_isfavorite.setTag(item.getId());

		return convertView;
	}

    @Override
    public int getCount() {
        return session_list.size();
    }

    @Override
    public Item_Session getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }
}

class ViewHolder_Session {
    long sId;
    TextView s_name;
    TextView s_type;
    CheckBox s_isfavorite;
}