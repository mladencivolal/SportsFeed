package com.lalovic.mladen.sportsfeed;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class VideoData {
    private List<VideoItem> mVideoItemItemList;

    VideoData(List<VideoItem> mList) {
        this.mVideoItemItemList = mList;
    }

    List<VideoItem> getAllVideos() {
        return mVideoItemItemList;
    }

    public void setvList(List<VideoItem> mList) {
        this.mVideoItemItemList = mList;
    }

    List<VideoItem> getCountryFilteredVideos(List<String> genre, List<VideoItem> mList) {
        List<VideoItem> tempList = new ArrayList<>();
        for (VideoItem videoItem : mList) {
            for (String g : genre) {
                if (videoItem.getCountry().equalsIgnoreCase(g)) {
                    tempList.add(videoItem);
                }
            }

        }
        return tempList;
    }

    List<VideoItem> getSportFilteredVideos(List<String> yearstr, List<VideoItem> mList) {
        List<VideoItem> tempList = new ArrayList<>();
        for (VideoItem videoItem : mList) {
            for (String y : yearstr) {
                if (videoItem.getSport().equalsIgnoreCase(y)) {
                    tempList.add(videoItem);
                }
            }
        }
        return tempList;
    }

    List<VideoItem> getAuthorFilteredMovies(List<String> quality, List<VideoItem> mList) {
        List<VideoItem> tempList = new ArrayList<>();
        for (VideoItem videoItem : mList) {
            for (String q : quality) {
                if (videoItem.getAuthor().equalsIgnoreCase(q)) {
                    tempList.add(videoItem);
                }
            }
        }
        return tempList;
    }

    List<String> getUniqueCountryKeys() {
        List<String> countries = new ArrayList<>();
        for (VideoItem videoItem : mVideoItemItemList) {
            if (!countries.contains(videoItem.getCountry())) {
                countries.add(videoItem.getCountry());
            }
        }
        Collections.sort(countries);
        return countries;
    }

    List<String> getUniqueSportKeys() {
        List<String> sports = new ArrayList<>();
        for (VideoItem videoItem : mVideoItemItemList) {
            if (!sports.contains(videoItem.getSport() + "")) {
                sports.add(videoItem.getSport() + "");
            }
        }
        Collections.sort(sports);
        return sports;
    }

    List<String> getUniqueAuthorKeys() {
        List<String> authors = new ArrayList<>();
        for (VideoItem movie : mVideoItemItemList) {
            if (!authors.contains(movie.getAuthor())) {
                authors.add(movie.getAuthor());
            }
        }
        Collections.sort(authors);
        return authors;
    }
}
