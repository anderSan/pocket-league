package com.twobits.pocketleague.db.tables;

import java.util.List;

public class CouchStubs {
}

class TeamStub extends Team {
    public static String ID = "stub_ID";

    public TeamStub(String team_name, List<Player> members) {
        super(team_name, members);
    }

    @Override
    public String getId() {
        return ID;
    }
}