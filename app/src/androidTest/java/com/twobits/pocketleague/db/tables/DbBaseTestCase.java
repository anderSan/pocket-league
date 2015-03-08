package com.twobits.pocketleague.db.tables;

import android.test.AndroidTestCase;

import com.couchbase.lite.Database;
import com.twobits.pocketleague.SandboxContext;
import com.twobits.pocketleague.db.DatabaseHelper;

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
