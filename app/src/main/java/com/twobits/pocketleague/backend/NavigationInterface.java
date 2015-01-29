package com.twobits.pocketleague.backend;

public interface NavigationInterface {
    public void setTitle(String title);

    public void setDrawerItemChecked(int position);

	public void loadGame(long gId);

	public void viewSessions();

    public void viewPlayerDetails(Long pId);

    public void viewTeamDetails(Long tId);

    public void viewVenueDetails(Long vId);
}