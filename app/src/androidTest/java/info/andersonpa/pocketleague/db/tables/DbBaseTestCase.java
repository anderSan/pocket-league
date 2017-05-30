package info.andersonpa.pocketleague.db.tables;

import android.support.test.InstrumentationRegistry;

import com.couchbase.lite.Database;

import org.junit.After;
import org.junit.Before;

import info.andersonpa.pocketleague.db.DatabaseHelper;

class DbBaseTestCase {
    private DatabaseHelper databaseHelper;
    Database database;

    @Before
    public void setUp() throws Exception {
        databaseHelper = new DatabaseHelper(InstrumentationRegistry.getTargetContext());
        database = databaseHelper.getDatabase();
    }

    @After
    public void tearDown() throws Exception {
        database.delete();
        database.close();
        database = null;
        databaseHelper.close();
        databaseHelper = null;
    }
}
