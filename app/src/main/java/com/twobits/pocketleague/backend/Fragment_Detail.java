package com.twobits.pocketleague.backend;

import android.app.Activity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ToggleButton;

import com.twobits.pocketleague.R;
import com.twobits.pocketleague.db.OrmLiteFragment;

public abstract class Fragment_Detail extends OrmLiteFragment {
    public ImageButton bar_modify;
    public ToggleButton bar_isFavorite;
    public ToggleButton bar_isActive;

    public View.OnClickListener modifyClicked;
    public View.OnClickListener favoriteClicked;
    public View.OnClickListener activeClicked;

    public void setModifyClicked(View.OnClickListener modifyClicked) {
        this.modifyClicked = modifyClicked;
    }

    public void setFavoriteClicked(View.OnClickListener favoriteClicked) {
        this.favoriteClicked = favoriteClicked;
    }

    public void setActiveClicked(View.OnClickListener activeClicked) {
        this.activeClicked = activeClicked;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        inflater.inflate(R.menu.detail_menu, menu);
//        mi_modify = menu.findItem(R.id.menu_modify);
//        mi_isFavorite = (CheckBox) menu.findItem(R.id.menu_favorite).getActionView();
//        mi_isActive = (ToggleButton) menu.findItem(R.id.menu_active).getActionView();
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
//        mi_isFavorite = new CheckBox(context, null, android.R.attr.starStyle);
//        mi_isFavorite.setOnClickListener(favoriteClicked);
//
//        mi_isActive = new ToggleButton(context);
//        mi_isActive.setTextOn(getString(R.string.active));
//        mi_isActive.setTextOff(getString(R.string.retired));
//        mi_isActive.setOnClickListener(activeClicked);
    }

    @Override
    public void onResume() {
        super.onResume();

        refreshDetails();
    }

    public void setupBarButtons() {
        bar_modify = (ImageButton) rootView.findViewById(R.id.bar_modify);
        bar_modify.setOnClickListener(modifyClicked);
        bar_isFavorite = (ToggleButton) rootView.findViewById(R.id.bar_favorite);
        bar_isFavorite.setOnClickListener(favoriteClicked);
        bar_isActive = (ToggleButton) rootView.findViewById(R.id.bar_active);
        bar_isActive.setOnClickListener(activeClicked);
    }

    public abstract void refreshDetails();
}
