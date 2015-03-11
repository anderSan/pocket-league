package com.twobits.pocketleague.backend;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.twobits.pocketleague.R;

import java.util.ArrayList;
import java.util.List;

public class ListAdapter_GameType extends ArrayAdapter<Item_GameType> {
    private Context context;
    private int layoutResourceId;
    private List<Item_GameType> gametype_list = new ArrayList<>();

    public ListAdapter_GameType(Context context, int layoutResourceId, List<Item_GameType> data) {
        super(context, layoutResourceId, data);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.gametype_list = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View gridView;
        TextView textView;
        ImageView imageView;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context
                    .LAYOUT_INFLATER_SERVICE);
            gridView = inflater.inflate(R.layout.grid_item, parent, false);
            gridView.setTag(gametype_list.get(position).getGameType());
        } else {
            gridView = convertView;
        }

        textView = (TextView) gridView.findViewById(R.id.grid_text);
        imageView = (ImageView) gridView.findViewById(R.id.grid_image);

        textView.setText(gametype_list.get(position).getName());
        imageView.setImageResource(gametype_list.get(position).getDrawableId());

        return gridView;
    }

    @Override
    public int getCount() {
        return gametype_list.size();
    }
}