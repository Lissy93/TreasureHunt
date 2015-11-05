package net.as93.treasurehunt.models;

/**
 * Created by Alicia on 09/10/2015.
 */
public class Hunt {
    private String title;
    private String creator;
    public Hunt(String title, String creator) {
        this.title = title;
        this.creator = creator;
    }
    public String getTitle(){
        return title;
    }
    public String getCreator() {
        return creator;
    }
    public String toString() {
        return title;
    }
}
