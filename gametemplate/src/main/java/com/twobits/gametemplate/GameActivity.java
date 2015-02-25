package com.twobits.gametemplate;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.twobits.gametemplate.db.DatabaseHelper;
import com.twobits.gametemplate.db.Game;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GameActivity extends ActionBarActivity {
    private DatabaseHelper databaseHelper = null;

    Long gId;
    List<Long> gm_ids = new ArrayList<>();
    List<String> team_names = new ArrayList<>();
    String session_name;
    String venue_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Intent intent = getIntent();
        gId = intent.getLongExtra("GID", -1);

        fetchGameDetails();

        try {
            Dao<Game, Long> gDao = getHelper().getGameDao();
        } catch (SQLException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void fetchGameDetails() {
        Uri pl_uri;
        Cursor cursor;

        pl_uri = new Uri.Builder().scheme(ContentResolver.SCHEME_CONTENT)
                .authority("com.twobits.pocketleague.provider").appendPath("game")
                .appendPath(String.valueOf(gId)).build();
        cursor = getContentResolver().query(pl_uri, null, null, null, null);
        cursor.moveToFirst();

        session_name = cursor.getString(cursor.getColumnIndex("session_name"));
        TextView tv_session = (TextView) this.findViewById(R.id.session_name);
        tv_session.setText(session_name);

        venue_name = cursor.getString(cursor.getColumnIndex("venue_name"));
        TextView tv_venue = (TextView) this.findViewById(R.id.venue_name);
        tv_venue.setText(venue_name);

        cursor.close();


        pl_uri = new Uri.Builder().scheme(ContentResolver.SCHEME_CONTENT)
                .authority("com.twobits.pocketleague.provider").appendPath("game_member")
                .appendPath(String.valueOf(gId)).build();
        cursor = getContentResolver().query(pl_uri, null, null, null, null);
        while (cursor.moveToNext()) {
            gm_ids.add(cursor.getLong(cursor.getColumnIndex("id")));
            team_names.add(cursor.getString(cursor.getColumnIndex("team_name")));
        }

        cursor.close();

        ListView lv = (ListView) this.findViewById(R.id.game_members);
        ArrayAdapter<String> lv_adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, team_names);
        lv.setAdapter(lv_adapter);
        final Context context = this;
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int score;
                ContentValues values;
                Uri uri;

                for (int ii = 0; ii < gm_ids.size(); ii++) {
                    if (position == ii) {
                        score = 1;
                    } else {
                        score = 0;
                    }
                    uri = new Uri.Builder().scheme(ContentResolver.SCHEME_CONTENT)
                            .authority("com.twobits.pocketleague.provider")
                            .appendPath("game_member").appendPath(gm_ids.get(ii).toString())
                            .build();
                    values = new ContentValues();
                    values.put("score", score);
                    getContentResolver().update(uri, values, null, null);
                }

                uri = new Uri.Builder().scheme(ContentResolver.SCHEME_CONTENT)
                        .authority("com.twobits.pocketleague.provider").appendPath("game")
                        .appendPath(gId.toString()).build();
                values = new ContentValues();
                values.put("is_complete", true);
                getContentResolver().update(uri, values, null, null);
                finish();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_game, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public DatabaseHelper getHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);
        }
        return databaseHelper;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (databaseHelper != null) {
            OpenHelperManager.releaseHelper();
            databaseHelper = null;
        }
    }
}
