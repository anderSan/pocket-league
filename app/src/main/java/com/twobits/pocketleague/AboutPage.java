package com.twobits.pocketleague;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.twobits.pocketleague.db.OrmLiteFragment;

import org.w3c.dom.Text;

public class AboutPage extends OrmLiteFragment {
	private View rootView;
	private Context context;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_about_page, container,
				false);

        TextView tv_flaticon = (TextView) rootView.findViewById(R.id.tv_flaticon_acknowledgment);
        tv_flaticon.setMovementMethod(LinkMovementMethod.getInstance());

		return rootView;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		context = getActivity();
	}

}
