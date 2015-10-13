package net.as93.treasurehunt.models;

/**
 * Created by Alicia on 09/10/2015.
 */
public class Item {
    private String title;
    private String link;
    public Item(String title, String link) {
        this.title = title;
        this.link = link;
    }
    public String getTitle(){
        return title;
    }
    public String getLink() {
        return link;
    }
    public String toString() {
        return title;
    }
}
