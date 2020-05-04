package com.example.top10downloader;

public class FeedEntry {
    private String name;
    private String artist;
    private String releaseDate;
    private String Summary;
    private String ImageURL;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getSummary() {
        return Summary;
    }

    public void setSummary(String summary) {
        Summary = summary;
    }

    public String getImageURL() {
        return ImageURL;
    }

    public void setImageURL(String imageURL) {
        ImageURL = imageURL;
    }

//    @Override
//    public String toString() {
//        return "name =" + name + '\n' +
//                ", artist =" + artist + '\n' +
//                ", releaseDate =" + releaseDate + '\n' +
//                ", ImageURL =" + ImageURL + '\n' ;
//
//    }
}
