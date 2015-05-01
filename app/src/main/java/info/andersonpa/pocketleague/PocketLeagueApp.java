package info.andersonpa.pocketleague;

import android.app.Application;

import com.couchbase.lite.Database;
import info.andersonpa.pocketleague.db.DatabaseHelper;

public class PocketLeagueApp extends Application {
    private DatabaseHelper databaseHelper = null;

    public Database getDatabase() {
        if (databaseHelper == null) {
            databaseHelper = new DatabaseHelper(this);
        }
        return databaseHelper.getDatabase();
    }

    public void deleteDatabase() {
        databaseHelper.deleteDatabase();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        if (databaseHelper != null) {
            databaseHelper.close();
            databaseHelper = null;
        }
    }
}
