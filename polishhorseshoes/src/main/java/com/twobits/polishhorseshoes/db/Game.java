package com.twobits.polishhorseshoes.db;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import com.twobits.polishhorseshoes.enums.ThrowResult;
import com.twobits.polishhorseshoes.enums.ThrowType;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@DatabaseTable
public class Game {
    public static final String POCKETLEAGUE_ID = "pocketleague_id";
    public static final String FIRST_TEAM = "team_1_id";
    public static final String SECOND_TEAM = "team_2_id";
    public static final String RULESET_ID = "ruleset_id";
    public static final String TEAM_1_ON_TOP = "team_1_on_top";
    public static final String DATE_PLAYED = "date_played";
    public static final String TEAM_1_SCORE = "team_1_score";
    public static final String TEAM_2_SCORE = "team_1_score";
    public static final String IS_COMPLETE = "is_complete";

    @DatabaseField(generatedId = true)
    private long id;

    @DatabaseField(unique = true)
    private long pocketleague_id;

    @DatabaseField(canBeNull = false)
    private long team_1_id;

    @DatabaseField(canBeNull = false)
    private long team_2_id;

    @DatabaseField(canBeNull = false)
    public int ruleset_id;

    @DatabaseField(canBeNull = false)
    public boolean team_1_on_top;

    @DatabaseField(canBeNull = false)
    private Date date_played;

    @DatabaseField
    private int team_1_score;

    @DatabaseField
    private int team_2_score;

    @DatabaseField
    private boolean is_complete = false;

    @ForeignCollectionField
    ForeignCollection<Throw> throw_list;

    public Game() {
    }

    public Game(long team_1_id, long team_2_id, int ruleset_id, Date date_played) {
        this.team_1_id = team_1_id;
        this.team_2_id = team_2_id;
        this.ruleset_id = ruleset_id;
        this.date_played = date_played;

    }

    public Game(long team_1_id, long team_2_id, int ruleset_id) {
        this.team_1_id = team_1_id;
        this.team_2_id = team_2_id;
        this.ruleset_id = ruleset_id;
        this.date_played = new Date();
    }

    public static Dao<Game, Long> getDao(Context context) {
        DatabaseHelper helper = new DatabaseHelper(context);
        Dao<Game, Long> d = null;
        try {
            d = helper.getGameDao();
        } catch (SQLException e) {
            throw new RuntimeException("Couldn't get game dao: ", e);
        }
        return d;
    }

    public static List<Game> getAll(Context context) throws SQLException {
        Dao<Game, Long> d = Game.getDao(context);
        List<Game> games = new ArrayList<Game>();
        for (Game g : d) {
            games.add(g);
        }
        return games;
    }

    public boolean isValidThrow(Throw t) {
        boolean isValid = true;
        int idx = t.throwIdx;
        switch (idx % 2) {
            // TODO: do players need to be refreshed now that foreign variable is
            // used?
            // first player is on offense
            case 0:
                isValid = isValid && (t.getOffensiveTeamId() == team_1_id);
                break;
            // second player is on defense
            case 1:
                isValid = isValid && (t.getDefensiveTeamId() == team_2_id);
                break;
            default:
                throw new RuntimeException("invalid index " + idx);
        }
        return isValid;
    }

    public ArrayList<Throw> getThrowList(Context context) throws SQLException {
        int tidx, maxThrowIndex;
        ArrayList<Throw> throwArray = new ArrayList<Throw>();

        HashMap<Integer, Throw> throwMap = new HashMap<Integer, Throw>();
        HashMap<String, Object> m = new HashMap<String, Object>();
        m.put("game_id", getId());

        Dao<Throw, Long> d = Throw.getDao(context);
        List<Throw> dbThrows = d.queryForFieldValuesArgs(m);

        maxThrowIndex = 0;
        if (!dbThrows.isEmpty()) {
            Collections.sort(dbThrows);

            for (Throw t : dbThrows) {
                tidx = t.throwIdx;

                // purge any throws with negative index
                if (tidx < 0) {
                    d.delete(t);
                }

                // populate the map
                throwMap.put(tidx, t);

                // keep track of the maximum index
                if (tidx > maxThrowIndex) {
                    maxThrowIndex = tidx;
                }
            }

            // ensure throws in correct order and complete
            Throw t = null;
            for (int i = 0; i <= maxThrowIndex; i++) {
                t = throwMap.get(i);
                // infill with a caught strike if necessary
                if (t == null) {
                    t = makeNewThrow(i);
                    t.throwType = ThrowType.STRIKE;
                    t.throwResult = ThrowResult.CATCH;
                }
                throwArray.add(t);
            }
        }

        return throwArray;
    }

    public Throw makeNewThrow(int throwNumber) {
        long offensiveTeam_id, defensiveTeam_id;
        if (throwNumber % 2 == 0) {
            offensiveTeam_id = getTeam1Id();
            defensiveTeam_id = getTeam2Id();
        } else {
            offensiveTeam_id = getTeam2Id();
            defensiveTeam_id = getTeam1Id();
        }
        Date timestamp = new Date(System.currentTimeMillis());
        Throw t = new Throw(throwNumber, this, offensiveTeam_id, defensiveTeam_id, timestamp);

        return t;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTeam1Id() {
        return team_1_id;
    }

    public void setTeam1Id(long team_1_id) {
        this.team_1_id = team_1_id;
    }

    public long getTeam2Id() {
        return team_2_id;
    }

    public void setTeam2Id(long team_2_id) {
        this.team_2_id = team_2_id;
    }

    public Date getDatePlayed() {
        return date_played;
    }

    public void setDatePlayed(Date date_played) {
        this.date_played = date_played;
    }

    public int getTeam1Score() {
        return team_1_score;
    }

    public void setTeam1Score(int team_1_score) {
        this.team_1_score = team_1_score;
        checkGameComplete();
    }

    public int getTeam2Score() {
        return team_2_score;
    }

    public void setTeam2Score(int team_2_score) {
        this.team_2_score = team_2_score;
        checkGameComplete();
    }

    public boolean getIsComplete() {
        return is_complete;
    }

    public void setIsComplete(boolean isComplete) {
        this.is_complete = isComplete;
    }

    public void checkGameComplete() {
        Integer s1 = getTeam1Score();
        Integer s2 = getTeam2Score();
        if (Math.abs(s1 - s2) >= 2 && (s1 >= 11 || s2 >= 11)) {
            setIsComplete(true);
        } else {
            setIsComplete(false);
        }
    }

    public ForeignCollection<Throw> getThrows() {
        return throw_list;
    }

    public long getWinner() {
        // TODO: should raise an error if game is not complete
        long winner = team_1_id;
        if (getTeam2Score() > getTeam1Score()) {
            winner = team_2_id;
        }
        return winner;
    }

    public long getLoser() {
        long loser = team_1_id;
        if (getTeam2Score() < getTeam1Score()) {
            loser = team_2_id;
        }
        return loser;
    }
}
