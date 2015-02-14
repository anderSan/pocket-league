package com.twobits.polishhorseshoes.throwstats;

import com.twobits.polishhorseshoes.db.Throw;

import java.util.Set;

/**
 * This class provides one method, which maps a throw to an string.
 * Should be used to e.g. count whether a throw meets some certain criterion,
 * like being either a bottle, pole, or cup hit.
 */
public interface ThrowIndicator {

    public String indicate(Throw t);

    public Set<String> enumerate();

}
