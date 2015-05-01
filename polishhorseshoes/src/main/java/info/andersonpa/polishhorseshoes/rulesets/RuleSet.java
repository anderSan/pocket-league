package info.andersonpa.polishhorseshoes.rulesets;

import android.content.Context;
import android.widget.ImageView;

import info.andersonpa.polishhorseshoes.db.Throw;

public interface RuleSet {
    int getId();

    String getDescription();

    boolean useAutoFire();

    // Primary setting functions ==============================================
    void setThrowType(Throw t, int throwType);

    void setThrowResult(Throw t, int throwResult);

    void setDeadType(Throw t, int deadType);

    void setIsTipped(Throw t, boolean isTipped);

    void setOwnGoals(Throw t, boolean[] ownGoals);

    void setDefErrors(Throw t, boolean[] defErrors);

    // Scores and UI ==========================================================
    int[] getScoreDifferentials(Throw t);

    int[] getFinalScores(Throw t);

    String getSpecialString(Throw t);

    void setThrowDrawable(Throw t, ImageView iv);

    // Special Rules ==========================================================
    boolean isOffenseOnHill(Throw t);

    boolean isFiredOn(Throw t);

    boolean isOnFire(Throw t);

    void setFireCounts(Throw t, Throw previousThrow);

    // Validation =============================================================
    boolean isValid(Throw t, Context context);

    boolean isValid(Throw t);

    // Convenience Definitions ================================================
    boolean isStackHit(Throw t);

    boolean isOffensiveError(Throw t);

    boolean isDefensiveError(Throw t);
}