package com.twobits.pocketleague.db.tables;

import com.couchbase.lite.Database;

import java.util.ArrayList;
import java.util.List;

public class Team extends CouchDocumentBase {
    static final String TYPE = "team";
	public static final String NAME = "team_name";
    public static final String MEMBERS = "member_ids";
	public static final String COLOR = "color";
	public static final String IS_ACTIVE = "is_active";
	public static final String IS_FAVORITE = "is_favorite";

	public Team(String team_name, List<Team> members, int color, boolean is_favorite) {
        // name and size combination should be unique
        content.put("type", TYPE);
        content.put(NAME, team_name);
        content.put(MEMBERS, members);
        content.put(COLOR, color);
        content.put(IS_ACTIVE, true);
        content.put(IS_FAVORITE, is_favorite);
	}

	public Team(Database database, String id){
        super(database, id);
    }

	public String getName() {
		return (String) content.get(NAME);
	}

	public void setName(String team_name) {
		content.put(NAME, team_name);
	}

    public List<Player> getMembers(Database database) {
        List<Player> members = new ArrayList<>();
        for (String member_id : (List<String>) content.get(MEMBERS)) {
            members.add(new Player(database, member_id));
        }
        return members;
    }

	public int getColor() {
		return (int) content.get(COLOR);
	}

	public void setColor(int color) {
		content.put(COLOR, color);
	}

    public boolean getIsActive() {
        return (boolean) content.get(IS_ACTIVE);
    }

    public void setIsActive(boolean is_active) {
        content.put(IS_ACTIVE, is_active);
    }

    public boolean getIsFavorite() {
        return (boolean) content.get(IS_FAVORITE);
    }

    public void setIsFavorite(boolean is_favorite) {
        content.put(IS_FAVORITE, is_favorite);
    }

	// =========================================================================
	// Additional methods
	// =========================================================================

    public int getSize() {
        return ((List<String>) content.get(MEMBERS)).size();
    }

//	public boolean exists(Context context) {
//		return exists(name, context);
//	}

//	public static boolean exists(String name, Context context) {
//		if (name == null) {
//			return false;
//		}
//
//		try {
//			List<Team> tList = getDao(context).queryBuilder().where()
//					.eq(Team.NAME, name).query();
//            return !tList.isEmpty();
//		} catch (SQLException e) {
//			Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
//			return false;
//		}
//	}
}
