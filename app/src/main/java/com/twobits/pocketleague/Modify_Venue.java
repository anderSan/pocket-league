package com.twobits.pocketleague;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.twobits.pocketleague.backend.Fragment_Edit;
import com.twobits.pocketleague.db.tables.Venue;

import java.sql.SQLException;

public class Modify_Venue extends Fragment_Edit {
	String vId;
	Venue v;

	Button btn_create;
	TextView tv_name;
	CheckBox cb_isFavorite;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_modify_venue, container, false);

        Bundle args = getArguments();
        vId = args.getString("VID");

		btn_create = (Button) rootView.findViewById(R.id.button_createVenue);
		tv_name = (TextView) rootView.findViewById(R.id.editText_venueName);
		cb_isFavorite = (CheckBox) rootView.findViewById(R.id.newVenue_isFavorite);

        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doneButtonPushed();
            }
        });

		if (vId != null) {
			loadVenueValues();
		}

        return rootView;
	}

	private void loadVenueValues() {
        v = Venue.getFromId(database, vId);
        btn_create.setText("Modify");
        tv_name.setText(v.getName());
        cb_isFavorite.setChecked(v.getIsFavorite());
	}

	public void doneButtonPushed() {
		String venue_name = tv_name.getText().toString().trim();
		if (venue_name.isEmpty()) {
			Toast.makeText(context, "Venue name is required.", Toast.LENGTH_LONG)
					.show();
		} else {
			Boolean is_favorite = cb_isFavorite.isChecked();

			if (vId != null) {
				modifyVenue(venue_name, is_favorite);
			} else {
				createVenue(venue_name, is_favorite);
			}
		}
	}

	private void createVenue(String venue_name, boolean is_favorite) {
		Venue newVenue = new Venue(venue_name, is_favorite);

        newVenue.update(database);
        Toast.makeText(context, "Venue created!", Toast.LENGTH_SHORT).show();
        mNav.onBackPressed();
	}

	private void modifyVenue(String venue_name, boolean is_favorite) {
		v.setName(venue_name);
		v.setIsFavorite(is_favorite);

        v.update(database);
        Toast.makeText(context, "Venue modified.", Toast.LENGTH_SHORT).show();
        mNav.onBackPressed();
	}
}
