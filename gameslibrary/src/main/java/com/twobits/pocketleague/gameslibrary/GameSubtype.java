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
    DARTS_CRICKET(new Cricket()),
    DISC_GOLF(new DiscGolf()),
    EIGHTBALL(new EightBall()),
    GOLF(new Golf()),
    NINEBALL(new NineBall()),
    POLISH_SINGLES(new PolishSingles()),
    POLISH_DOUBLES(new PolishDoubles()),
    SPADES(new Undefined()),
    UNDEFINED(new Undefined());

    private GameDescriptor descriptor;

    private GameSubtype(GameDescriptor descriptor) {
        this.descriptor = descriptor;
    }

    public GameDescriptor toDescriptor() {
        return descriptor;
    }
}
