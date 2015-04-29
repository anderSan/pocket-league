package com.twobits.pocketleague.backend;

import com.twobits.pocketleague.db.tables.Team;
import com.twobits.pocketleague.enums.SessionType;

import java.util.List;

public interface NavigationInterface {
    void setTitle(String title);

    void setTitle(String title, String subtitle);

    void setDrawerItemChecked(int position);

    void onBackPressed();

    void refreshFragment();

    void setTeams(List<Team> teams);

    void loadGame(String gId);

    void viewGameTypes();

    void viewGameSubtypes(String gametype);

    void viewSessions();

    void viewSessionDetails(String sId, SessionType session_type);

    void viewPlayerDetails(String pId);

    void viewTeamDetails(String tId);

    void viewVenueDetails(String vId);

    void modifySession(String sId);

    void modifyPlayer(String pId);

    void modifyTeam(String tId);

    void modifyVenue(String vId);

    void selectTeams();

    void selectReseedSession();
}