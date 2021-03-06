package com.ok.lab.magiclantern.data;

import java.util.List;

/**
 * Created by olgakuklina on 2016-12-04.
 */

public class PersonalProfileDatas {

    private String actorName;
    private String profilePath;
    private String placeOfBirth;
    private String biography;
    private int personId;
    private int popularity;

    public PersonalProfileDatas(String actorName, String profilePath, String placeOfBirth,  String biography, int personId, int popularity) {
        this.actorName = actorName;
        this.profilePath = profilePath;
        this.biography = biography;
        this.personId = personId;
        this.placeOfBirth = placeOfBirth;
        this.popularity = popularity;

    }
    public String getActorName() {
        return actorName;
    }

    public String getProfileImagePath() {
        return profilePath;
    }

    public String getBiography() {
        return biography;
    }

    public int getPersonId()
    {
        return personId;
    }
    public String getPlaceOfBirth()
    {
        return placeOfBirth;
    }

    public int getPpopularity()
    {
        return popularity;
    }
}
