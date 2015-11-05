package net.as93.treasurehunt.models;

/**
 * Created by Alicia on 05/11/2015.
 */
public class Leg {

    private String name;
    private String location;
    private String position;
    private String description;
    private String latitude;
    private String longitude;
    private String question;
    private String answer;
    private String clue;


    public Leg(String name,
               String location,
               String position,
               String description,
               String latitude,
               String longitude,
               String question,
               String answer,
               String clue) {
        this.name = name;
        this.location = location;
        this.position = position;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.question = question;
        this.answer = answer;
        this.clue = clue;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public String getPosition() {
        return position;
    }

    public String getDescription() {
        return description;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

    public String getClue() {
        return clue;
    }
}
