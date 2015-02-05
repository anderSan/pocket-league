package com.twobits.pocketleague.backend;

import com.twobits.pocketleague.enums.SessionType;

public interface NavigationInterface {
    public void setTitle(String title);

    public void setTitle(String title, String subtitle);

    public void setDrawerItemChecked(int position);

    public void onBackPressed();

	public void loadGame(long gId);

	public void viewSessions();

    public void viewSessionDetails(Long sId, SessionType session_type);

    public void viewPlayerDetails(Long pId);

    public void viewTeamDetails(Long tId);

    public void viewVenueDetails(Long vId);

    public void modifySession(Long sId);

    public void modifyPlayer(Long pId);

    public void modifyTeam(Long tId);

    public void modifyVenue(Long vId);
}