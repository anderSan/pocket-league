package info.andersonpa.pocketleague;

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
import info.andersonpa.pocketleague.backend.Fragment_Base;
import info.andersonpa.pocketleague.db.DbxInfo;
import info.andersonpa.pocketleague.db.tables.Player;
import info.andersonpa.pocketleague.db.tables.Session;
import info.andersonpa.pocketleague.db.tables.SessionMember;
import info.andersonpa.pocketleague.db.tables.Venue;
import info.andersonpa.pocketleague.enums.SessionType;
import info.andersonpa.pocketleague.gameslibrary.GameSubtype;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DbSettings extends Fragment_Base {

	private DbxAccountManager mDbxAcctMgr;
	private static final String appKey = DbxInfo.appKey;
	private static final String appSecret = DbxInfo.appSecret;
	private Button mLinkButton;
	private Button dbxSaveButton;
	private Button dbxSaveOtherButton;
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

        Button mClearOtherButton = (Button) rootView.findViewById(R.id.db_clearOtherDB);
        mClearOtherButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                clearOtherTables();
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

        dbxSaveOtherButton = (Button) rootView.findViewById(R.id.db_save_other_dropbox);
        dbxSaveOtherButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                saveOtherDB();
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
        dbxSaveOtherButton.setVisibility(View.VISIBLE);
		dbxLoadButton.setVisibility(View.VISIBLE);
	}

	private void showUnlinkedView() {
		mLinkButton.setVisibility(View.VISIBLE);
		dbxSaveButton.setVisibility(View.GONE);
        dbxSaveOtherButton.setVisibility(View.GONE);
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
        mData.deleteDatabase();
//        database = mData.getDatabase();
	}

    public void clearOtherTables() {
        List<String> other_dbs = new ArrayList<>();
        other_dbs.add("/data/data/info.andersonpa.polishhorseshoes/databases/polishhorseshoes.db");
        other_dbs.add("/data/data/info.andersonpa.gametemplate/databases/gametemplate.db");
        for (String db_path : other_dbs) {
            File external_db = new File(db_path);
            boolean deleted = external_db.delete();
            if (deleted) {
                log("The database was deleted!");
            } else {
                log("Did not delete the database.");
            }
        }
    }

	public void doPopulateTest() {
		Player[] players = {
				new Player(database(), "mike c", "Michael", "Cannamela", true, false, true, false,
                        170, 70, getResources().getColor(R.color.SaddleBrown), true),
				new Player(database(), "samu", "Erin", "Arai", true, false, true, false,
						160, 50, getResources().getColor(R.color.BlanchedAlmond), false),
				new Player(database(), "king tut", "Matt", "Tuttle", true, false, true, false,
                        182, 63, getResources().getColor(R.color.CornflowerBlue), false),
				new Player(database(), "dru", "andrew", "o'brien", true, false, true,
						false, 182, 63, getResources().getColor(R.color.DarkOrange), false),
				new Player(database(), "murder", "matt", "miguez", true, false, true,
						false, 182, 63, getResources().getColor(R.color.FireBrick), false),
				new Player(database(), "juice", "julian", "spring", false, true, true,
						false, 182, 63, getResources().getColor(R.color.Goldenrod), false),
				new Player(database(), "freeeedom", "mike", "freeman", true, false, true,
						false, 182, 63, getResources().getColor(R.color.HotPink), false),
				new Player(database(), "pilip", "phillip", "anderson", false, true, true, false,
						182, 63, getResources().getColor(R.color.Green), true),
				new Player(database(), "sukes appeal", "jon", "sukovich", true, false, true, false,
                        182, 63, getResources().getColor(R.color.Khaki), false)
        };
        for (Player p : players) {
            p.update();
        }

        Venue[] venues = {
                new Venue(database(), "Putnam St.", true),
                new Venue(database(), "Verndale", false),
                new Venue(database(), "Oxford", true)
        };
        for (Venue v : venues) {
            v.update();
        }

        List<SessionMember> members = new ArrayList<>();
        members.add(new SessionMember(players[0], 1));
        members.add(new SessionMember(players[1], 2));
        members.add(new SessionMember(players[2], 3));
        members.add(new SessionMember(players[3], 4));
        members.add(new SessionMember(players[4], 5));
        members.add(new SessionMember(players[5], 6));

        Session[] sessions = {
                new Session(database(), "Test Single Elim Session", SessionType.SNGL_ELIM,
                        GameSubtype.POLISH_SINGLES, 0, 1, venues[2], members, true),
                new Session(database(), "Test Double Elim Session", SessionType.DBL_ELIM,
                        GameSubtype.EIGHTBALL, 0, 1, venues[0], members, false)
        };
        for (Session s : sessions) {
            s.update();
        }
	}

	public void saveDBdropbox() {
        // TODO: zip all of the databases together before pushing to dropbox
        // http://www.jondev.net/articles/Zipping_Files_with_Android_%28Programmatically%29

		Toast.makeText(context, "Saved to dropbox", Toast.LENGTH_SHORT).show();
		try {
			// Create DbxFileSystem for synchronized file access.
            DbxFileSystem dbxFs = DbxFileSystem.forAccount(mDbxAcctMgr.getLinkedAccount());
            String fileName = new SimpleDateFormat("yyyy-MM-dd_HH-mm'.db'",
					Locale.US).format(new Date());

			DbxPath db_path = new DbxPath(DbxPath.ROOT, fileName);
			if (!dbxFs.exists(db_path)) {
				DbxFile phDBfile = dbxFs.create(db_path);
				try {
					phDBfile.writeFromExistingFile(getInternalPath(), false);
				} finally {
					phDBfile.close();
				}
				mTestOutput.append("\nCreated new file '" + db_path + "'.\n");
			}
		} catch (IOException e) {
            mTestOutput.setText("Dropbox test failed: " + e);
        }
	}

    public void saveOtherDB() {
        try {
            // Create DbxFileSystem for synchronized file access.
            DbxFileSystem dbxFs = DbxFileSystem.forAccount(mDbxAcctMgr.getLinkedAccount());
            List<String> filenames = new ArrayList<>();
            filenames.add("polishhorseshoes.db");
            filenames.add("gametemplate.db");

            List<String> other_dbs = new ArrayList<>();
            other_dbs.add("/data/data/info.andersonpa.polishhorseshoes/databases/polishhorseshoes.db");
            other_dbs.add("/data/data/info.andersonpa.gametemplate/databases/gametemplate.db");

            int ii = 0;
            File out_file;
            for (String name : filenames) {
                out_file = new File(other_dbs.get(ii));
                if (out_file.exists()) {
                    DbxPath db_path = new DbxPath(DbxPath.ROOT, name);
                    if (!dbxFs.exists(db_path)) {
                        DbxFile phDBfile = dbxFs.create(db_path);
                        try {
                            phDBfile.writeFromExistingFile(out_file, false);
                        } finally {
                            phDBfile.close();
                        }
                        mTestOutput.append("\nCreated new file '" + db_path + "'.\n");
                    }
                }
                ii++;
            }
        } catch (IOException e) {
            mTestOutput.setText("Dropbox test failed: " + e);
        }
    }

	public void loadDBdropbox() {
        // once zipping is implemented, unzipping will have to be done here. See:
        // http://jondev.net/articles/Unzipping_Files_with_Android_(Programmatically)

		DbxPath latestFile = null;

		try {
			// Create DbxFileSystem for synchronized file access.
			DbxFileSystem dbxFs = DbxFileSystem.forAccount(mDbxAcctMgr
					.getLinkedAccount());

			// Print the contents of the root folder. This will block until we can
			// sync metadata the first time.
			List<DbxFileInfo> infos = dbxFs.listFolder(DbxPath.ROOT);
			mTestOutput.setText("\nStored .db Files:\n");
			for (DbxFileInfo info : infos) {
				if (info.path.toString().contains(".db")) {
				// exclude files that dont have .db in the name
					if (latestFile == null) {
					// latestFile starts as null, so make first file latest
						latestFile = info.path;
					} else { // compare each file to latestFile, update if necessary
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
        File internal = context.getFilesDir();
        File sub = new File(internal, "pocketleague/attachments");

        File db = new File(internal, "pocketleague.cblite");
        for (File f : internal.listFiles()) {
            log("File: " + f.getName());
        }

        for (File f : sub.listFiles()) {
            log("File (sub): " + f.getName());
            log("is directory: " + f.isDirectory());
        }
//		String dbPath = mData.getHelper().getReadableDatabase().getPath();
//		String dbPath = "/data/data/info.andersonpa.gametemplate/databases/gametemplate.db";

//		return new File(dbPath);
        return db;
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
