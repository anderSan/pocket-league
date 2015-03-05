package com.twobits.pocketleague.gameslibrary;

import com.twobits.pocketleague.gameslibrary.Descriptors.Billiards.EightBall;
import com.twobits.pocketleague.gameslibrary.Descriptors.Billiards.NineBall;
import com.twobits.pocketleague.gameslibrary.Descriptors.Darts.Cricket;
import com.twobits.pocketleague.gameslibrary.Descriptors.DiscGolf;
import com.twobits.pocketleague.gameslibrary.Descriptors.Golf;
import com.twobits.pocketleague.gameslibrary.Descriptors.Polishhorseshoes.PolishDoubles;
import com.twobits.pocketleague.gameslibrary.Descriptors.Polishhorseshoes.PolishSingles;
import com.twobits.pocketleague.gameslibrary.Descriptors.Undefined.Undefined;

public enum GameSubtype {
    DARTS_CRICKET(GameType.DARTS, new Cricket()),
    DISC_GOLF(GameType.DISC_GOLF, new DiscGolf()),
    EIGHTBALL(GameType.BILLIARDS, new EightBall()),
    GOLF(GameType.GOLF, new Golf()),
    NINEBALL(GameType.BILLIARDS, new NineBall()),
    POLISH_SINGLES(GameType.POLISH_HORSESHOES, new PolishSingles()),
    POLISH_DOUBLES(GameType.POLISH_HORSESHOES, new PolishDoubles()),
    SPADES(GameType.CARDS, new Undefined()),
    UNDEFINED(GameType.UNDEFINED, new Undefined());

    private GameType game_type;

    private GameDescriptor descriptor;

    private GameSubtype(GameType game_type, GameDescriptor descriptor) {
        this.game_type = game_type;
        this.descriptor = descriptor;
    }

    public String toString() {
        return "Hello"; //descriptor.getName();
    }

    public GameType toGameType() {
        return game_type;
    }

    public GameDescriptor toDescriptor() {
        return descriptor;
    }
}
