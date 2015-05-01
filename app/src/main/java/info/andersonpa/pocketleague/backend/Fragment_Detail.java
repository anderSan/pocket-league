package info.andersonpa.pocketleague.backend;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ToggleButton;

import info.andersonpa.pocketleague.R;

public abstract class Fragment_Detail extends Fragment_Base {
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
}
