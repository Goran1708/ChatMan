package com.crossover.crosschat.android.core.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by Mahmoud Abdurrahman (mahmoud.abdurrahman@crossover.com) on 2/10/18.
 */
public class ChatMessageItem extends BaseObject implements Parcelable {

    private static final long serialVersionUID = 1556747903054882913L;

    private MessageItem messageItem;

    private String rawMessage;

    private Date date;

    private boolean self;

    private boolean formatted;

    private boolean html;

    public ChatMessageItem() {
    }

    public ChatMessageItem(MessageItem messageItem, String rawMessage, Date date, boolean self,
                           boolean formatted, boolean html) {
        this.messageItem = messageItem;
        this.rawMessage = rawMessage;
        this.date = date;
        this.self = self;
        this.formatted = formatted;
        this.html = html;
    }

    public MessageItem getMessageItem() {
        return messageItem;
    }

    public void setMessageItem(MessageItem messageItem) {
        this.messageItem = messageItem;
    }

    public String getRawMessage() {
        return rawMessage;
    }

    public void setRawMessage(String rawMessage) {
        this.rawMessage = rawMessage;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isSelf() {
        return self;
    }

    public void setSelf(boolean self) {
        this.self = self;
    }

    public boolean isFormatted() {
        return formatted;
    }

    public void setFormatted(boolean formatted) {
        this.formatted = formatted;
    }

    public boolean isHtml() {
        return formatted && html;
    }

    public void setHtml(boolean html) {
        this.formatted = true;
        this.html = html;
    }

    protected ChatMessageItem(Parcel in) {
        messageItem = in.readParcelable(MessageItem.class.getClassLoader());
        rawMessage = in.readString();
        long tmpDate = in.readLong();
        date = tmpDate != -1 ? new Date(tmpDate) : null;
        self = in.readByte() != 0x00;
        formatted = in.readByte() != 0x00;
        html = in.readByte() != 0x00;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(messageItem, 0);
        dest.writeString(rawMessage);
        dest.writeLong(date != null ? date.getTime() : -1L);
        dest.writeByte((byte) (self ? 0x01 : 0x00));
        dest.writeByte((byte) (formatted ? 0x01 : 0x00));
        dest.writeByte((byte) (html ? 0x01 : 0x00));
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ChatMessageItem> CREATOR
            = new Parcelable.Creator<ChatMessageItem>() {
        @Override
        public ChatMessageItem createFromParcel(Parcel in) {
            return new ChatMessageItem(in);
        }

        @Override
        public ChatMessageItem[] newArray(int size) {
            return new ChatMessageItem[size];
        }
    };
}
