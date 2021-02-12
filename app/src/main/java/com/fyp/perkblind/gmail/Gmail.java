package com.fyp.perkblind.gmail;

public class Gmail {
    public static final String GMAIL_BASE_URL ="https://gmail.googleapis.com";
    public static final String GMAIL_GET_MAILS ="gmail/v1/users/{userId}/messages";
    public static final String GMAIL_GET_MASSAGE_DETAIL ="gmail/v1/users/{userId}/messages/{id}";
    public static final String GMAIL_SEND_MAILS_WITH_MEDIA ="upload/gmail/v1/users/{userId}/messages/send";
    public static final String GMAIL_SEND_MAILS ="gmail/v1/users/{userId}/messages/send";
    public static final String GMAIL_DELETE_MAILS ="gmail/v1/users/{userId}/messages/{id}";
    public static final String GMAIL_CREATE_DRAFT ="gmail/v1/users/{userId}/drafts";
    public static final String GMAIL_CREATE_DRAFT_WITH_MEDIA ="upload/gmail/v1/users/{userId}/drafts";
}
