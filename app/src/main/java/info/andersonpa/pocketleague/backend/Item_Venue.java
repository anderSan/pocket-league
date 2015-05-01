package info.andersonpa.pocketleague.backend;

public class Item_Venue {
    public String vId;
    public String name;
    private boolean is_favorite;

    public Item_Venue(String vId, String name, boolean is_favorite) {
        this.vId = vId;
        this.name = name;
        this.is_favorite = is_favorite;
    }

    public String getId() {
        return vId;
    }

    public String getName() {
        return name;
    }

    public boolean getIsFavorite() {
        return is_favorite;
    }
}
