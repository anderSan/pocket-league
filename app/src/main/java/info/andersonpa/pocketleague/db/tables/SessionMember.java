package info.andersonpa.pocketleague.db.tables;

import android.util.ArrayMap;

import java.util.Comparator;
import java.util.Map;

public class SessionMember implements Comparable<SessionMember> {
    public static final String TEAM_ID = "team_id";
	public static final String TEAM_SEED = "team_seed";
	public static final String TEAM_RANK = "team_rank";

	private Team team = null;
	private int team_seed = 0;
	private int team_rank = 0;

    public SessionMember() {}

    public SessionMember(int team_seed) {
        this.team_seed = team_seed;
    }

    public SessionMember(Team team, int team_seed, int team_rank) {
        this.team = team;
        this.team_seed = team_seed;
        this.team_rank = team_rank;
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

    public void swapRank(SessionMember that_sm) {
        swapRank(this, that_sm);
    }

    public static void swapRank(SessionMember sm1, SessionMember sm2) {
        int sm1_rank = sm1.team_rank;
        sm1.team_rank = sm2.team_rank;
        sm2.team_rank = sm1_rank;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> contents = new ArrayMap<>();
        contents.put(TEAM_ID, team.getId());
        contents.put(TEAM_SEED, team_seed);
        contents.put(TEAM_RANK, team_rank);
        return contents;
    }

    public int compareTo(SessionMember another) {
        return ((Integer) team_rank).compareTo(another.team_rank);
    }

    public static final Comparator<SessionMember> SEED_ORDER =
        new Comparator<SessionMember>() {
            public int compare(SessionMember e1, SessionMember e2) {
                return ((Integer) e1.team_seed).compareTo(e2.team_seed);
            }
        };
}
