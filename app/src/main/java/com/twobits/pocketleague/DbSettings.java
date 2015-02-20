package com.twobits.pocketleague;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dropbox.sync.android.DbxAccountManager;
import com.dropbox.sync.android.DbxFile;
import com.dropbox.sync.android.DbxFileInfo;
import com.dropbox.sync.android.DbxFileSystem;
import com.dropbox.sync.android.DbxPath;
import com.j256.ormlite.dao.Dao;
import com.twobits.pocketleague.backend.Fragment_Base;
import com.twobits.pocketleague.db.DatabaseHelper;
import com.twobits.pocketleague.db.DbxInfo;
import com.twobits.pocketleague.db.tables.Player;
import com.twobits.pocketleague.db.tables.Session;
import com.twobits.pocketleague.db.tables.Team;
import com.twobits.pocketleague.db.tables.TeamMember;
import com.twobits.pocketleague.db.tables.Venue;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DbSettings extends Fragment_Base {

	private DbxAccountManager mDbxAcctMgr;
	private static final String appKey = DbxInfo.appKey;
	private static final String appSecret = DbxInfo.appSecret;
	private Button mLinkButton;
	private Button dbxSaveButton;
	private Button dbxLoadButton;
	private TextView mTestOutput;

	private static final int REQUEST_LINK_TO_DBX = 0;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.activity_db_settings, container,
				false);

		mDbxAcctMgr = DbxAccountManager.getInstance(
				context.getApplicationContext(), appKey, appSecret);

		Button mClearButton = (Button) rootView.findViewById(R.id.db_clearDB);
		mClearButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				clearTables();
			}
		});

		Button mPopulateButton = (Button) rootView.findViewById(R.id.db_popDB);
		mPopulateButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				doPopulateTest();
			}
		});

		mLinkButton = (Button) rootView.findViewById(R.id.db_linkToDropbox);
		mLinkButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				linkToDropbox();
			}
		});

		dbxSaveButton = (Button) rootView.findViewById(R.id.db_save_dropbox);
		dbxSaveButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				saveDBdropbox();
			}
		});

		dbxLoadButton = (Button) rootView.findViewById(R.id.db_load_dropbox);
		mTestOutput = (TextView) rootView.findViewById(R.id.db_dbxFiles);

		dbxLoadButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
						view.getContext());
				alertDialogBuilder.setTitle("Overwrite local database?");
				alertDialogBuilder
						.setMessage(
								"The local database will be overwritten by the most recent file in dropbox.")
						.setPositiveButton("Overwrite",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										// if this button is clicked, close
										// current activity
										loadDBdropbox();
									}
								})
						.setNegativeButton("Cancel",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										// if this button is clicked, just close
										// the dialog box and do nothing
										dialog.cancel();
									}
								});

				AlertDialog alertDialog = alertDialogBuilder.create();
				alertDialog.show();
			}
		});

		// Button mUpdtBtn = (Button)
		// rootView.findViewById(R.id.db_updateScores);
		// mUpdtBtn.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// updateScores();
		// }
		// });

		return rootView;
	}

	@Override
	public void onResume() {
		super.onResume();
		if (mDbxAcctMgr.hasLinkedAccount()) {
			showLinkedView();
		} else {
			showUnlinkedView();
		}
	}

	private void showLinkedView() {
		mLinkButton.setVisibility(View.GONE);
		dbxSaveButton.setVisibility(View.VISIBLE);
		dbxLoadButton.setVisibility(View.VISIBLE);
	}

	private void showUnlinkedView() {
		mLinkButton.setVisibility(View.VISIBLE);
		dbxSaveButton.setVisibility(View.GONE);
		dbxLoadButton.setVisibility(View.GONE);
	}

	private void linkToDropbox() {
		mDbxAcctMgr.startLink((Activity) context, REQUEST_LINK_TO_DBX);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_LINK_TO_DBX) {
            if (resultCode == Activity.RESULT_OK) {
                // ... Start using Dropbox files.
                Toast.makeText(context, "Successfully connected to dropbox!",
                        Toast.LENGTH_SHORT).show();
                mLinkButton.setVisibility(View.GONE);
            } else {
                // ... Link failed or was cancelled by the user.
                Toast.makeText(context, "Link failed or was cancelled by the user.",
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
                    }

	public void clearTables() {
		DatabaseHelper h = mData.getHelper();
		h.dropAll();
		h.createAll();
	}

	public void doPopulateTest() {
		byte[] emptyImage = new byte[0];
		Player[] players = {
				new Player("mike c", "michael", "cannamela", true, false, true,
						false, 170, 70, emptyImage, getResources().getColor(
								R.color.SaddleBrown), true),
				new Player("samu", "erin", "arai", true, false, true, false,
						160, 50, emptyImage, getResources().getColor(
								R.color.BlanchedAlmond), false),
				new Player("king tut", "matt", "tuttle", true, false, true,
						false, 182, 63, emptyImage, getResources().getColor(
								R.color.CornflowerBlue), false),
				new Player("dru", "andrew", "o'brien", true, false, true,
						false, 182, 63, emptyImage, getResources().getColor(
								R.color.DarkOrange), false),
				new Player("murder", "matt", "miguez", true, false, true,
						false, 182, 63, emptyImage, getResources().getColor(
								R.color.FireBrick), false),
				new Player("juice", "julian", "spring", false, true, true,
						false, 182, 63, emptyImage, getResources().getColor(
								R.color.Goldenrod), false),
				new Player("freeeedom", "mike", "freeman", true, false, true,
						false, 182, 63, emptyImage, getResources().getColor(
								R.color.HotPink), false),
				new Player("pilip", "phillip", "anderson", false, true, true,
						false, 182, 63, emptyImage, getResources().getColor(
								R.color.Green), true),
				new Player("sukes appeal", "jon", "sukovich", true, false,
						true, false, 182, 63, emptyImage, getResources()
								.getColor(R.color.Khaki), false) };

        Venue v1 = new Venue("Putnam St.", true);
        Venue v2 = new Venue("Verndale", false);
        Venue v3 = new Venue("Oxford", true);

//		Session s1 = new Session("league", GameType.POLISH_HORSESHOES,
//		GameSubtype.POLISH_SINGLES, SessionType.LEAGUE, 1, Venue);
//
//		Session s2 = new Session("league", GameType.BILLIARDS,
//		GameSubtype.EIGHTBALL, SessionType.LEAGUE, 1);

		try {
			Dao<Player, Long> playerDao = mData.getPlayerDao();
			Dao<Team, Long> teamDao = mData.getTeamDao();
			Dao<TeamMember, Long> tmDao = mData.getTeamMemberDao();
			Dao<Session, Long> sDao = mData.getSessionDao();
			Dao<Venue, Long> venueDao = mData.getVenueDao();
			for (Player p : players) {
				playerDao.create(p);
				Team t = new Team(p.getNickName(), 1, p.getColor(),
						p.getIsFavorite());
				teamDao.create(t);
				tmDao.create(new TeamMember(t, p));
			}

//			sessionDao.create(s1);
//			sessionDao.create(s2);

			venueDao.create(v1);
			venueDao.create(v2);
			venueDao.create(v3);
		} catch (SQLException e) {
			int duration = Toast.LENGTH_LONG;
			Toast.makeText(context, e.getMessage(), duration).show();
			loge("Populate database failed", e);
		}
	}

	// public void updateScores() {
	// List<Long> badGames = null;
	// List<Long> badThrows = null;
	// Dao<Game, Long> gDao;
	// Dao<Throw, Long> tDao;
	// try {
	//
	// gDao = Game.getDao(context);
	// tDao = Throw.getDao(context);
	// badGames = DatabaseUpgrader.updateScores(gDao, context);
	// if (badGames.size() > 0) {
	// Log.w("SimpleSettings",
	// "The following games had different scores after upgrade: "
	// + badGames.toString());
	// // throw new RuntimeException("Scores changed on upgrade");
	// } else {
	// Log.i("SimpleSettings",
	// "All game scores unchanged after upgrade!");
	// }
	//
	// badThrows = DatabaseUpgrader.checkThrows(tDao, context);
	// if (badThrows.size() > 0) {
	// Log.w("SimpleSettings", "The following throws are not valid: "
	// + badThrows.toString());
	// } else {
	// Log.i("SimpleSettings", "All throws are valid!");
	// }
	// } catch (SQLException e) {
	// int duration = Toast.LENGTH_LONG;
	// Toast.makeText(context, e.getMessage(), duration).show();
	// Log.e(PocketLeague.class.getName(), "Update of scores failed", e);
	// }
	// }

	public void saveDBdropbox() {
		Toast.makeText(context, "Saved to dropbox", Toast.LENGTH_SHORT).show();
		try {
			// Create DbxFileSystem for synchronized file access.
            DbxFileSystem dbxFs = DbxFileSystem.forAccount(mDbxAcctMgr.getLinkedAccount());
            String fileName = new SimpleDateFormat("yyyy-MM-dd_HH-mm'.db'",
					Locale.US).format(new Date());

			DbxPath phDBpath = new DbxPath(DbxPath.ROOT, fileName);
			if (!dbxFs.exists(phDBpath)) {
				DbxFile phDBfile = dbxFs.create(phDBpath);
				try {
					phDBfile.writeFromExistingFile(getInternalPath(), false);
				} finally {
					phDBfile.close();
				}
				mTestOutput.append("\nCreated new file '" + phDBpath + "'.\n");
			}
		} catch (IOException e) {
            mTestOutput.setText("Dropbox test failed: " + e);
        }
	}

	public void loadDBdropbox() {
		DbxPath latestFile = null;

		try {
			// Create DbxFileSystem for synchronized file access.
			DbxFileSystem dbxFs = DbxFileSystem.forAccount(mDbxAcctMgr
					.getLinkedAccount());

			// Print the contents of the root folder. This will block until we
			// can
			// sync metadata the first time.
			List<DbxFileInfo> infos = dbxFs.listFolder(DbxPath.ROOT);
			mTestOutput.setText("\nStored .db Files:\n");
			for (DbxFileInfo info : infos) {
				if (info.path.toString().contains(".db")) { // exclude files
															// that dont have
															// .db in the name
					if (latestFile == null) { // latestFile starts as null, so
												// make first file latest
						latestFile = info.path;
					} else { // compare each file to latestFile, update if
								// necessary
						if (info.modifiedTime.after(dbxFs
								.getFileInfo(latestFile).modifiedTime)) {
							latestFile = info.path;
						}
					}
					// list all the .db files in the dropbox folder
					mTestOutput.append("    " + info.path + ", "
							+ info.modifiedTime + '\n');
				}
			}

			// open the latest .db file and copy over the local database
			if (latestFile != null) {
				DbxFile latestDb = dbxFs.open(latestFile);
				copyDbxFile(latestDb, getInternalPath());
				mTestOutput.append("Loaded: " + latestDb.getPath() + '\n');
				latestDb.close();
			} else {
				mTestOutput.append("No database files were found.\n");
			}

		} catch (IOException e) {
			mTestOutput.setText("Dropbox test failed: " + e);
		}
	}

	File getInternalPath() {
		String dbPath = mData.getHelper().getReadableDatabase().getPath();
//		String dbPath = "/data/data/com.twobits.gametemplate/databases/gametemplate.db";

		File internalDB = new File(dbPath);
		return internalDB;
	}

	public static void copyDbxFile(DbxFile sourceFile, File destFile)
			throws IOException {
		if (!destFile.exists()) {
			destFile.createNewFile();
		}

		FileChannel source = null;
		FileChannel destination = null;

		try {
			source = sourceFile.getReadStream().getChannel();
			destination = new FileOutputStream(destFile).getChannel();
			destination.transferFrom(source, 0, source.size());
		} finally {
			if (source != null) {
				source.close();
			}
			if (destination != null) {
				destination.close();
			}
		}
	}
}
