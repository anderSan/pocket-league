package com.twobits.pocketleague.backend;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.util.Log;
import android.view.View;

import com.couchbase.lite.Database;

public abstract class DialogFragment_Base extends DialogFragment {
    protected String LOGTAG = getClass().getSimpleName();
    public View rootView;
    public Context context;
    public NavigationInterface mNav;
    public DataInterface mData;

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

    public Database database() {
        return mData.getDatabase();
    }

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
