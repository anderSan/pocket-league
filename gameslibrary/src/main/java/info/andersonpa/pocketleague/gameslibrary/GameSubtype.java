package info.andersonpa.pocketleague.gameslibrary;

import info.andersonpa.pocketleague.gameslibrary.Descriptors.Badminton;
import info.andersonpa.pocketleague.gameslibrary.Descriptors.Billiards.EightBall;
import info.andersonpa.pocketleague.gameslibrary.Descriptors.Billiards.NineBall;
import info.andersonpa.pocketleague.gameslibrary.Descriptors.Cards.Hearts;
import info.andersonpa.pocketleague.gameslibrary.Descriptors.Cards.Spades;
import info.andersonpa.pocketleague.gameslibrary.Descriptors.Darts.Cricket;
import info.andersonpa.pocketleague.gameslibrary.Descriptors.Darts.FiveZeroOne;
import info.andersonpa.pocketleague.gameslibrary.Descriptors.DiscGolf;
import info.andersonpa.pocketleague.gameslibrary.Descriptors.Golf;
import info.andersonpa.pocketleague.gameslibrary.Descriptors.PolishHorseshoes;
import info.andersonpa.pocketleague.gameslibrary.Descriptors.Undefined;

public enum GameSubtype {
    BADMINTON(new Badminton()),
    BILLIARDS_EIGHTBALL(new EightBall()),
    BILLIARDS_NINEBALL(new NineBall()),
    CARDS_HEARTS(new Hearts()),
    CARDS_SPADES(new Spades()),
    DARTS_CRICKET(new Cricket()),
    DARTS_FIVEZEROONE(new FiveZeroOne()),
    DISC_GOLF(new DiscGolf()),
    GOLF(new Golf()),
    POLISH_HORSESHOES(new PolishHorseshoes()),
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
