package com.twobits.pocketleague.gameslibrary;

import java.util.Arrays;
import java.util.List;

public enum GameType {BILLIARDS("Billiards", R.drawable.test1, Arrays.asList(GameRule.EIGHTBALL,
        GameRule.NINEBALL)),
    DARTS("Darts", R.drawable.test2, Arrays.asList(GameRule.CRICKET)),
    // DISC_GOLF("Disc Golf", R.drawable.test3, Arrays.asList("")),
    POLISH_HORSESHOES("Polish Horseshoes", R.drawable.test4, Arrays
            .asList(GameRule.POLISH_SINGLES)),
    // PUB_TRIVIA("Pub Trivia", R.drawable.test2, Arrays.asList()),
    UNDEFINED("Undefined", R.drawable.ic_launcher, Arrays
            .asList(GameRule.UNDEFINED));

    private String gametype_label;
    private int drawable_id;
    private List<GameRule> game_rules;

    private GameType(String label, int drawable_id, List<GameRule> game_rules) {
        this.gametype_label = label;
        this.drawable_id = drawable_id;
        this.game_rules = game_rules;
    }

    @Override
    public String toString() {
        return gametype_label;
    }

    public int toDrawableId() {
        return drawable_id;
    }

    public List<GameRule> toGameRules() {
        return game_rules;
    }
}
