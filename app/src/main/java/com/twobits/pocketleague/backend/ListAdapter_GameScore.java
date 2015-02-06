package com.twobits.pocketleague.backend;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.twobits.pocketleague.R;
import com.twobits.pocketleague.gameslibrary.ScoreType;

import java.util.ArrayList;
import java.util.List;

public class ListAdapter_GameScore extends ArrayAdapter<Item_GameScore> {
    public static final String LOGTAG = "ListAdapter_GameScore";
    private Context context;
    private List<Item_GameScore> gamescore_list = new ArrayList<>();
    private ScoreType score_type;

    public ListAdapter_GameScore(Context context, int layoutResourceId, List<Item_GameScore> data, ScoreType score_type) {

        super(context, layoutResourceId, data);
        this.context = context;
        this.gamescore_list = data;
        this.score_type = score_type;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder_GameScore holder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context
                    .LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item_gamescore, parent, false);

            holder = new ViewHolder_GameScore();
            holder.member_name = (TextView) convertView.findViewById(R.id.tv_memberName);
            holder.member_score = (EditText) convertView.findViewById(R.id.memberScore);
            holder.member_score.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        int new_score = Integer.parseInt(((EditText) v).getText().toString());
                        gamescore_list.get(position).setMemberScore(new_score);
                    }
                }
            });

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder_GameScore) convertView.getTag();
        }

        holder.member_name.setText(gamescore_list.get(position).getMemberName());
        holder.member_score.setText(Integer.toString(gamescore_list.get(position).getMemberScore()));

        return convertView;
    }

    @Override
    public int getCount() {
        return gamescore_list.size();
    }
}

class ViewHolder_GameScore {
    int position;
    TextView member_name;
    EditText member_score;
}