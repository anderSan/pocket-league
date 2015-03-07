package com.twobits.pocketleague.db.tables;

import android.util.ArrayMap;

import com.couchbase.lite.Database;

import java.util.Map;

public class SessionMember {
	public static final String TEAM = "team";
    private static final String TEAM_ID = "team_id";
	public static final String TEAM_SEED = "team_seed";
	public static final String TEAM_RANK = "team_rank";

	private Team team;
    private String team_id;
	private int team_seed;
	private int team_rank;

    public SessionMember() {}

	public SessionMember(int team_seed, int team_rank) {
		this.team_seed = team_seed;
		this.team_rank = team_rank;
	}

    public SessionMember(String team_id, int team_seed, int team_rank) {
        this(team_seed, team_rank);
        this.team = team;
    }

	public SessionMember(Team team, int team_seed, int team_rank) {
        this(team_seed, team_rank);
        this.team = team;
	}

    public SessionMember(Team team, int team_seed) {
        this(team, team_seed, 0);
    }

	public Team getTeam() {
		return team;
	}

	public int getSeed() {
		return team_seed;
	}

	public int getRank() {
		return team_rank;
	}

	public void setRank(int team_rank) {
		this.team_rank = team_rank;
	}

    public Map<String, Object> toMap() {
        Map<String, Object> contents = new ArrayMap<>();
        contents.put(TEAM_ID, team.getId());
        contents.put(TEAM_SEED, team_seed);
        contents.put(TEAM_RANK, team_rank);
        return contents;
    }

	// =========================================================================
	// Additional methods
	// =========================================================================

	public int compareTo(SessionMember another) {
        if (team_rank < another.team_rank) {
            return -1;
        } else if (team_rank == another.team_rank) {
            return 0;
        } else {
            return 1;
        }
	}

	public boolean equals(Object o) {
        if (!(o instanceof SessionMember))
            return false;
        SessionMember another = (SessionMember) o;
            if (team == another.team) {
            return true;
        } else {
            return false;
        }
	}
}
