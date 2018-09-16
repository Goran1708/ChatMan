package com.crossover.crosschat.android.core.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Mahmoud Abdurrahman (mahmoud.abdurrahman@crossover.com) on 2/8/18.
 */
public class MessageItem extends BaseObject implements Parcelable {

    private static final long serialVersionUID = 2401554129441836178L;

    @JsonIgnore
    private String rawMessage;

    @JsonProperty
    private List<String> mentions;

    @JsonProperty
    private List<String> emoticons;

    @JsonProperty
    private List<String> links;

    @JsonProperty
    private List<String> hashtags;

    @JsonIgnore
    private List<ContentEntity> allEntities;

    public MessageItem() {

    }

    public String getRawMessage() {
        return rawMessage;
    }

    public void setRawMessage(String rawMessage) {
        this.rawMessage = rawMessage;
    }

    public List<String> getMentions() {
        return mentions;
    }

    public void setMentions(List<String> mentions) {
        if (mentions == null || mentions.isEmpty()) return;

        this.mentions = mentions;
    }

    public List<String> getEmoticons() {
        return emoticons;
    }

    public void setEmoticons(List<String> emoticons) {
        if (emoticons == null || emoticons.isEmpty()) return;

        this.emoticons = emoticons;
    }

    public List<String> getLinks() {
        return links;
    }

    public void setLinks(List<String> links) {
        if (links == null || links.isEmpty()) return;

        this.links = links;
    }

    public void setHashtags(List<String> hashtags) {
        if (hashtags == null || hashtags.isEmpty()) return;

        this.hashtags = hashtags;
    }

    public List<String> getHashtags() {
        return hashtags;
    }

    public List<ContentEntity> getAllEntities() {
        if (allEntities == null) {
            allEntities = Collections.emptyList();
        }
        return allEntities;
    }

    public void setAllEntities(List<ContentEntity> allEntities) {
        this.allEntities = allEntities;
    }

    protected MessageItem(Parcel in) {
        rawMessage = in.readString();
        mentions = in.createStringArrayList();
        emoticons = in.createStringArrayList();
        links = in.createStringArrayList();
        hashtags = in.createStringArrayList();
        allEntities = new ArrayList<>();
        in.readList(allEntities, ContentEntity.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(rawMessage);
        dest.writeStringList(mentions);
        dest.writeStringList(emoticons);
        dest.writeStringList(links);
        dest.writeStringList(hashtags);
        dest.writeList(allEntities);
    }

    public static final Creator<MessageItem> CREATOR = new Creator<MessageItem>() {
        @Override
        public MessageItem createFromParcel(Parcel source) {
            return new MessageItem(source);
        }

        @Override
        public MessageItem[] newArray(int size) {
            return new MessageItem[size];
        }
    };
}
