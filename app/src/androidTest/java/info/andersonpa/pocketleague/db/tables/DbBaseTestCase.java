package info.andersonpa.pocketleague.db.tables;

import android.test.AndroidTestCase;

import com.couchbase.lite.Database;

import info.andersonpa.pocketleague.SandboxContext;
import info.andersonpa.pocketleague.db.DatabaseHelper;

public class DbBaseTestCase extends AndroidTestCase {
    DatabaseHelper databaseHelper;
    Database database;

    protected void setUp() throws Exception {
        super.setUp();
        SandboxContext mock_context = new SandboxContext();
        databaseHelper = new DatabaseHelper(mock_context);
        database = databaseHelper.getDatabase();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        database.delete();
        database.close();
        database = null;
        databaseHelper.close();
        databaseHelper = null;
    }
}
