package info.andersonpa.pocketleague.gameslibrary;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum GameType {
    BILLIARDS("Billiards", R.drawable.billiards, Arrays.asList(GameSubtype.EIGHTBALL,
            GameSubtype.NINEBALL)),
    CARDS("Cards", R.drawable.cards, Collections.singletonList(GameSubtype.SPADES)),
    DARTS("Darts", R.drawable.darts, Collections.singletonList(GameSubtype.DARTS_CRICKET)),
    DISC_GOLF("Disc Golf", R.drawable.golf, Collections.singletonList(GameSubtype.DISC_GOLF)),
    GOLF("Golf", R.drawable.golf, Collections.singletonList(GameSubtype.GOLF)),
    POLISH_HORSESHOES("Polish Horseshoes", R.drawable.polishhorseshoes,
            Collections.singletonList(GameSubtype.POLISH_HORSESHOES)),
    // TRIVIA("Trivia", R.drawable.test2, Arrays.asList()),
    UNDEFINED("Undefined", R.drawable.undefined, Collections.singletonList(GameSubtype.UNDEFINED));

    private String gametype_label;
    private int drawable_id;
    private List<GameSubtype> game_subtypes;

    GameType(String label, int drawable_id, List<GameSubtype> game_subtypes) {
        this.gametype_label = label;
        this.drawable_id = drawable_id;
        this.game_subtypes = game_subtypes;
    }

    @Override
    public String toString() {
        return gametype_label;
    }

    public int toDrawableId() {
        return drawable_id;
    }

    public List<GameSubtype> toGameSubtype() {
        return game_subtypes;
    }
}
