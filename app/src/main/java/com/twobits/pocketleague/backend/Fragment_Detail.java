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

public class Fragment_Detail extends OrmLiteFragment {
    public static final String LOGTAG = "Fragment_Detail";
    public MenuItem mi_modify;
    public CheckBox mi_isFavorite;
    public ToggleButton mi_isActive;

    MenuItem.OnMenuItemClickListener modifyClicked;
    View.OnClickListener favoriteClicked;
    View.OnClickListener activeClicked;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        mi_modify = menu.add(R.string.menu_modify);
        mi_modify.setOnMenuItemClickListener(modifyClicked);
        mi_modify.setTitle(R.string.menu_modify);
        mi_modify.setIcon(R.drawable.ic_action_edit);
        mi_modify.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem
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
        mi_isFavorite.setOnClickListener(favoriteClicked);

        mi_isActive = new ToggleButton(context);
        mi_isActive.setTextOn(getString(R.string.active));
        mi_isActive.setTextOff(getString(R.string.retired));
        mi_isActive.setOnClickListener(activeClicked);
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshDetails();
    }

    public void setModifyClicked(MenuItem.OnMenuItemClickListener modifyClicked) {
        this.modifyClicked = modifyClicked;
    }

    public void setFavoriteClicked(View.OnClickListener favoriteClicked) {
        this.favoriteClicked = favoriteClicked;
    }

    public void setActiveClicked(View.OnClickListener activeClicked) {
        this.activeClicked = activeClicked;
    }

    public void refreshDetails() {

    }
}
