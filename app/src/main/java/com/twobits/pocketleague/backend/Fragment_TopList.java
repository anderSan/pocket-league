package com.twobits.pocketleague.backend;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ToggleButton;

import com.twobits.pocketleague.R;

public abstract class Fragment_TopList extends Fragment_Base {
    public ImageButton bar_add;
    public ToggleButton bar_isFavorite;
    public ToggleButton bar_isActive;

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
        refreshDetails();
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
