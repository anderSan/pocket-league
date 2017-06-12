package info.andersonpa.polishhorseshoes.backend;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import info.andersonpa.polishhorseshoes.R;
import info.andersonpa.polishhorseshoes.rulesets.RuleSet;
import info.andersonpa.polishhorseshoes.rulesets.RuleSet01;

public class Adapter_Inning extends RecyclerView.Adapter<Adapter_Inning.ViewHolder>{
    private Context context;
    private List<Item_Inning> inning_list;
    private View.OnClickListener itemClicked;

    public Adapter_Inning(Context context, List<Item_Inning> data, View.OnClickListener itemClicked) {
        this.context = context;
        this.inning_list = data;
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
        Item_Inning inning = inning_list.get(position);
        RuleSet rs = new RuleSet01();

        viewHolder.inning.setText(String.valueOf(inning.getInning()));

        viewHolder.pl_hp.setText(String.valueOf(inning.getPL_throw().initialDefensivePlayerHitPoints));
        viewHolder.pl_pts.setText(String.valueOf(inning.getPL_throw().initialDefensivePlayerScore));
        rs.setThrowDrawable(inning.getPL_throw(), viewHolder.pl_throw);
        if (inning.pr_throw != null) {
            viewHolder.pr_hp.setText(String.valueOf(inning.getPR_throw().initialOffensivePlayerHitPoints));
            viewHolder.pr_pts.setText(String.valueOf(inning.getPR_throw().initialOffensivePlayerScore));
            rs.setThrowDrawable(inning.getPR_throw(), viewHolder.pr_throw);

        }

    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView inning;
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
