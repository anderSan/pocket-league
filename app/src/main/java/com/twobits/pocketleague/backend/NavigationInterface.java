package com.twobits.pocketleague.backend;

import com.twobits.pocketleague.enums.SessionType;

public interface NavigationInterface {
    public void setTitle(String title);

    public void setTitle(String title, String subtitle);

    public void setDrawerItemChecked(int position);

    public void onBackPressed();

    public void refreshFragment();

	public void loadGame(Long gId);

    public void viewGameTypes();

	public void viewSessions();

    public void viewSessionDetails(String sId, SessionType session_type);

    public void viewPlayerDetails(String pId);

    public void viewTeamDetails(String tId);

    public void viewVenueDetails(String vId);

    public void modifySession(Long sId);

    public void modifyPlayer(Long pId);

    public void modifyTeam(Long tId);

    public void modifyVenue(Long vId);
}