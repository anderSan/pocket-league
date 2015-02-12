package com.twobits.pocketleague;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.twobits.pocketleague.backend.Detail_Session_Base;

public class Detail_Session_Ladder extends Detail_Session_Base {

    @Override
    public void createSessionLayout(LayoutInflater inflater, ViewGroup container) {
        rootView = inflater.inflate(R.layout.fragment_detail_session_ladder, container, false);
    }

    @Override
    public void refreshDetails() {

    }
}
