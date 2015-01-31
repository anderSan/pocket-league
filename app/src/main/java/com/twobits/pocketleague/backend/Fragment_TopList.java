package com.twobits.pocketleague.backend;

import android.app.Activity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ToggleButton;

import com.twobits.pocketleague.R;
import com.twobits.pocketleague.db.OrmLiteFragment;

public abstract class Fragment_TopList extends OrmLiteFragment {
    public ImageButton bar_add;
    public ToggleButton bar_isFavorite;
    public ToggleButton bar_isActive;

    public boolean show_favorites = true;
    public boolean show_actives = true;

    public View.OnClickListener addClicked = null;

    public void setAddClicked(View.OnClickListener addClicked) {
        this.addClicked = addClicked;
    }

    View.OnClickListener favoriteClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            show_favorites = ((ToggleButton) v).isChecked();
            refreshListing();
        }
    };

    View.OnClickListener activeClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            show_actives = ((ToggleButton) v).isChecked();
            refreshListing();
        }
    };

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        bar_add = menu.add(R.string.menu_add);
//        bar_add.setOnMenuItemClickListener(addClicked);
//        bar_add.setTitle(R.string.menu_add);
//        bar_add.setIcon(R.drawable.ic_menu_add);
//        bar_add.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
//
//        MenuItem favorite_item = menu.add(R.string.menu_favorite);
//        favorite_item.setTitle(R.string.menu_favorite);
//        favorite_item.setActionView(bar_isFavorite);
//        favorite_item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem
//                .SHOW_AS_ACTION_WITH_TEXT);
//
//        MenuItem active_item = menu.add(R.string.menu_active);
//        active_item.setTitle(R.string.menu_active);
//        active_item.setActionView(bar_isActive);
//        active_item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem
//                .SHOW_AS_ACTION_WITH_TEXT);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
//        bar_isFavorite = new CheckBox(context, null, android.R.attr.starStyle);
//        bar_isFavorite.setChecked(show_favorites);
//        bar_isFavorite.setOnClickListener(favoriteClicked);
//
//        bar_isActive = new ToggleButton(context);
//        bar_isActive.setTextOn(getString(R.string.active));
//        bar_isActive.setTextOff(getString(R.string.retired));
//        bar_isActive.setChecked(show_actives);
//        bar_isActive.setOnClickListener(activeClicked);
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshListing();
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

    public abstract void refreshListing();
}
