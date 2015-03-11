package com.twobits.pocketleague;

import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.twobits.pocketleague.backend.Fragment_Base;
import com.twobits.pocketleague.backend.NavDrawerAdapter;
import com.twobits.pocketleague.backend.NavDrawerItem;
import com.twobits.pocketleague.backend.NavigationInterface;
import com.twobits.pocketleague.db.tables.Game;
import com.twobits.pocketleague.enums.SessionType;
import com.twobits.pocketleague.gameslibrary.GameType;

import java.util.ArrayList;
import java.util.List;

public class PocketLeague extends DataInterfaceActivity implements NavigationInterface {
    FragmentManager mFragmentManager;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private CharSequence mSubTitle;
    NavDrawerItem[] mDrawerItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
            mFragmentManager.beginTransaction()
                    .replace(R.id.content_frame, new List_Sessions()).commit();
            mDrawerList.setItemChecked(0, true);
            if (getCurrentGameType() == GameType.UNDEFINED) {
                viewGameTypes();
            }
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

    // --------------------------------
    // NAVIGATION INTERFACE METHODS
    // --------------------------------

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

    @Override
    public void onBackPressed() {
        boolean cab_closed = false;
        if (getFragmentManager().findFragmentById(R.id.content_frame) instanceof Fragment_Base) {
            cab_closed = ((Fragment_Base) getFragmentManager()
                    .findFragmentById(R.id.content_frame)).closeContextualActionBar();
        }

        if (cab_closed) {
            // Then don't do anything else.
        } else if (mDrawerLayout.isDrawerOpen(mDrawerList)) {
            mDrawerLayout.closeDrawer(mDrawerList);
        } else if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    public void refreshFragment() {
        Fragment_Base f = (Fragment_Base) getFragmentManager().findFragmentById(R.id.content_frame);
        f.refreshDetails();
    }

    public void loadGame(String gId) {
        Game g = null;

//        try {
//            g = gDao.queryForId(gId);
//            sDao.refresh(g.getSession());
//            vDao.refresh(g.getVenue());
//        } catch (SQLException e) {
//            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
//        }

        if (g != null){
            String action_type = g.getSession().getGameSubtype().toDescriptor().actionString();
            Intent intent = new Intent(action_type);
            if (intent.resolveActivity(getPackageManager()) != null) {
                intent.putExtra("GID", gId);
                startActivity(intent);
            } else {
                // DialogFragment.show() will take care of adding the fragment
                // in a transaction.  We also want to remove any currently showing
                // dialog, so make our own transaction and take care of that here.
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                Fragment prev = getFragmentManager().findFragmentByTag("dialog");
                if (prev != null) {
                    ft.remove(prev);
                }
                ft.addToBackStack(null);

                DialogFragment newFragment = Quick_Game.newInstance(getDatabase(), gId);
                newFragment.show(ft, "dialog");
            }
        }
    }

    public void viewSessions() {
        selectItem(0, "Sessions");
    }

    public void viewGameTypes() {
        selectItem(2, "Games");
    }

    public void viewGameSubtypes(String gametype) {
        // DialogFragment.show() will take care of adding the fragment
        // in a transaction.  We also want to remove any currently showing
        // dialog, so make our own transaction and take care of that here.
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        DialogFragment newFragment = List_GameSubtypes.newInstance(gametype);
        newFragment.show(ft, "dialog");
    }

    public void viewSessionDetails(String sId, SessionType session_type) {
        Fragment fragment = null;
        try {
            fragment = (Fragment) session_type.toClass().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            Toast.makeText(this, "Unable to load fragment.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

        if (fragment != null) {
            Bundle args = new Bundle();
            args.putString("SID", sId);
            fragment.setArguments(args);

            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.replace(R.id.content_frame, fragment).addToBackStack(null);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.commit();
        }
    }

    public void viewPlayerDetails(String pId) {
        Fragment fragment = new Detail_Player();

        Bundle args = new Bundle();
        args.putString("PID", pId);
        fragment.setArguments(args);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.content_frame, fragment).addToBackStack(null);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }

    public void viewTeamDetails(String tId) {
        Fragment fragment = new Detail_Team();

        Bundle args = new Bundle();
        args.putString("TID", tId);
        fragment.setArguments(args);

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment)
                .addToBackStack(null).commit();
    }

    public void viewVenueDetails(String vId) {
        Fragment fragment = new Detail_Venue();

        Bundle args = new Bundle();
        args.putString("VID", vId);
        fragment.setArguments(args);

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment)
                .addToBackStack(null).commit();
    }

    public void modifySession(String sId) {
        Fragment fragment = new Modify_Session();

        Bundle args = new Bundle();
        if (sId != null) {
            args.putString("SID", sId);
        }
        fragment.setArguments(args);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.content_frame, fragment).addToBackStack(null);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }

    public void modifyPlayer(String pId) {
        Fragment fragment = new Modify_Player();

        Bundle args = new Bundle();
        if (pId != null) {
            args.putString("PID", pId);
        }
        fragment.setArguments(args);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.content_frame, fragment).addToBackStack(null);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }

    public void modifyTeam(String tId) {
        Fragment fragment = new Modify_Team();

        Bundle args = new Bundle();
        if (tId != null) {
            args.putString("TID", tId);
        }
        fragment.setArguments(args);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.content_frame, fragment).addToBackStack(null);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }

    public void modifyVenue(String vId) {
        Fragment fragment = new Modify_Venue();

        Bundle args = new Bundle();
        if (vId != null) {
            args.putString("VID", vId);
        }
        fragment.setArguments(args);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.content_frame, fragment).addToBackStack(null);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }
}