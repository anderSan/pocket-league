package com.twobits.pocketleague.gameslibrary;

import java.util.Arrays;
import java.util.List;

public enum GameType {BILLIARDS("Billiards", R.drawable.billiards,
                        Arrays.asList(GameRule.EIGHTBALL, GameRule.NINEBALL)),
                      CARDS("Cards", R.drawable.cards, Arrays.asList(GameRule.SPADES)),
                      DARTS("Darts", R.drawable.darts, Arrays.asList(GameRule.CRICKET)),
                      GOLF("Golf", R.drawable.golf, Arrays.asList(GameRule.DISC_GOLF)),
                      POLISH_HORSESHOES("Polish Horseshoes", R.drawable.polishhorseshoes,
                        Arrays.asList(GameRule.POLISH_SINGLES)),
    // TRIVIA("Trivia", R.drawable.test2, Arrays.asList()),
                      UNDEFINED("Undefined", R.drawable.undefined,
                        Arrays.asList(GameRule.UNDEFINED));

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
