package com.twobits.pocketleague.backend;

import android.app.Activity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ToggleButton;

import com.twobits.pocketleague.R;
import com.twobits.pocketleague.db.OrmLiteFragment;

public class Fragment_TopList extends OrmLiteFragment {
    public static final String LOGTAG = "Fragment_TopList";
    public MenuItem mi_add;
    public CheckBox mi_isFavorite;
    public ToggleButton mi_isActive;

    public boolean show_favorites = true;
    public boolean show_actives = true;

    MenuItem.OnMenuItemClickListener addClicked = null;

    View.OnClickListener favoriteClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            show_favorites = ((CheckBox) v).isChecked();
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
        mi_add = menu.add(R.string.menu_add);
        mi_add.setOnMenuItemClickListener(addClicked);
        mi_add.setTitle(R.string.menu_add);
        mi_add.setIcon(R.drawable.ic_menu_add);
        mi_add.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem
                .SHOW_AS_ACTION_WITH_TEXT);

        MenuItem favorite_item = menu.add(R.string.menu_favorite);
        favorite_item.setTitle(R.string.menu_favorite);
        favorite_item.setActionView(mi_isFavorite);
        favorite_item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem
                .SHOW_AS_ACTION_WITH_TEXT);

        MenuItem active_item = menu.add(R.string.menu_active);
        active_item.setTitle(R.string.menu_active);
        active_item.setActionView(mi_isActive);
        active_item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem
                .SHOW_AS_ACTION_WITH_TEXT);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mi_isFavorite = new CheckBox(context, null, android.R.attr.starStyle);
        mi_isFavorite.setChecked(show_favorites);
        mi_isFavorite.setOnClickListener(favoriteClicked);

        mi_isActive = new ToggleButton(context);
        mi_isActive.setTextOn(getString(R.string.active));
        mi_isActive.setTextOff(getString(R.string.retired));
        mi_isActive.setChecked(show_actives);
        mi_isActive.setOnClickListener(activeClicked);
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshListing();
    }

    public void setAddClicked(MenuItem.OnMenuItemClickListener addClicked) {
        this.addClicked = addClicked;
    }

    public void refreshListing() {

    }
}
