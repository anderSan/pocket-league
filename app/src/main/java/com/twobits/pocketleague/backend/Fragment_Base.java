package com.twobits.pocketleague.backend;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.View;

public abstract class Fragment_Base extends Fragment {
    protected String LOGTAG = getClass().getSimpleName();
    public View rootView;
    public Context context;
    public NavigationInterface mNav;
    public DataInterface mData;
    public ActionMode mActionMode;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mNav = (NavigationInterface) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement NavigationInterface");
        }
        try {
            mData = (DataInterface) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement DataInterface");
        }

        context = getActivity();
    }

    public boolean closeContextualActionBar() {
        if (mActionMode != null) {
            mActionMode.finish();
            return true;
        }
        return false;
    }

    public void refreshDetails() {}

    public void log(String msg) {
        Log.i(LOGTAG, msg);
    }

    public void logd(String msg) {
        Log.d(LOGTAG, msg);
    }

    public void loge(String msg, Exception e) {
        Log.e(LOGTAG, msg + ": " + e.getMessage());
    }
}
