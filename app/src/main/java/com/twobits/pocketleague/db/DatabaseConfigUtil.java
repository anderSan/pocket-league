package com.twobits.pocketleague.db;

import com.j256.ormlite.android.apptools.OrmLiteConfigUtil;
import com.twobits.pocketleague.db.tables.Game;
import com.twobits.pocketleague.db.tables.GameMember;
import com.twobits.pocketleague.db.tables.Player;
import com.twobits.pocketleague.db.tables.Session;
import com.twobits.pocketleague.db.tables.SessionMember;
import com.twobits.pocketleague.db.tables.Team;
import com.twobits.pocketleague.db.tables.TeamMember;
import com.twobits.pocketleague.db.tables.Venue;

public class DatabaseConfigUtil extends OrmLiteConfigUtil {
	private static final Class<?>[] classes = new Class[] { Game.class,
			GameMember.class, Player.class, Session.class, SessionMember.class,
			Team.class, TeamMember.class, Venue.class };

	public static void main(String[] args) throws Exception {
		writeConfigFile("ormlite_config.txt", classes);
	}
}
