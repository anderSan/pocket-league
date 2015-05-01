package info.andersonpa.gametemplate.db;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@DatabaseTable
public class Game {
    public static final String ID = "id";
    public static final String POCKETLEAGUE_ID = "pocketleague_id";
    public static final String RULESET_ID = "ruleset_id";
    public static final String DATE_PLAYED = "date_played";
    public static final String IS_COMPLETE = "is_complete";

    @DatabaseField(generatedId = true)
    private long id;

    @DatabaseField(unique = true)
    private long pocketleague_id;

    @DatabaseField(canBeNull = false)
    public int ruleset_id;

    @DatabaseField(canBeNull = false)
    private Date date_played;

    @DatabaseField
    private boolean is_complete = false;

    public Game() {
    }

    public Game(int ruleset_id, Date date_played) {
        this.ruleset_id = ruleset_id;
        this.date_played = date_played;
    }

    public Game(int ruleset_id) {
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
        List<Game> games = new ArrayList<>();
        for (Game g : d) {
            games.add(g);
        }
        return games;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getDatePlayed() {
        return date_played;
    }

    public void setDatePlayed(Date date_played) {
        this.date_played = date_played;
    }

    public boolean getIsComplete() {
        return is_complete;
    }

    public void setIsComplete(boolean isComplete) {
        this.is_complete = isComplete;
    }
}
