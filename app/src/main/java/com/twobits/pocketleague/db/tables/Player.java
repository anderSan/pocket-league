package com.twobits.pocketleague.db.tables;

import android.content.Context;
import android.widget.Toast;

import com.couchbase.lite.Database;
import com.j256.ormlite.dao.Dao;
import com.twobits.pocketleague.db.DatabaseHelper;

import java.sql.SQLException;
import java.util.List;

public class Player extends CouchDocumentBase { //implements Comparable<Player> {
    public static final String NICK_NAME = "nickname";
    public static final String FIRST_NAME = "first_name";
    public static final String LAST_NAME = "last_name";
    public static final String IS_LEFT_HANDED = "is_left_handed";
    public static final String IS_RIGHT_HANDED = "is_right_handed";
    public static final String IS_LEFT_FOOTED = "is_left_footed";
    public static final String IS_RIGHT_FOOTED = "is_right_footed";
    public static final String HEIGHT = "height_cm";
    public static final String WEIGHT = "weight_kg";
    public static final String COLOR = "color";
    public static final String IS_ACTIVE = "is_active";
    public static final String IS_FAVORITE = "is_favorite";

    public Player(String nickname, int color) {
        // nickname should be unique
        content.put(NICK_NAME, nickname);
        content.put(FIRST_NAME, "");
        content.put(LAST_NAME, "");
        content.put(IS_LEFT_HANDED, false);
        content.put(IS_RIGHT_HANDED, false);
        content.put(IS_LEFT_FOOTED, false);
        content.put(IS_RIGHT_FOOTED, false);
        content.put(HEIGHT, 0);
        content.put(WEIGHT, 0);
        content.put(COLOR, color);
        content.put(IS_ACTIVE, true);
        content.put(IS_FAVORITE, false);
    }

    public Player(String nickname, String first_name, String last_name, boolean is_left_handed,
                  boolean is_right_handed, boolean is_left_footed, boolean is_right_footed,
                  int height_cm, int weight_kg, int color, boolean is_favorite) {
        content.put(NICK_NAME, nickname);
        content.put(FIRST_NAME, first_name);
        content.put(LAST_NAME, last_name);
        content.put(IS_LEFT_HANDED, is_left_handed);
        content.put(IS_RIGHT_HANDED, is_right_handed);
        content.put(IS_LEFT_FOOTED, is_left_footed);
        content.put(IS_RIGHT_FOOTED, is_right_footed);
        content.put(HEIGHT, height_cm);
        content.put(WEIGHT, weight_kg);
        content.put(COLOR, color);
        content.put(IS_ACTIVE, true);
        content.put(IS_FAVORITE, is_favorite);
    }

    public Player(Database database, String id){
        super(database, id);
    }


    public String getNickName() {
        return (String) content.get(NICK_NAME);
    }

    public void setNickName(String nickname) {
        content.put(NICK_NAME, nickname);
    }

    public String getFirstName() {
        return (String) content.get(FIRST_NAME);
    }

    public void setFirstName(String first_name) {
        content.put(FIRST_NAME, first_name);
    }

    public String getLastName() {
        return (String) content.get(LAST_NAME);
    }

    public void setLastName(String last_name) {
        content.put(LAST_NAME, last_name);
    }

    public boolean getIsLeftHanded() {
        return (boolean) content.get(IS_LEFT_HANDED);
    }

    public void setIsLeftHanded(boolean is_left_handed) {
        content.put(IS_LEFT_HANDED, is_left_handed);
    }

    public boolean getIsRightHanded() {
        return (boolean) content.get(IS_RIGHT_HANDED);
    }

    public void setIsRightHanded(boolean is_right_handed) {
        content.put(IS_RIGHT_HANDED, is_right_handed);
    }

    public boolean getIsLeftFooted() {
        return (boolean) content.get(IS_LEFT_FOOTED);
    }

    public void setIsLeftFooted(boolean is_left_footed) {
        content.put(IS_LEFT_FOOTED, is_left_footed);
    }

    public boolean getIsRightFooted() {
        return (boolean) content.get(IS_RIGHT_FOOTED);
    }

    public void setIsRightFooted(boolean is_right_footed) {
        content.put(IS_RIGHT_FOOTED, is_right_footed);
    }

    public int getHeight() {
        return (int) content.get(HEIGHT);
    }

    public void setHeight_cm(int height_cm) {
        content.put(HEIGHT, height_cm);
    }

    public int getWeight() {
        return (int) content.get(WEIGHT);
    }

    public void setWeight_kg(int weight_kg) {
        content.put(WEIGHT, weight_kg);
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

    public String getDisplayName() {
        return getFirstName() + " \"" + getNickName() + "\" " + getLastName();
    }

    public String getFullName() {
        return getFirstName() + " " + getLastName();
    }

//    public int compareTo(Player another) {
//        if (id < another.id) {
//            return -1;
//        } else if (id == another.id) {
//            return 0;
//        } else {
//            return 1;
//        }
//    }
//
//    public boolean equals(Object o) {
//        if (!(o instanceof Player)) return false;
//        Player another = (Player) o;
//        return id == another.id;
//    }
//
//    public boolean exists(Context context) {
//        return exists(nickname, context);
//    }
//
//    public static boolean exists(String nickname, Context context) {
//        if (nickname == null) {
//            return false;
//        }
//
//        try {
//            List<Player> pList = getDao(context).queryBuilder().where().eq(Player.NICK_NAME, nickname).query();
//            return !pList.isEmpty();
//        } catch (SQLException e) {
//            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
//            return false;
//        }
//
//    }
}
