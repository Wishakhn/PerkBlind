package com.fyp.perkblind.gmail;

import com.google.gson.annotations.SerializedName;

public class GetInboxData {
    @SerializedName("albumId")
    private Integer albumId;
    @SerializedName("id")
    private Integer id;
    @SerializedName("title")
    private String title;

    public GetInboxData(Integer albumId, Integer id, String title) {
        this.albumId = albumId;
        this.id = id;
        this.title = title;
    }

    public Integer getAlbumId() {
        return albumId;
    }

    public void setAlbumId(Integer albumId) {
        this.albumId = albumId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
