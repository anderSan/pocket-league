package info.andersonpa.gametemplate.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import info.andersonpa.gametemplate.R;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "gametemplate.db";
    private static final int DATABASE_VERSION = 1;

    private Dao<Game, Long> gameDao;

    private List<Class> tableClasses = new ArrayList<>();

    private Context myContext;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION, R.raw.ormlite_config);
        tableClasses.add(Game.class);

        myContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqliteDatabase, ConnectionSource connectionSource) {
        Log.i("DatabaseHelper", "Attempting to create db");
        try {
            createAll(connectionSource);
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Unable to create database", e);
        }
    }

    @Override
    public void onUpgrade(final SQLiteDatabase sqliteDatabase,
                          final ConnectionSource connectionSource, int oldVer, final int newVer) {
        Log.w("DatabaseHelper", "Attempting to upgrade from version " + oldVer + " to" +
                " version " + newVer);

        switch (oldVer) {
            case 10:
            default:
                try {
                    dropAll(connectionSource);
                    createAll(connectionSource);
                } catch (SQLException e) {
                    Log.e(DatabaseHelper.class.getName(), "Unable to upgrade database from " +
                            "version " + oldVer + " to " + newVer, e);
                }
        }
    }

    public void createAll() {
        try {
            createAll(getConnectionSource());
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.toString(), e.getMessage());
            throw new RuntimeException("could not create tables", e);
        }
    }

    public void dropAll() {
        try {
            dropAll(getConnectionSource());
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.toString(), e.getMessage());
            throw new RuntimeException("could not drop tables", e);
        }
    }

    protected void createAll(ConnectionSource connectionSource) throws SQLException {
        for (Class c : tableClasses) {
            TableUtils.createTableIfNotExists(connectionSource, c);
        }
    }

    protected void dropAll(ConnectionSource connectionSource) throws SQLException {
        for (Class c : tableClasses) {
            TableUtils.dropTable(connectionSource, c, true);
        }
    }

    public Dao<Game, Long> getGameDao() throws SQLException {
        if (gameDao == null) {
            gameDao = getDao(Game.class);
        }
        return gameDao;
    }
}
