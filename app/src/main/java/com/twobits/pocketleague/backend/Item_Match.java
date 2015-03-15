package com.twobits.pocketleague.backend;

import com.twobits.pocketleague.db.tables.Team;

public class Item_Match {
    private int id_in_session = -1;
    private String game_id;
    private Team upper_team;
    private Team lower_team;
    public String title = "";
    public String subtitle = "";
    private boolean creatable = false;
    private boolean viewable = false;

    Item_Match(int id_in_session) {
        this.id_in_session = id_in_session;
    }

    Item_Match(int id_in_session, String game_id, Team upper_team, Team lower_team) {
        this(id_in_session);
        setGameId(game_id);
        setUpperTeam(upper_team);
        setLowerTeam(lower_team);
    }

    public int getIdInSession() {
        return id_in_session;
    }

    public String getGameId() {
        return game_id;
    }

    public void setGameId(String game_id) {
        this.game_id = game_id;
        if (game_id != "") {
            this.viewable = true;
        }
    }

    public Team getUpper_team() {
        return upper_team;
    }

    public void setUpperTeam(Team upper_team) {
        this.upper_team = upper_team;
        if (upper_team != null && lower_team != null && game_id != null) {
            this.creatable = true;
        }
    }

    public Team getLower_team() {
        return lower_team;
    }

    public void setLowerTeam(Team lower_team) {
        this.lower_team = lower_team;
        if (upper_team != null && lower_team != null && game_id != null) {
            this.creatable = true;
        }
    }

    public boolean getCreatable() {
        return creatable;
    }

    public boolean getViewable() {
        return viewable;
    }

}
