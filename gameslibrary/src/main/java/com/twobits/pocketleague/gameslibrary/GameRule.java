package com.twobits.pocketleague.gameslibrary;

import com.twobits.pocketleague.gameslibrary.Rulesets.Billiards.EightBall;
import com.twobits.pocketleague.gameslibrary.Rulesets.Billiards.NineBall;
import com.twobits.pocketleague.gameslibrary.Rulesets.Darts.Cricket;
import com.twobits.pocketleague.gameslibrary.Rulesets.Polishhorseshoes.PolishSingles;
import com.twobits.pocketleague.gameslibrary.Rulesets.Undefined.Undefined;

public enum GameRule {
    EIGHTBALL(new EightBall()),
    NINEBALL(new NineBall()),
    CRICKET(new Cricket()),
    POLISH_SINGLES(new PolishSingles()),
    UNDEFINED(new Undefined());

    private RuleSet ruleset;

    private GameRule(RuleSet ruleset) {
        this.ruleset = ruleset;
    }

    public RuleSet toRuleSet() {
        return ruleset;
    }
}
