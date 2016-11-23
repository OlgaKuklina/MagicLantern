package com.ok.lab.magiclantern.data;

/**
 * Created by olgakuklina on 2016-11-20.
 */

public class CastData {

    private String actorName;
    private String profilePath;
    private String character;
    private int personId;
    private int castOrder;

    public CastData(String actorName, String profilePath, String character, int personId, int castOrder) {
        this.actorName = actorName;
        this.profilePath = profilePath;
        this.character = character;
        this.personId = personId;
        this.castOrder = castOrder;
    }

    public String getCastName() {
        return actorName;
    }

    public String getCastImagePath() {
        return profilePath;
    }

    public String getCharacter() {
        return character;
    }

    public int getPersonId() {
        return personId;
    }

    public int getCastOrder() {
        return castOrder;
    }
}
