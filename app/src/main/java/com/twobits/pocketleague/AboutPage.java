package com.twobits.pocketleague;

import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.twobits.pocketleague.backend.Fragment_Base;

public class AboutPage extends Fragment_Base {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_about_page, container,
				false);

        TextView tv_flaticon = (TextView) rootView.findViewById(R.id.tv_flaticon_acknowledgment);
        tv_flaticon.setMovementMethod(LinkMovementMethod.getInstance());

		return rootView;
	}
}
