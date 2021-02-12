package com.fyp.perkblind;

import com.fyp.perkblind.gmail.GetInboxData;
import com.fyp.perkblind.gmail.Gmail;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RestDbApi {

    @GET(Gmail.GMAIL_GET_MAILS)
    Call<GetInboxData> getGmailInbox(@Path("{userId}") String id);

    @DELETE(Gmail.GMAIL_DELETE_MAILS)
    Call<GetInboxData> deleteMail(@Path("{userId}") String id);

    @GET(Gmail.GMAIL_GET_MASSAGE_DETAIL)
    Call<GetInboxData> fetchMessage(@Path("{userId}") String msgId, @Path("{id}") String id);

    @POST(Gmail.GMAIL_SEND_MAILS)
    Call<GetInboxData> sendEmail(@Path("{userId}") String id);

    @POST(Gmail.GMAIL_SEND_MAILS_WITH_MEDIA)
    Call<GetInboxData> sendEmailWithMEdia(@Path("{userId}") String id);

}
