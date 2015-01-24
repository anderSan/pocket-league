package com.twobits.pocketleague.backend;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.twobits.pocketleague.R;

import java.util.ArrayList;
import java.util.List;

public class ListAdapter_Session extends ArrayAdapter<Item_Session> {
    private static final String LOGTAG = "ListAdapter_Session";
	private Context context;
	private List<Item_Session> session_list;

	public ListAdapter_Session(Context context, int layoutResourceId, List<Item_Session> data) {
        super(context, layoutResourceId, data);
		this.context = context;
		this.session_list = data;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder_Session holder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item_session, null);

            holder = new ViewHolder_Session();
            holder.s_id = (TextView) convertView.findViewById(R.id.tv_s_id);
            holder.s_name = (TextView) convertView.findViewById(R.id.tv_s_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder_Session) convertView.getTag();
        }

        holder.s_id.setText(session_list.get(position).getId());
        holder.s_name.setText(session_list.get(position).getName());

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
    int position;
    TextView s_id;
    TextView s_name;
}