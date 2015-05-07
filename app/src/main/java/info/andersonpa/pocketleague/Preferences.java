package info.andersonpa.pocketleague;

import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceFragment;

import info.andersonpa.pocketleague.backend.DataInterface;
import info.andersonpa.pocketleague.backend.NavigationInterface;

public class Preferences extends PreferenceFragment {
    public NavigationInterface mNav;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mNav = (NavigationInterface) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement NavigationInterface");
        }
    }

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mNav.setTitle("Preferences");
		mNav.setDrawerItemChecked(7);

		// Load the preferences from an XML resource
		addPreferencesFromResource(R.xml.preferences);
	}
}
