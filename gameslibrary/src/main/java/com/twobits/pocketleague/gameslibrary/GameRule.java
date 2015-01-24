package com.twobits.pocketleague.gameslibrary;

import com.twobits.pocketleague.gameslibrary.Rulesets.Billiards.EightBall;
import com.twobits.pocketleague.gameslibrary.Rulesets.Billiards.NineBall;
import com.twobits.pocketleague.gameslibrary.Rulesets.Darts.Cricket;
import com.twobits.pocketleague.gameslibrary.Rulesets.Polishhorseshoes.PolishSingles;
import com.twobits.pocketleague.gameslibrary.Rulesets.Undefined.Undefined;

public enum GameRule {
    CRICKET(new Cricket()),
    DISC_GOLF(new Undefined()),
    EIGHTBALL(new EightBall()),
    NINEBALL(new NineBall()),
    POLISH_SINGLES(new PolishSingles()),
    SPADES(new Undefined()),
    UNDEFINED(new Undefined());

    private RuleSet ruleset;

    private GameRule(RuleSet ruleset) {
        this.ruleset = ruleset;
    }

    public RuleSet toRuleSet() {
        return ruleset;
    }
}
