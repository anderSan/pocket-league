package info.andersonpa.pocketleague.gameslibrary;

import info.andersonpa.pocketleague.gameslibrary.Descriptors.Billiards.EightBall;
import info.andersonpa.pocketleague.gameslibrary.Descriptors.Billiards.NineBall;
import info.andersonpa.pocketleague.gameslibrary.Descriptors.Darts.Cricket;
import info.andersonpa.pocketleague.gameslibrary.Descriptors.DiscGolf;
import info.andersonpa.pocketleague.gameslibrary.Descriptors.Golf;
import info.andersonpa.pocketleague.gameslibrary.Descriptors.PolishHorseshoes;
import info.andersonpa.pocketleague.gameslibrary.Descriptors.Undefined;

public enum GameSubtype {
    DARTS_CRICKET(new Cricket()),
    DISC_GOLF(new DiscGolf()),
    EIGHTBALL(new EightBall()),
    GOLF(new Golf()),
    NINEBALL(new NineBall()),
    POLISH_HORSESHOES(new PolishHorseshoes()),
    SPADES(new Undefined()),
    UNDEFINED(new Undefined());

    private GameDescriptor descriptor;

    GameSubtype(GameDescriptor descriptor) {
        this.descriptor = descriptor;
    }

    public GameDescriptor toDescriptor() {
        return descriptor;
    }

    public GameType toGameType() {
        return descriptor.getGameType();
    }

    public String toString() {
        return descriptor.getName();
    }
}
