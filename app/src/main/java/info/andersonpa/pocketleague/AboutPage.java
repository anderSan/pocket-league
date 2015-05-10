package info.andersonpa.pocketleague;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import info.andersonpa.pocketleague.backend.Fragment_Base;

public class AboutPage extends Fragment_Base {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_about_page, container,
				false);

		TextView tv_version = (TextView) rootView.findViewById(R.id.version_number);
        TextView tv_flaticon = (TextView) rootView.findViewById(R.id.tv_flaticon_acknowledgment);
        tv_flaticon.setMovementMethod(LinkMovementMethod.getInstance());

        String version_name;
        try {
            version_name = getActivity().getPackageManager()
                    .getPackageInfo(getActivity().getPackageName(), 0).versionName;
            tv_version.setText("v" + version_name);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

		return rootView;
	}
}
