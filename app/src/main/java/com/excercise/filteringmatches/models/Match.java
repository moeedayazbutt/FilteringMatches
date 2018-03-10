package com.excercise.filteringmatches.models;

/**
 * Created by khurr on 3/4/2018.
 */

public class Match {
    private String displayName;
    private Integer age;
    private String jobTitle;
    private Integer heightInCm;
    private String mainPhoto;
    private Double compatibilityScore;
    private Integer contactsExchanged;
    private boolean favourite;
    private String religion;
    private City city;
    private int distanceInKm;

    public Match(String displayName, Integer age, String jobTitle, Integer heightInCm, String mainPhoto, Double compatibilityScore, Integer contactsExchanged, boolean favourite, String religion, City city) {
        this.displayName = displayName;
        this.age = age;
        this.jobTitle = jobTitle;
        this.heightInCm = heightInCm;
        this.mainPhoto = mainPhoto;
        this.compatibilityScore = compatibilityScore;
        this.contactsExchanged = contactsExchanged;
        this.favourite = favourite;
        this.religion = religion;
        this.city = city;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public Integer getHeightInCm() {
        return heightInCm;
    }

    public void setHeightInCm(Integer heightInCm) {
        this.heightInCm = heightInCm;
    }

    public String getMainPhoto() {
        return mainPhoto;
    }

    public void setMainPhoto(String mainPhoto) {
        this.mainPhoto = mainPhoto;
    }

    public Double getCompatibilityScore() {
        return compatibilityScore;
    }

    public void setCompatibilityScore(Double compatibilityScore) {
        this.compatibilityScore = compatibilityScore;
    }

    public Integer getContactsExchanged() {
        return contactsExchanged;
    }

    public void setContactsExchanged(Integer contactsExchanged) {
        this.contactsExchanged = contactsExchanged;
    }

    public boolean isFavourite() {
        return favourite;
    }

    public void setFavourite(boolean favourite) {
        this.favourite = favourite;
    }

    public String getReligion() {
        return religion;
    }

    public void setReligion(String religion) {
        this.religion = religion;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public int getDistanceInKm() {
        return distanceInKm;
    }

    public void setDistanceInKm(int distanceInKm) {
        this.distanceInKm = distanceInKm;
    }
}
