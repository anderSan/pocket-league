package com.twobits.polishhorseshoes.backend;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.twobits.polishhorseshoes.db.Game;
import com.twobits.polishhorseshoes.db.Throw;
import com.twobits.polishhorseshoes.enums.RuleType;
import com.twobits.polishhorseshoes.rulesets.RuleSet;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;

public class ActiveGame {
    private String session_name;
    private String[] team_names = new String[2];
    private String venue_name;

    private Context context;
    private int activeIdx;

    private Game g;

    private ArrayList<Throw> tArray;

    public RuleSet ruleSet;

    private Dao<Game, Long> gDao;
    private Dao<Throw, Long> tDao;

    public ActiveGame(long gId, long p1Id, long p2Id, Context context, int testRuleSetId) {
        gDao = Game.getDao(context);
        tDao = Throw.getDao(context);

        if (gId != -1) {
            try {
                g = gDao.queryBuilder().where().eq(Game.POCKETLEAGUE_ID, gId).queryForFirst();
                if (g == null) {
                    g = new Game(p1Id, p2Id,0);
                }

                tArray = g.getThrowList(context);
            } catch (SQLException e) {
                throw new RuntimeException("couldn't get throws for game " + g.getId() + ": ", e);
            }

            session_name = "The Session";
            venue_name = "The Venue";
            team_names[0] = "Team1 Name";
            team_names[1] = "Team2 Name";
            ruleSet = RuleType.map.get(g.ruleset_id);

        }

        activeIdx = 0;
        if (tArray.size() > 0) {
            activeIdx = tArray.size() - 1;
        }
        updateScoresFrom(0);
    }

    private Throw makeNextThrow() {
        Throw t = g.makeNewThrow(nThrows());
        return t;
    }

    private void setInitialScores(Throw t, Throw previousThrow) {
        int[] scores = ruleSet.getFinalScores(previousThrow);
        t.initialDefensivePlayerScore = scores[0];
        t.initialOffensivePlayerScore = scores[1];
    }

    private void setInitialScores(Throw t) {
        t.initialDefensivePlayerScore = 0;
        t.initialOffensivePlayerScore = 0;
    }

    public void updateScoresFrom(int idx) {
        Throw t, u;
        for (int i = idx; i < nThrows(); i++) {
            t = getThrow(i);
            if (i == 0) {
                setInitialScores(t);
                t.offenseFireCount = 0;
                t.defenseFireCount = 0;
            } else {
                u = getPreviousThrow(t);
                setInitialScores(t, u);
                ruleSet.setFireCounts(t, u);
            }
        }
        updateGameScore();
    }

    private void updateGameScore() {
        int[] scores = {0, 0};
        if (nThrows() > 0) {
            Throw lastThrow = getThrow(nThrows() - 1);
            if (Throw.isP1Throw(lastThrow)) {
                scores = ruleSet.getFinalScores(lastThrow);
            } else {
                int[] tmp = ruleSet.getFinalScores(lastThrow);
                scores[1] = tmp[0];
                scores[0] = tmp[1];
            }
        }
        g.setTeam1Score(scores[0]);
        g.setTeam2Score(scores[1]);
    }

    private ArrayList<Long> getThrowIds() {
        HashMap<String, Object> m;
        List<Throw> tList = new ArrayList<Throw>();
        ArrayList<Long> throwIds = new ArrayList<Long>();
        int cnt = 0;
        try {
            for (Throw t : tArray) {
                m = t.getQueryMap();
                tList = tDao.queryForFieldValuesArgs(m);
                if (tList.isEmpty()) {
                    throwIds.add(Long.valueOf(-1));
                } else {
                    throwIds.add(tList.get(0).getId());
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("could not query for throw ids");
        }
        return throwIds;
    }

    public Throw getActiveThrow() {
        return getThrow(activeIdx);
    }

    public void updateActiveThrow(Throw t) {
        setThrow(activeIdx, t);
    }

    public void setThrow(int idx, Throw t) {
        if (idx < 0) {
            throw new RuntimeException("must have positive throw index, not: " + idx);
        } else if (idx >= 0 && idx < nThrows()) {
            t.throwIdx = idx;
            assert g.isValidThrow(t) : "invalid throw for index " + idx;
            t = tArray.set(idx, t);
            saveThrow(t);
        } else if (idx == nThrows()) {
            t.throwIdx = idx;
            assert g.isValidThrow(t) : "invalid throw for index " + idx;
            tArray.add(t);
            saveThrow(t);
        } else if (idx > nThrows()) {
            throw new RuntimeException("cannot set throw " + idx + " in the far future");
        }
        updateScoresFrom(idx);
    }

    public Throw getThrow(int idx) {
        Throw t = null;
        if (idx < 0) {
            throw new RuntimeException("must have positive throw index, not: " + idx);
        } else if (idx >= 0 && idx < nThrows()) {
            t = tArray.get(idx);
        } else if (idx == nThrows()) {
            t = makeNextThrow();
            if (idx == 0) {
                setInitialScores(t);
            } else {
                Throw u = getPreviousThrow(t);
                setInitialScores(t, u);
            }

            tArray.add(t);
        } else if (idx > nThrows()) {
            throw new RuntimeException("cannot get throw " + idx + " from the far future");
        }
        if (t == null) {
            throw new NullPointerException("Got invalid throw for index " + idx);
        }
        return t;
    }

    public Throw getPreviousThrow(Throw t) {
        Throw u = null;
        int idx = t.throwIdx;
        if (idx <= 0) {
            throw new RuntimeException("throw " + idx + " has no predecessor");
        } else if (idx > 0 && idx <= nThrows()) {
            u = tArray.get(idx - 1);
        } else if (idx > nThrows()) {
            throw new RuntimeException("cannot get predecessor of throw " + idx + " from the far " +
                    "future");
        }
        if (u == null) {
            throw new NullPointerException("Got invalid predecessor for throw index " + idx);
        }
        return u;
    }

    public int nThrows() {
        return tArray.size();
    }

    public ArrayList<Throw> getThrows() {
        return tArray;
    }

    public int getActiveIdx() {
        return activeIdx;
    }

    public void setActiveIdx(int activeIdx) {
        this.activeIdx = activeIdx;
    }

    public Game getGame() {
        return g;
    }

    public void setGame(Game g) {
        this.g = g;
    }

    public long getGameId() {
        return g.getId();
    }

    public Date getGameDate() {
        return g.getDatePlayed();
    }

    public String getP1Name() {
        return team_names[0];
    }

    public String getP2Name() {
        return team_names[1];
    }

    public String getSessionName() {
        return session_name;
    }

    public String getVenueName() {
        return venue_name;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    /* Saving functions */
    private void saveThrow(Throw t) {
        if (g != null) {
            HashMap<String, Object> m = t.getQueryMap();
            List<Throw> tList = new ArrayList<Throw>();
            try {
                tList = tDao.queryForFieldValuesArgs(m);
            } catch (SQLException e) {
                throw new RuntimeException("could not query for throw " + t.throwIdx + ", " +
                        "game " + t.getGame().getId());
            }
            try {
                if (tList.isEmpty()) {
                    assert g.isValidThrow(t) : "invalid throw for index " + t.throwIdx + ", not saving";
                    tDao.create(t);
                } else {
                    assert g.isValidThrow(t) : "invalid throw for index " + t.throwIdx + ", not updating";
                    t.setId(tList.get(0).getId());
                    tDao.update(t);
                }
            } catch (SQLException e) {
                throw new RuntimeException("could not create/update throw " + t.throwIdx + ", game " + t.getGame().getId());
            }
        }
    }

    public void saveAllThrows() {
        updateScoresFrom(0);
        if (g != null) {
            final ArrayList<Long> throwIds = getThrowIds();
            try {
                tDao.callBatchTasks(new Callable<Void>() {
                    public Void call() throws SQLException {
                        long id;
                        Throw t;
                        for (int i = 0; i < tArray.size(); i++) {
                            id = throwIds.get(i);
                            t = tArray.get(i);
                            if (id == -1) {
                                tDao.create(t);
                            } else {
                                t.setId(id);
                                tDao.update(t);
                            }
                        }
                        return null;
                    }
                });
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

    }

    public void saveGame() {
        if (g != null) {
            try {
                gDao.update(g);
            } catch (SQLException e) {
                throw new RuntimeException("Could not save game " + g.getId());
            }
        }
    }
}
