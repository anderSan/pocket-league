package com.twobits.pocketleague.backend;

import com.twobits.pocketleague.enums.SessionType;

public interface NavigationInterface {
    public void setTitle(String title);

    public void setDrawerItemChecked(int position);

	public void loadGame(long gId);

	public void viewSessions();

    public void viewSessionDetails(Long sId, SessionType session_type);

    public void viewPlayerDetails(Long pId);

    public void viewTeamDetails(Long tId);

    public void viewVenueDetails(Long vId);
}