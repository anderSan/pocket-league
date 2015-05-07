package info.andersonpa.pocketleague.backend;

public class Item_Team {
    public String tId;
    public String name;
    public int color;
    private boolean is_favorite;

    public Item_Team(String tId, String name, int color, boolean is_favorite) {
        this.tId = tId;
        this.name = name;
        this.color = color;
        this.is_favorite = is_favorite;
    }

    public String getId() {
        return tId;
    }

    public String getName() {
        return name;
    }

    public Integer getColor() {
        return color;
    }

    public boolean getIsFavorite() {
        return is_favorite;
    }
}
