package com.example.esther.journalapp.data;

public class Entry {

    private String mContent;
    private String mDate;
    private String mTitle;
    private String mEntryId;


    public Entry(){
        // Default constructor required for calls to DataSnapshot.getValue(Entry.class)
    }

    public Entry(String title , String content, String date){
        this.mContent = content;
        this.mDate = date;
        this.mTitle = title;
    }

    public Entry(String title , String content, String date, String id){
        this.mContent = content;
        this.mDate = date;
        this.mTitle = title;
        this.mEntryId = id;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String mContent) {
        this.mContent = mContent;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String mDate) {
        this.mDate = mDate;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getEntryId() {
        return mEntryId;
    }

    public void setmId(String id) {
        this.mEntryId = id;
    }
}
