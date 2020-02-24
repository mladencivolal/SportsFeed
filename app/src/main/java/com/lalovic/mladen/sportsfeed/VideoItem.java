package com.lalovic.mladen.sportsfeed;

class VideoItem {
    private String mUrl;
    private String mAuthor;
    private String mViews;
    private String mPoster;
    private String mFlag;
    private String mSportUrl;
    private String mDescription;
    private String mSport;
    private String mCountry;

    VideoItem(String poster, String url, String author, String views, String flag, String sportUrl, String description, String sport, String country) {
        this.mPoster = poster;
        this.mUrl = url;
        this.mAuthor = author;
        this.mViews = views;
        this.mFlag = flag;
        this.mSportUrl = sportUrl;
        this.mDescription = description;
        this.mSport = sport;
        this.mCountry = country;
    }

    String getPoster() {
        return mPoster;
    }

    String getUrl() {
        return mUrl;
    }

    String getAuthor() {
        return mAuthor;
    }

    String getViews() {
        return mViews;
    }

    String getFlag() {
        return mFlag;
    }

    String getSportUrl() {
        return mSportUrl;
    }

    String getDescription() {
        return mDescription;
    }

    String getSport() {
        return mSport;
    }

    String getCountry() {
        return mCountry;
    }
}
