package com.careager.BE;

/**
 * Created by appslure on 31-12-2015.
 */
public class UserChatBE {

    private String TABLE_PEOPLE = "people_table";
    private String TABLE_FORUM="forum_table";
    int DATABASE_VERSION = 1;

    private String ID = "id";
    private String PERSON_NAME = "person_name";
    private String PERSON_CHAT_MESSAGE = "person_chat_message";
    private String PERSON_CHAT_TO_FROM = "to_or_from";
    private String PERSON_ID="device_id";
    private String CHAT_DATE="date";


    public String getTABLE_FORUM() {
        return TABLE_FORUM;
    }

    public void setTABLE_FORUM(String TABLE_FORUM) {
        this.TABLE_FORUM = TABLE_FORUM;
    }

    public String getCHAT_DATE() {
        return CHAT_DATE;
    }

    public void setCHAT_DATE(String CHAT_DATE) {
        this.CHAT_DATE = CHAT_DATE;
    }

    public String getPERSON_CHAT_TO_FROM() {
        return PERSON_CHAT_TO_FROM;
    }

    public String getPERSON_ID() {
        return PERSON_ID;
    }

    public void setPERSON_ID(String PERSON_ID) {
        this.PERSON_ID = PERSON_ID;
    }

    public void setPERSON_CHAT_TO_FROM(String pERSON_CHAT_TO_FROM) {
        PERSON_CHAT_TO_FROM = pERSON_CHAT_TO_FROM;
    }

    public String getPERSON_CHAT_MESSAGE() {
        return PERSON_CHAT_MESSAGE;
    }

    public void setPERSON_CHAT_MESSAGE(String pERSON_CHAT_MESSAGE) {
        PERSON_CHAT_MESSAGE = pERSON_CHAT_MESSAGE;
    }

    public String getTABLE_PEOPLE() {
        return TABLE_PEOPLE;
    }

    public void setTABLE_PEOPLE(String tABLE_PEOPLE) {
        TABLE_PEOPLE = tABLE_PEOPLE;
    }

    public int getDATABASE_VERSION() {
        return DATABASE_VERSION;
    }

    public void setDATABASE_VERSION(int dATABASE_VERSION) {
        DATABASE_VERSION = dATABASE_VERSION;
    }

    public String getPERSON_NAME() {
        return PERSON_NAME;
    }

    public void setPERSON_NAME(String pERSON_NAME) {
        PERSON_NAME = pERSON_NAME;
    }



    public void setID(String iD) {
        ID = iD;
    }

    public String getTableName() {
        return TABLE_PEOPLE;
    }

    public String getTableNameForum() {
        return TABLE_FORUM;
    }

    public int getDatabaseVersion() {
        return DATABASE_VERSION;
    }

    public String getID() {
        return ID;
    }

    public String getFilename() {
        return PERSON_NAME;
    }

    public String getDatabaseCreateQuery() {
        final String DATABASE_CREATE = "create table IF NOT EXISTS "
                + TABLE_PEOPLE + " (" + ID + " INTEGER PRIMARY KEY, "
                + PERSON_ID + " TEXT NOT NULL, "
                + PERSON_NAME + " TEXT NOT NULL, "
                + PERSON_CHAT_MESSAGE + " TEXT NOT NULL, "
                + PERSON_CHAT_TO_FROM + " TEXT NOT NULL ,"
                + CHAT_DATE+" TEXT NOT NULL)";

        System.out.println(DATABASE_CREATE);
        return DATABASE_CREATE;
    }


    public String getDatabaseCreateQueryForum() {
        final String DATABASE_CREATE = "create table IF NOT EXISTS "
                + TABLE_FORUM + " (" + ID + " INTEGER PRIMARY KEY, "
                + PERSON_ID + " TEXT NOT NULL, "
                + PERSON_NAME + " TEXT NOT NULL, "
                + PERSON_CHAT_MESSAGE + " TEXT NOT NULL, "
                + PERSON_CHAT_TO_FROM + " TEXT NOT NULL ,"
                + CHAT_DATE+" TEXT NOT NULL)";

        System.out.println(DATABASE_CREATE);
        return DATABASE_CREATE;
    }
}
