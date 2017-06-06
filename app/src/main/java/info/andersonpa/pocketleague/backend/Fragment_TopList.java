package info.andersonpa.pocketleague.backend;

import android.animation.Animator;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import info.andersonpa.pocketleague.R;

public abstract class Fragment_TopList extends Fragment_Base {
    FloatingActionButton fab, fab1, fab2, fab3;
    LinearLayout fabLayout1, fabLayout2, fabLayout3;
    TextView fabText1, fabText2, fabText3;
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
            show_favorites = !show_favorites;
            closeFABMenu();
            refreshDetails();
        }
    };

    View.OnClickListener activeClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            show_actives = !show_actives;
            closeFABMenu();
            refreshDetails();
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        refreshDetails();
    }

    private void showFABMenu(){
        isFABOpen=true;
        fabLayout1.setVisibility(View.VISIBLE);
        fabLayout2.setVisibility(View.VISIBLE);
        fabLayout3.setVisibility(View.VISIBLE);
        fabBGLayout.setVisibility(View.VISIBLE);
        fabText1.setVisibility(View.VISIBLE);
        fabText2.setVisibility(View.VISIBLE);
        fabText3.setVisibility(View.VISIBLE);

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
                if(!isFABOpen) {
                    fabText1.setVisibility(View.GONE);
                    fabText2.setVisibility(View.GONE);
                    fabText3.setVisibility(View.GONE);
                }
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

    public void setupFabButtons(String title1, String title2, String title3) {
        fabLayout1 = (LinearLayout) rootView.findViewById(R.id.fabLayout1);
        fabLayout2 = (LinearLayout) rootView.findViewById(R.id.fabLayout2);
        fabLayout3 = (LinearLayout) rootView.findViewById(R.id.fabLayout3);
        fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab1 = (FloatingActionButton) rootView.findViewById(R.id.fab1);
        fab2 = (FloatingActionButton) rootView.findViewById(R.id.fab2);
        fab3 = (FloatingActionButton) rootView.findViewById(R.id.fab3);
        fabText1 = (TextView) rootView.findViewById(R.id.fabTitle1);
        fabText2 = (TextView) rootView.findViewById(R.id.fabTitle2);
        fabText3 = (TextView) rootView.findViewById(R.id.fabTitle3);
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

        changeFabTitle(1, title1);
        changeFabTitle(2, title2);
        changeFabTitle(3, title3);
        fab1.setOnClickListener(addClicked);
        fab2.setOnClickListener(activeClicked);
        fab3.setOnClickListener(favoriteClicked);
    }

    public void changeFabTitle(int which, String title) {
        switch (which) {
            case 1: fabText1.setText(title);
                break;
            case 2: fabText2.setText(title);
                break;
            case 3: fabText3.setText(title);
                break;
        }
    }

    public void changeFabIcon(int which, int draw_id) {
        switch (which) {
            case 1: fab1.setImageDrawable(ContextCompat.getDrawable(getActivity(), draw_id));
                break;
            case 2: fab2.setImageDrawable(ContextCompat.getDrawable(getActivity(), draw_id));
                break;
            case 3: fab3.setImageDrawable(ContextCompat.getDrawable(getActivity(), draw_id));
                break;
        }
    }
}
