package com.twobits.pocketleague;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.twobits.pocketleague.backend.DataInterface;
import com.twobits.pocketleague.backend.Fragment_Base;
import com.twobits.pocketleague.backend.NavDrawerAdapter;
import com.twobits.pocketleague.backend.NavDrawerItem;
import com.twobits.pocketleague.backend.NavigationInterface;
import com.twobits.pocketleague.db.DatabaseHelper;
import com.twobits.pocketleague.db.tables.Game;
import com.twobits.pocketleague.db.tables.GameMember;
import com.twobits.pocketleague.db.tables.Player;
import com.twobits.pocketleague.db.tables.Session;
import com.twobits.pocketleague.db.tables.SessionMember;
import com.twobits.pocketleague.db.tables.Team;
import com.twobits.pocketleague.db.tables.TeamMember;
import com.twobits.pocketleague.db.tables.Venue;
import com.twobits.pocketleague.enums.SessionType;
import com.twobits.pocketleague.gameslibrary.GameType;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PocketLeague extends ActionBarActivity implements NavigationInterface, DataInterface {
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private CharSequence mSubTitle;
    NavDrawerItem[] mDrawerItems;
    FragmentManager mFragmentManager;

    public static final String APP_PREFS = "PocketLeaguePreferences";

    private SharedPreferences settings;
    private SharedPreferences.Editor prefs_editor;
    private DatabaseHelper databaseHelper = null;
    Dao<Game, Long> gDao;
    Dao<GameMember, Long> gmDao;
    Dao<Player, Long> pDao;
    Dao<Session, Long> sDao;
    Dao<SessionMember, Long> smDao;
    Dao<Team, Long> tDao;
    Dao<TeamMember, Long> tmDao;
    Dao<Venue, Long> vDao;


    public Dao<Game, Long> getGameDao(){
        return gDao;
    }

    public Dao<GameMember, Long> getGameMemberDao(){
         return gmDao;
    }

    public Dao<Player, Long> getPlayerDao(){
        return pDao;
    }

    public Dao<Session, Long> getSessionDao(){
        return sDao;
    }

    public Dao<SessionMember, Long> getSessionMemberDao(){
        return smDao;
    }

    public Dao<Team, Long> getTeamDao(){
        return tDao;
    }

    public Dao<TeamMember, Long> getTeamMemberDao(){
        return tmDao;
    }

    public Dao<Venue, Long> getVenueDao(){
        return vDao;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            gDao = getHelper().getGameDao();
            gmDao = getHelper().getGameMemberDao();
            pDao = getHelper().getPlayerDao();
            sDao = getHelper().getSessionDao();
            smDao = getHelper().getSessionMemberDao();
            tDao = getHelper().getTeamDao();
            tmDao = getHelper().getTeamMemberDao();
            vDao = getHelper().getVenueDao();
        } catch (SQLException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }


        settings = this.getSharedPreferences(APP_PREFS, 0);
        prefs_editor = settings.edit();
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        setContentView(R.layout.activity_pocket_league);

        mFragmentManager = getFragmentManager();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

        mTitle = mDrawerTitle = getTitle();
        mDrawerItems = makeDrawerItemArray();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener
        mDrawerList.setAdapter(new NavDrawerAdapter(this, R.layout.nav_drawer_item, mDrawerItems));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        // enable ActionBar app icon to behave as action to toggle nav drawer
        //		getActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(this, /* host Activity */
                mDrawerLayout, /* DrawerLayout object */
                toolbar,
                R.string.drawer_open, /* "open drawer" description for accessibility */
                R.string.drawer_close /* "close drawer" description for accessibility */) {
            public void onDrawerClosed(View view) {
                getSupportActionBar().setTitle(mTitle);
                getSupportActionBar().setSubtitle(mSubTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                ((Fragment_Base) getFragmentManager()
                        .findFragmentById(R.id.content_frame)).closeContextualActionBar();
                getSupportActionBar().setTitle(mDrawerTitle);
                getSupportActionBar().setSubtitle("");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            if (getCurrentGameType() == GameType.UNDEFINED) {
                mFragmentManager.beginTransaction()
                        .replace(R.id.content_frame, new List_GameTypes()).commit();
                mDrawerList.setItemChecked(2, true);
            } else {
                mFragmentManager.beginTransaction()
                        .replace(R.id.content_frame, new List_Sessions()).commit();
                mDrawerList.setItemChecked(0, true);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (((Fragment_Base) getFragmentManager().findFragmentById(R.id.content_frame))
                .closeContextualActionBar()) {
        } else if (mDrawerLayout.isDrawerOpen(mDrawerList)) {
            mDrawerLayout.closeDrawer(mDrawerList);
        } else if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    private NavDrawerItem[] makeDrawerItemArray() {
        List<NavDrawerItem> items = new ArrayList<>();
        String[] mLabels = getResources().getStringArray(R.array.menuItems);
        TypedArray mIcons = getResources().obtainTypedArray(R.array.menuIcons);

        for (int ii = 0; ii < mLabels.length; ii += 1) {
            items.add(new NavDrawerItem(mLabels[ii], mIcons.getResourceId(ii, -1)));
        }
        items.add(1, new NavDrawerItem());
        items.add(6, new NavDrawerItem());
        mIcons.recycle();
//        items.get(0).counter = 3;
        return items.toArray(new NavDrawerItem[items.size()]);
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // hide action items when drawer is open
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        // menu.findItem(R.id.games).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        // ActionBarDrawerToggle will take care of this.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action buttons
        switch (item.getItemId()) {
            // case R.id.action_websearch:
            // // create intent to perform web search for this planet
            // Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
            // intent.putExtra(SearchManager.QUERY, getActionBar().getTitle());
            // // catch event that there's no activity to handle intent
            // if (intent.resolveActivity(getPackageManager()) != null) {
            // startActivity(intent);
            // } else {
            // Toast.makeText(this, R.string.app_not_available,
            // Toast.LENGTH_LONG).show();
            // }
            // return true;
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /* The click listener for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String label = (String) view.getTag();
            selectItem(position, label);
        }
    }

    private void selectItem(int position, String label) {
        Fragment fragment = null;

        switch (label) {
            case "About": // about
                fragment = new AboutPage();
                break;
            case "Database": // database
                fragment = new DbSettings();
                break;
            case "Games": // games
                fragment = new List_GameTypes();
                break;
            case "Players": // players
                fragment = new List_Players();
                break;
            case "Preferences": // preferences
                fragment = new Preferences();
                break;
            case "Sessions": // sessions
                fragment = new List_Sessions();
                break;
            case "Teams": // teams
                fragment = new List_Teams();
                break;
            case "Venues": // venues
                fragment = new List_Venues();
                break;
        }
        // update the main content by replacing fragments
        // Fragment fragment = new ContentFragment();
        // Bundle args = new Bundle();
        // args.putInt(ContentFragment.ARG_PLANET_NUMBER, position);
        // fragment.setArguments(args);

        if (fragment != null) {
            if (mFragmentManager.getBackStackEntryCount() > 0) {
                String f_name = mFragmentManager.getBackStackEntryAt(0).getName();
                mFragmentManager.popBackStack(f_name, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
            if (!label.equals("Sessions")) {
                FragmentTransaction ft = mFragmentManager.beginTransaction();
                ft.replace(R.id.content_frame, fragment);
                ft.addToBackStack(label);
                ft.commit();
            }

            mDrawerLayout.closeDrawer(mDrawerList);
        }
    }

    @Override
    public void setTitle(String title) {
        setTitle(title, "");
    }

    @Override
    public void setTitle(String title, String subtitle) {
        if (title != null) {
            mTitle = title;
            getSupportActionBar().setTitle(mTitle);
        }
        if (subtitle != null) {
            mSubTitle = subtitle;
            getSupportActionBar().setSubtitle(mSubTitle);
        }
    }

    public void setDrawerItemChecked(int position) {
        mDrawerList.setItemChecked(position, true);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    public String getPreference(String pref_name, String pref_default) {
        return settings.getString(pref_name, pref_default);
    }

    public void setPreference(String pref_name, String pref_value) {
        prefs_editor.putString(pref_name, pref_value);
        prefs_editor.commit();
    }

    public GameType getCurrentGameType() {
        return GameType.valueOf(getPreference("currentGameType",
                GameType.UNDEFINED.name()));
    }

    public void setCurrentGameType(GameType gametype) {
        setPreference("currentGameType", gametype.name());
    }

    public void loadGame(Long gId) {
        Fragment fragment = new Quick_Game();

        Bundle args = new Bundle();
        if (gId != null) {
            args.putLong("GID", gId);
        }
        fragment.setArguments(args);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.content_frame, fragment).addToBackStack(null);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }

    public void viewSessions() {
        selectItem(0, "Sessions");
    }

    public void viewSessionDetails(Long sId, SessionType session_type) {

        Fragment fragment = null;
        try {
            fragment = (Fragment) session_type.toClass().newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        Bundle args = new Bundle();
        args.putLong("SID", sId);
        fragment.setArguments(args);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.content_frame, fragment).addToBackStack(null);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }

    public void modifySession(Long sId) {
        Fragment fragment = new Modify_Session();

        Bundle args = new Bundle();
        if (sId != null) {
            args.putLong("SID", sId);
        }
        fragment.setArguments(args);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.content_frame, fragment).addToBackStack(null);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }

    public void viewPlayerDetails(Long pId) {
        Fragment fragment = new Detail_Player();

        Bundle args = new Bundle();
        args.putLong("PID", pId);
        fragment.setArguments(args);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.content_frame, fragment).addToBackStack(null);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }

    public void modifyPlayer(Long pId) {
        Fragment fragment = new Modify_Player();

        Bundle args = new Bundle();
        if (pId != null) {
            args.putLong("PID", pId);
        }
        fragment.setArguments(args);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.content_frame, fragment).addToBackStack(null);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }

    public void viewTeamDetails(Long tId) {
        Fragment fragment = new Detail_Team();

        Bundle args = new Bundle();
        args.putLong("TID", tId);
        fragment.setArguments(args);

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment)
                .addToBackStack(null).commit();
    }

    public void modifyTeam(Long tId) {
        Fragment fragment = new Modify_Team();

        Bundle args = new Bundle();
        if (tId != null) {
            args.putLong("TID", tId);
        }
        fragment.setArguments(args);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.content_frame, fragment).addToBackStack(null);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }

    public void viewVenueDetails(Long vId) {
        Fragment fragment = new Detail_Venue();

        Bundle args = new Bundle();
        args.putLong("VID", vId);
        fragment.setArguments(args);

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment)
                .addToBackStack(null).commit();
    }

    public void modifyVenue(Long vId) {
        Fragment fragment = new Modify_Venue();

        Bundle args = new Bundle();
        if (vId != null) {
            args.putLong("VID", vId);
        }
        fragment.setArguments(args);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.content_frame, fragment).addToBackStack(null);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }
}