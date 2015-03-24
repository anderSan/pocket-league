package com.twobits.pocketleague.backend;

import android.graphics.Color;
import android.view.View;

import com.twobits.pocketleague.db.tables.SessionMember;
import com.twobits.pocketleague.db.tables.Team;
import com.twobits.pocketleague.enums.BrNodeType;

public class Item_Match {
    private int id_in_session = -1;
    private String game_id = "";
    private boolean viewable = false;

    private SessionMember upper_member;
    private BrNodeType upper_node_type = BrNodeType.UNSET;
    private View upper_view;
    private boolean upper_is_labelled = false;

    private SessionMember lower_member;
    private BrNodeType lower_node_type = BrNodeType.UNSET;
    private View lower_view;
    private boolean lower_is_labelled = false;

    Item_Match(int id_in_session) {
        this.id_in_session = id_in_session;
    }

    Item_Match(int id_in_session, String game_id, SessionMember upper_member, SessionMember lower_member) {
        this(id_in_session);
        setGameId(game_id);
        setUpperMember(upper_member);
        setLowerMember(lower_member);
    }

    public String getTitle() {
        // upper team
        String title = "";
        if (upper_node_type == BrNodeType.UNSET || upper_node_type == BrNodeType.RESPAWN) {
            title += "Unknown";
        } else {
            title += upper_member.getTeam().getName();
            if (upper_node_type == BrNodeType.WIN) {
                title += " (W)";
            } else if (upper_node_type == BrNodeType.LOSS) {
                title += " (L)";
            }
        }

        // lower team
        if (lower_node_type == BrNodeType.UNSET || lower_node_type == BrNodeType.RESPAWN) {
            title += " -vs- Unknown";
        } else if (lower_node_type == BrNodeType.NA) {
            title += ", bracket winner.";
        } else {
            title += " -vs- " + lower_member.getTeam().getName();
            if (lower_node_type == BrNodeType.WIN) {
                title += " (W)";
            } else if (lower_node_type == BrNodeType.LOSS) {
                title += " (L)";
            }
        }

        return title;
    }

    public String getSubtitle() {
        String subtitle = "match: " + getIdInSession() + ", gameId: " + game_id;
        return subtitle;
    }

    public int getIdInSession() {
        return id_in_session;
    }

    public String getGameId() {
        return game_id;
    }

    public void setGameId(String game_id) {
        this.game_id = game_id;
        if (game_id != "") {
            this.viewable = true;
        }
    }

    public boolean getCreatable() {
        return (game_id.equals("") && getUpperTeam() != null && getLowerTeam() != null);
    }

    public boolean getViewable() {
        return viewable;
    }

    public Team getUpperTeam() {
        if (upper_member != null) {
            return upper_member.getTeam();
        } else {
            return null;
        }
    }

    public int getUpperColor() {
        if (getUpperTeam() != null) {
            return getUpperTeam().getColor();
        } else {
            return Color.LTGRAY;
        }
    }

    public String getUpperSeedName() {
        return getSeedName(upper_member);
    }

    public String getUpperRespawnName() {
        if (getUpperTeam() == null) {
            return "(" + (char) (upper_member.getSeed() + 65) + ")";
        } else {
            return getUpperTeam().getName();
        }
    }

    public SessionMember getUpperMember() {
        return upper_member;
    }

    public void setUpperMember(SessionMember upper_member) {
        this.upper_member = upper_member;
    }

    public BrNodeType getUpperNodeType() {
        return upper_node_type;
    }

    public void setUpperNodeType(BrNodeType type) {
        upper_node_type = type;
    }

    public View getUpperView() {
        return upper_view;
    }

    public void setUpperView(View view) {
        upper_view = view;
    }

    public Team getLowerTeam() {
        if (lower_member != null) {
            return lower_member.getTeam();
        } else {
            return null;
        }
    }

    public int getLowerColor() {
        if (getLowerTeam() != null) {
            return getLowerTeam().getColor();
        } else {
            return Color.LTGRAY;
        }
    }

    public String getLowerSeedName() {
        return getSeedName(lower_member);
    }

    public String getLowerRespawnName() {
        if (getLowerTeam() == null) {
            return "(" + (char) (lower_member.getSeed() + 65) + ")";
        } else {
            return getLowerTeam().getName();
        }
    }

    public SessionMember getLowerMember() {
        return lower_member;
    }

    public void setLowerMember(SessionMember lower_member) {
        this.lower_member = lower_member;
    }

    public BrNodeType getLowerNodeType() {
        return lower_node_type;
    }

    public void setLowerNodeType(BrNodeType type) {
        lower_node_type = type;
    }

    public View getLowerView() {
        return lower_view;
    }

    public void setLowerView(View view) {
        lower_view = view;
    }

    private String getSeedName(SessionMember sm) {
        return "(" + String.valueOf(sm.getSeed() + 1) + ") " + sm.getTeam().getName();
    }

    public void setOnClickListener(View.OnClickListener ocl) {
        upper_view.setOnClickListener(ocl);
        lower_view.setOnClickListener(ocl);
    }

    public boolean getUpperIsLabelled() {
        return upper_is_labelled;
    }

    public void setUpperIsLabelled(boolean is_labelled) {
        upper_is_labelled = is_labelled;
    }

    public boolean getLowerIsLabelled() {
        return lower_is_labelled;
    }

    public void setLowerIsLabelled(boolean is_labelled) {
        lower_is_labelled = is_labelled;
    }

    public boolean isBye() {
        return upper_node_type == BrNodeType.BYE || lower_node_type == BrNodeType.BYE;
    }

    public void setWinner(boolean upper_wins) {
        if (upper_wins) {
            upper_node_type = BrNodeType.WIN;
            if (lower_node_type != BrNodeType.BYE) {
                lower_node_type = BrNodeType.LOSS;
            }
        } else {
            lower_node_type = BrNodeType.WIN;
            if (upper_node_type != BrNodeType.BYE) {
                upper_node_type = BrNodeType.LOSS;
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Item_Match that = (Item_Match) o;

        if (id_in_session != that.id_in_session) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id_in_session;
    }
}
