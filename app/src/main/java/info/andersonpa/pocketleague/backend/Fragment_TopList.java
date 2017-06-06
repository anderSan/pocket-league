package info.andersonpa.pocketleague.backend;

import android.animation.Animator;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ToggleButton;

import info.andersonpa.pocketleague.R;

public abstract class Fragment_TopList extends Fragment_Base {
    public ImageButton bar_add;
    public ToggleButton bar_isFavorite;
    public ToggleButton bar_isActive;

    FloatingActionButton fab, fab1, fab2, fab3;
    LinearLayout fabLayout1, fabLayout2, fabLayout3;
    View fabBGLayout;
    boolean isFABOpen=false;

    public boolean show_favorites = false;
    public boolean show_actives = true;

    public View.OnClickListener addClicked = null;

    public void setAddClicked(View.OnClickListener addClicked) {
        this.addClicked = addClicked;
    }

    View.OnClickListener favoriteClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            show_favorites = ((ToggleButton) v).isChecked();
            refreshDetails();
        }
    };

    View.OnClickListener activeClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            show_actives = ((ToggleButton) v).isChecked();
            refreshDetails();
        }
    };

    @Override
    public void onResume() {
        super.onResume();

        fabLayout1 = (LinearLayout) rootView.findViewById(R.id.fabLayout1);
        fabLayout2 = (LinearLayout) rootView.findViewById(R.id.fabLayout2);
        fabLayout3 = (LinearLayout) rootView.findViewById(R.id.fabLayout3);
        fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab1 = (FloatingActionButton) rootView.findViewById(R.id.fab1);
        fab2 = (FloatingActionButton) rootView.findViewById(R.id.fab2);
        fab3 = (FloatingActionButton) rootView.findViewById(R.id.fab3);
        fabBGLayout = rootView.findViewById(R.id.fabBGLayout);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isFABOpen){
                    showFABMenu();
                }else{
                    closeFABMenu();
                }
            }
        });

        fabBGLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeFABMenu();
            }
        });

        refreshDetails();
    }

    private void showFABMenu(){
        isFABOpen=true;
        fabLayout1.setVisibility(View.VISIBLE);
        fabLayout2.setVisibility(View.VISIBLE);
        fabLayout3.setVisibility(View.VISIBLE);
        fabBGLayout.setVisibility(View.VISIBLE);

        fab.animate().rotationBy(180);
        fabLayout1.animate().translationY(-getResources().getDimension(R.dimen.standard_55));
        fabLayout2.animate().translationY(-getResources().getDimension(R.dimen.standard_100));
        fabLayout3.animate().translationY(-getResources().getDimension(R.dimen.standard_145));
    }

    public boolean closeFABMenu(){
        if (!isFABOpen) {
            return false;
        }
        isFABOpen=false;
        fabBGLayout.setVisibility(View.GONE);
        fab.animate().rotationBy(-180);
        fabLayout1.animate().translationY(0);
        fabLayout2.animate().translationY(0);
        fabLayout3.animate().translationY(0).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                if(!isFABOpen){
                    fabLayout1.setVisibility(View.GONE);
                    fabLayout2.setVisibility(View.GONE);
                    fabLayout3.setVisibility(View.GONE);
                }

            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        return true;
    }

    public void setupBarButtons() {
        setupBarButtons(null, null);
    }

    public void setupBarButtons(String active_on, String active_off) {
        bar_add = (ImageButton) rootView.findViewById(R.id.bar_add);
        bar_add.setOnClickListener(addClicked);
        bar_isFavorite = (ToggleButton) rootView.findViewById(R.id.bar_favorite);
        bar_isFavorite.setOnClickListener(favoriteClicked);
        bar_isFavorite.setChecked(show_favorites);
        bar_isActive = (ToggleButton) rootView.findViewById(R.id.bar_active);
        if (active_on != null) {
            bar_isActive.setTextOn(active_on);
        }
        if (active_off != null) {
            bar_isActive.setTextOff(active_off);
        }
        bar_isActive.setOnClickListener(activeClicked);
        bar_isActive.setChecked(show_actives);
    }
}
