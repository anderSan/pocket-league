package info.andersonpa.polishhorseshoes.backend;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import info.andersonpa.polishhorseshoes.R;
import info.andersonpa.polishhorseshoes.db.Throw;
import info.andersonpa.polishhorseshoes.rulesets.RuleSet;

public class Adapter_Inning extends RecyclerView.Adapter<Adapter_Inning.ViewHolder>{
    private Context context;
    private List<Item_Inning> inning_list;
    private RuleSet rs;
    private Throw current_throw;
    private View.OnClickListener itemClicked;

    public Adapter_Inning(Context context, List<Item_Inning> data, RuleSet rs, View.OnClickListener itemClicked) {
        this.context = context;
        this.inning_list = data;
        this.rs = rs;
        this.itemClicked = itemClicked;
    }

    @Override
    public int getItemCount() {
        return inning_list.size();
    }

    @Override
    public Adapter_Inning.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View inning_view = inflater.inflate(R.layout.inning, parent, false);
        ViewHolder viewHolder = new ViewHolder(inning_view);
        inning_view.findViewById(R.id.iv_pleft_throw).setOnClickListener(itemClicked);
        inning_view.findViewById(R.id.iv_pright_throw).setOnClickListener(itemClicked);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(Adapter_Inning.ViewHolder viewHolder, int position) {
        Item_Inning this_inning = inning_list.get(position);

        viewHolder.inning.setText(String.valueOf(this_inning.getInning()));

        viewHolder.pl_hp.setText(String.valueOf(this_inning.getPL_throw().initialDefensivePlayerHitPoints));
        viewHolder.pl_pts.setText(String.valueOf(this_inning.getPL_throw().initialDefensivePlayerScore));
        viewHolder.pl_marks.setText(rs.getSpecialString(this_inning.getPL_throw()));
        rs.setThrowDrawable(this_inning.getPL_throw(), viewHolder.pl_throw);
        if (this_inning.pr_throw != null) {
            viewHolder.pr_hp.setText(String.valueOf(this_inning.getPR_throw().initialOffensivePlayerHitPoints));
            viewHolder.pr_pts.setText(String.valueOf(this_inning.getPR_throw().initialOffensivePlayerScore));
            rs.setThrowDrawable(this_inning.getPR_throw(), viewHolder.pr_throw);
            viewHolder.pr_marks.setText(rs.getSpecialString(this_inning.getPR_throw()));
        }
        if (this_inning.getPL_throw() == current_throw) {
            viewHolder.pl_hp.setBackgroundColor(Color.LTGRAY);
            viewHolder.pl_pts.setBackgroundColor(Color.LTGRAY);
            viewHolder.pl_marks.setBackgroundColor(Color.LTGRAY);
            viewHolder.pl_throw.setBackgroundColor(Color.LTGRAY);
        } else {
            viewHolder.pl_hp.setBackgroundColor(ContextCompat.getColor(context, R.color.sysTransparent));
            viewHolder.pl_pts.setBackgroundColor(ContextCompat.getColor(context, R.color.sysTransparent));
            viewHolder.pl_marks.setBackgroundColor(ContextCompat.getColor(context, R.color.sysTransparent));
            viewHolder.pl_throw.setBackgroundColor(ContextCompat.getColor(context, R.color.sysTransparent));
        }
        if (this_inning.getPR_throw() == current_throw) {
            viewHolder.pr_hp.setBackgroundColor(Color.LTGRAY);
            viewHolder.pr_pts.setBackgroundColor(Color.LTGRAY);
            viewHolder.pr_marks.setBackgroundColor(Color.LTGRAY);
            viewHolder.pr_throw.setBackgroundColor(Color.LTGRAY);
        } else {
            viewHolder.pr_hp.setBackgroundColor(ContextCompat.getColor(context, R.color.sysTransparent));
            viewHolder.pr_pts.setBackgroundColor(ContextCompat.getColor(context, R.color.sysTransparent));
            viewHolder.pr_marks.setBackgroundColor(ContextCompat.getColor(context, R.color.sysTransparent));
            viewHolder.pr_throw.setBackgroundColor(ContextCompat.getColor(context, R.color.sysTransparent));
        }
    }

    public void setCurrent_throw(Throw t) {
        current_throw = t;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView inning;
        TextView pl_pts;
        TextView pr_pts;
        TextView pl_hp;
        TextView pr_hp;
        ImageView pl_throw;
        ImageView pr_throw;
        TextView pl_marks;
        TextView pr_marks;

        ViewHolder(View itemView) {
            super(itemView);

            inning = (TextView) itemView.findViewById(R.id.tv_inning);
            pl_pts = (TextView) itemView.findViewById(R.id.tv_pleft_pts);
            pr_pts = (TextView) itemView.findViewById(R.id.tv_pright_pts);
            pl_hp = (TextView) itemView.findViewById(R.id.tv_pleft_hp);
            pr_hp = (TextView) itemView.findViewById(R.id.tv_pright_hp);
            pl_marks = (TextView) itemView.findViewById(R.id.tv_pleft_marks);
            pr_marks = (TextView) itemView.findViewById(R.id.tv_pright_marks);
            pl_throw = (ImageView) itemView.findViewById(R.id.iv_pleft_throw);
            pr_throw = (ImageView) itemView.findViewById(R.id.iv_pright_throw);
        }
    }
}
