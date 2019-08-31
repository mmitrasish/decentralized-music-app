package com.mitrasish.projectmusic.models;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class SongCredits {
    private String SongName;
    private String ComposedBy;
    private String LyricsBy;
    private String MusicBy;
    private String DirectedBy;
    private String ProducedBy;
    private String SongSource;
    private String SongKey;
    private String SongOwner;

    public SongCredits() {
    }

    public SongCredits(String songName, String composedBy, String lyricsBy, String musicBy, String directedBy, String producedBy, String songSource, String songOwner) {
        SongName = songName;
        ComposedBy = composedBy;
        LyricsBy = lyricsBy;
        MusicBy = musicBy;
        DirectedBy = directedBy;
        ProducedBy = producedBy;
        SongSource = songSource;
        SongOwner = songOwner;
    }

    public String getSongOwner() {
        return SongOwner;
    }

    public void setSongOwner(String songOwner) {
        SongOwner = songOwner;
    }

    public String getSongKey() {
        return SongKey;
    }

    public void setSongKey(String songKey) {
        SongKey = songKey;
    }

    public String getSongName() {
        return SongName;
    }

    public void setSongName(String songName) {
        SongName = songName;
    }

    public String getComposedBy() {
        return ComposedBy;
    }

    public void setComposedBy(String composedBy) {
        ComposedBy = composedBy;
    }

    public String getLyricsBy() {
        return LyricsBy;
    }

    public void setLyricsBy(String lyricsBy) {
        LyricsBy = lyricsBy;
    }

    public String getMusicBy() {
        return MusicBy;
    }

    public void setMusicBy(String musicBy) {
        MusicBy = musicBy;
    }

    public String getDirectedBy() {
        return DirectedBy;
    }

    public void setDirectedBy(String directedBy) {
        DirectedBy = directedBy;
    }

    public String getProducedBy() {
        return ProducedBy;
    }

    public void setProducedBy(String producedBy) {
        ProducedBy = producedBy;
    }

    public String getSongSource() {
        return SongSource;
    }

    public void setSongSource(String songSource) {
        SongSource = songSource;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("SongName", SongName);
        result.put("ComposedBy", ComposedBy);
        result.put("LyricsBy", LyricsBy);
        result.put("MusicBy", MusicBy);
        result.put("DirectedBy", DirectedBy);
        result.put("ProducedBy", ProducedBy);
        result.put("SongSource", SongSource);
        result.put("SongOwner", SongOwner);

        return result;
    }
}
