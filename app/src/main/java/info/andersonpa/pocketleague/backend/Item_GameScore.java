package info.andersonpa.pocketleague.backend;

public class Item_GameScore {
    private String member_name;
    private int member_score;

    public Item_GameScore(String name, Integer score) {
        this.member_name = name;
        this.member_score = score;
    }

    public String getMemberName() {
        return member_name;
    }

//    public void setMemberName(String name) {
//        this.member_name = name;
//    }

    public int getMemberScore() {
        return member_score;
    }

    public void setMemberScore(Integer score) {
        this.member_score = score;
    }
}