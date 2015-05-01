package info.andersonpa.pocketleague.enums;

import info.andersonpa.pocketleague.Detail_Session_Elimination;
import info.andersonpa.pocketleague.Detail_Session_Ladder;
import info.andersonpa.pocketleague.Detail_Session_League;
import info.andersonpa.pocketleague.backend.Detail_Session_Base;

/** Enum for the different session types */
public enum SessionType {
	OPEN("Open", Detail_Session_Base.class),
	LEAGUE("League", Detail_Session_League.class),
	LADDER("Ladder", Detail_Session_Ladder.class),
	GROUP_STAGE("Group Stage", Detail_Session_Base.class),
	SNGL_ELIM("Single-elimination Tournament", Detail_Session_Elimination.class),
	DBL_ELIM("Double-elimination Tournament", Detail_Session_Elimination.class);

	private String sessiontype_label;
	private Class<?> sessiontype_activity;

	SessionType(String label, Class<?> detail_activity) {
		sessiontype_label = label;
		sessiontype_activity = detail_activity;
	}

	@Override
	public String toString() {
		return sessiontype_label;
	}

	public Class<?> toClass() {
		return sessiontype_activity;
	}
}
