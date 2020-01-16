package com.mandywebdesign.impromptu.Interfaces;

import com.mandywebdesign.impromptu.Models.ChangePassword;
import com.mandywebdesign.impromptu.Models.CheckInGuest;
import com.mandywebdesign.impromptu.Models.GetAdminEmail;
import com.mandywebdesign.impromptu.Models.RetroPostcode;
import com.mandywebdesign.impromptu.Models.SendContactUSmesg;
import com.mandywebdesign.impromptu.Models.TotalCheckIn;
import com.mandywebdesign.impromptu.Retrofit.BookFreeEvents;
import com.mandywebdesign.impromptu.Retrofit.Booked_User;
import com.mandywebdesign.impromptu.Retrofit.EventMessageClick;
import com.mandywebdesign.impromptu.Retrofit.FAQ;
import com.mandywebdesign.impromptu.Retrofit.FollowUnfollow;
import com.mandywebdesign.impromptu.Retrofit.FollowerPublish;
import com.mandywebdesign.impromptu.Retrofit.GusetCheckIns;
import com.mandywebdesign.impromptu.Retrofit.HostedEvents;
import com.mandywebdesign.impromptu.Retrofit.InappropriateBehaviour;
import com.mandywebdesign.impromptu.Retrofit.NormalEventPrice;
import com.mandywebdesign.impromptu.Retrofit.NormalFilterEvents;
import com.mandywebdesign.impromptu.Retrofit.NormalGetEvent;
import com.mandywebdesign.impromptu.Retrofit.NormalGetFavEvents;
import com.mandywebdesign.impromptu.Retrofit.NormalGetProfile;
import com.mandywebdesign.impromptu.Retrofit.NormalPayment;
import com.mandywebdesign.impromptu.Retrofit.NormalPublishProfile;
import com.mandywebdesign.impromptu.Retrofit.NormalRetroFav;
import com.mandywebdesign.impromptu.Retrofit.NormalRetrodeleteFav;
import com.mandywebdesign.impromptu.Retrofit.NormalRetrologin;
import com.mandywebdesign.impromptu.Retrofit.Normal_past_booked;
import com.mandywebdesign.impromptu.Retrofit.NormalrelatedEvents;
import com.mandywebdesign.impromptu.Retrofit.NormalretroHosting_fav_evnts;
import com.mandywebdesign.impromptu.Retrofit.PostCode;
import com.mandywebdesign.impromptu.Retrofit.Rating;
import com.mandywebdesign.impromptu.Retrofit.RefundAPI;
import com.mandywebdesign.impromptu.Retrofit.RemainingTickets;
import com.mandywebdesign.impromptu.Retrofit.RetroAddEvent;
import com.mandywebdesign.impromptu.Retrofit.RetroAllChats;
import com.mandywebdesign.impromptu.Retrofit.RetroChat;
import com.mandywebdesign.impromptu.Retrofit.RetroContactUs;
import com.mandywebdesign.impromptu.Retrofit.RetroDelete;
import com.mandywebdesign.impromptu.Retrofit.RetroDeleteQUEs;
import com.mandywebdesign.impromptu.Retrofit.RetroDraftsEvents;
import com.mandywebdesign.impromptu.Retrofit.RetroEventCategory;
import com.mandywebdesign.impromptu.Retrofit.RetroFAQ;
import com.mandywebdesign.impromptu.Retrofit.RetroGetEventData;
import com.mandywebdesign.impromptu.Retrofit.RetroGetMessages;
import com.mandywebdesign.impromptu.Retrofit.RetroGetProfile;
import com.mandywebdesign.impromptu.Retrofit.RetroHelp;
import com.mandywebdesign.impromptu.Retrofit.RetroHistoryEvents;
import com.mandywebdesign.impromptu.Retrofit.RetroLiveEvents;
import com.mandywebdesign.impromptu.Retrofit.RetroLoginPojo;
import com.mandywebdesign.impromptu.Retrofit.RetroLogout;
import com.mandywebdesign.impromptu.Retrofit.RetroPostDraft;
import com.mandywebdesign.impromptu.Retrofit.RetroPrivancy;
import com.mandywebdesign.impromptu.Retrofit.RetroRegisterPojo;
import com.mandywebdesign.impromptu.Retrofit.RetroTerms;
import com.mandywebdesign.impromptu.Retrofit.RetroUploadProfilePojo;
import com.mandywebdesign.impromptu.Retrofit.RetroUsernameiMage;
import com.mandywebdesign.impromptu.Retrofit.SavedCardsResponse;
import com.mandywebdesign.impromptu.Retrofit.SearchMessages;
import com.mandywebdesign.impromptu.Retrofit.TotalTickets;
import com.mandywebdesign.impromptu.Retrofit.UpdateDraft;
import com.mandywebdesign.impromptu.Retrofit.UsersBookedPastEvent;
import com.mandywebdesign.impromptu.Retrofit.UsersLiveEvent;
import com.mandywebdesign.impromptu.Retrofit.UsersPastEvent;

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HEAD;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface RegisterApiInterface {

    @FormUrlEncoded
    @POST("register")
    Call<RetroRegisterPojo> RegisterUser(
            @Field("name") String name,
            @Field("email") String email,
            @Field("password") String password,
            @Field("charity_number") String charity_number,
            @Field("type") String type
    );


    @FormUrlEncoded
    @POST("login")
    Call<RetroLoginPojo> LoginUser(
            @Field("email") String email,
            @Field("password") String password,
            @Field("device_token") String device_token
    );

    @Multipart
    @POST("profile")
    Call<RetroUploadProfilePojo> PublishProfile(
            @Header("Authorization") String token,
            @Query("id") String id,
            @Query("name") String name,
            @Query("address1") String address1,
            @Query("address2") String address2,
            @Query("postcode") String postcode,
            @Query("city") String city,
            @Query("aboutOrganisation") String aboutOrganisation,
            @Query("webUrl") String webUrl,
            @Query("facebookUrl") String facebookUrl,
            @Query("instagramUrl") String instagramUrl,
            @Query("twitterUrl") String twitterUrl,
            @Part MultipartBody.Part avatar
    );


    @POST("category")
    Call<RetroEventCategory> Category(
            @Header("Authorization") String token
    );


    @Multipart
    @POST("events")
    Call<RetroAddEvent> addEvent(
            @Header("Authorization") String token,
            @Query("cat_id") String cat_id,
            @Query("title") String title,
            @Query("description") String description,
            @Query("category") String category,
            @Part ArrayList<MultipartBody.Part> images,
            @Query("addressline1") String addressline1,
            @Query("addressline2") String addressline2,
            @Query("postcode") String postcode,
            @Query("city") String city,
            @Query("date") String date,
            @Query("time_from") String time_from,
            @Query("time_to") String time_to,
            @Query("attendees_gender") String attendees_gender,
            @Query("attendees_no") String attendees_no,
            @Query("free_event") String free_event,
            @Query("ticket_type") String ticket_type,
            @Query("price") String price,
            @Query("no_of_tickets") String no_of_tickets,
            @Query("b_event_hostname") String b_event_hostname,
            @Query("status") String type,
            @Query("link1") String link1,
            @Query("link2") String link2,
            @Query("link3") String link3,
            @Query("start_datetime") String start_datetime,
            @Query("end_datetime") String end_datetime,
            @Query("type") String tickettype,
            @Query("value") String value,
            @Query("numberoftickets") String numberoftickets
    );

    @Multipart
    @POST("events")
    Call<RetroAddEvent> addEventNew(
            @Header("Authorization") String token,
            @Part HashMap<String, String> params,
            @Part ArrayList<MultipartBody.Part> images
    );


    //edit draft and post
    @Multipart
    @POST("update-edit-draft-event")
    Call<UpdateDraft> updateDraft(
            @Header("Authorization") String token,
            @Query("id") String id,
            @Query("cat_id") String cat_id,
            @Query("title") String title,
            @Query("description") String description,
            @Query("category") String category,
            @Part ArrayList<MultipartBody.Part> images,
            @Query("addressline1") String addressline1,
            @Query("addressline2") String addressline2,
            @Query("postcode") String postcode,
            @Query("city") String city,
            @Query("date") String date,
            @Query("time_from") String time_from,
            @Query("time_to") String time_to,
            @Query("attendees_gender") String attendees_gender,
            @Query("attendees_no") String attendees_no,
            @Query("free_event") String free_event,
            @Query("ticket_type") String ticket_type,
            @Query("price") String price,
            @Query("no_of_tickets") String no_of_tickets,
            @Query("b_event_hostname") String b_event_hostname,
            @Query("status") String type,
            @Query("start_datetime") String start_datetime,
            @Query("end_datetime") String end_datetime,
            @Query("type") String tickettype,
            @Query("value") String value,
            @Query("numberoftickets") String numberoftickets

    );


    @POST("postal-code")
    Call<RetroPostcode> checkpostcode(
            @Query("postcode") String postcode
    );


    @GET("view-profile")
    Call<RetroGetProfile> getProfile(
            @Header("Authorization") String token,
            @Header("Accept") String accept,
            @Query("user_id") String user_id

    );

    @POST("publish")
    Call<RetroLiveEvents> liveEvents(
            @Header("Authorization") String token,
            @Header("Accept") String accept
    );

    @POST("draft")
    Call<RetroDraftsEvents> drafts(
            @Header("Authorization") String token
            , @Header("Accept") String accept
    );

    @POST("history")
    Call<RetroHistoryEvents> history(
            @Header("Authorization") String token
            , @Header("Accept") String accept

    );

    @GET("user-history-events")
    Call<UsersPastEvent> userspastevents(
            @Query("user_id") String user_id
    );

    @GET("user-past-booked-events")
    Call<UsersBookedPastEvent> userbookedPast_event(
            @Query("user_id") String user_id
    );

    @GET("user-publish")
    Call<UsersLiveEvent> userliveevents(
            @Query("user_id") String user_id
    );

    @POST("help")
    Call<RetroHelp> help(
            @Header("Authorization") String token,
            @Header("Accept") String accept
    );

    @POST("terms")
    Call<RetroTerms> terms(
            @Header("Authorization") String token,
            @Header("Accept") String accept
    );

    @POST("privacy")
    Call<RetroPrivancy> privacy(
            @Header("Authorization") String token,
            @Header("Accept") String accept
    );

    @POST("faq")
    Call<RetroFAQ> faq(
            @Header("Authorization") String token,
            @Header("Accept") String accept
    );

    @POST("contact")
    Call<RetroContactUs> contact(
            @Header("Authorization") String token,
            @Header("Accept") String accept
    );

    @GET("view-event")
    Call<RetroGetEventData> getEvents(
            @Header("Authorization") String token,
            @Header("Accept") String accept,
            @Query("id") String event_id
    );

    @POST("logout")
    Call<RetroLogout> logout(
            @Header("Authorization") String token,
            @Header("Accept") String accept
    );

    @POST("delete")
    Call<RetroDelete> delete(
            @Header("Authorization") String token,
            @Header("Accept") String accept
    );

    @GET("hosted-events")
    Call<HostedEvents> hostedEvents(
            @Header("Authorization") String token,
            @Query("user_id") String user_id
    );

    //===============normal users=====================

    @POST("social-login")
    Call<NormalRetrologin> normalLogin(
            @Query("type") String type,
            @Query("social_id") String token,
            @Query("email") String email,
            @Query("name") String name,
            @Query("provider_id") String provider_id,
            @Query("device_token") String device_token
    );

    @GET("all-events")
    Call<NormalGetEvent> normalGetEvent(
            @Header("Authorization") String token,
            @Query("lat") String lat,
            @Query("lng") String lng
    );

    @GET("update-draft-event")
    Call<RetroPostDraft> postDraft(
            @Header("Authorization") String token,
            @Header("Accept") String accept,
            @Query("id") String id
    );

    @POST("add-favourite-events")
    Call<NormalRetroFav> fav(
            @Header("Authorization") String token,
            @Header("Accept") String accept,
            @Query("event_id") String id
    );

    @POST("delete-favourite-events")
    Call<NormalRetrodeleteFav> deletefav(
            @Header("Authorization") String token,
            @Header("Accept") String accept,
            @Query("event_id") String id
    );

    @GET("favourite-events")
    Call<NormalGetFavEvents> getFavEvents(
            @Header("Authorization") String token,
            @Header("Accept") String abc

    );

    @GET("hosting-events")
    Call<NormalretroHosting_fav_evnts> getHostFav(
            @Header("Authorization") String token,
            @Header("Accept") String accept
    );

    @POST("followersPublish")
    Call<FollowerPublish> followersPublish(
            @Header("Authorization") String token

    );


    @GET("related-events")
    Call<NormalrelatedEvents> getrelatedEvents(
            @Header("Authorization") String token,
            @Query("cat_id") String cat_id,
            @Query("lat") String lat,
            @Query("lng") String lng
    );

    @Multipart
    @POST("profile")
    Call<NormalPublishProfile> normalPublishProfile(
            @Header("Authorization") String token,
            @Query("id") String id,
            @Query("gender") String gender,
            @Query("name") String name,
            @Query("age") String age,
            @Query("aboutOrganisation") String about,
            @Query("question[]") ArrayList<String> questions,
            @Query("answer[]") ArrayList<String> answer,
            @Query("question_id[]") ArrayList<String> question_id,
            @Part MultipartBody.Part avatar
    );

    //get max price of event
    @GET("event-price")
    Call<NormalEventPrice> eventPrice(

    );

    @GET("user-details")
    Call<RetroUsernameiMage> userNameImage(
            @Header("Authorization") String token
    );


    @POST("filter-events")
    Call<NormalFilterEvents> filterEvnt(
            @Header("Authorization") String token,
            @Header("Accept") String accept,
            @Query("lat") String lat,
            @Query("lng") String lng,
            @Query("gender") String gender,
            @Query("date") String date,
            @Query("edate") String edate,
            @Query("price") String price
    );

    @GET("view-profile")
    Call<NormalGetProfile> normalGetPRofile(
            @Header("Authorization") String token,
            @Header("Accept") String accept,
            @Query("user_id") String user_id

    );

    @FormUrlEncoded
    @POST("book-events")
    Call<NormalPayment> normalPayment(
            @Header("Authorization") String token,
            @Field("event_id") String event_id,
            @Field("amount") String amount,
            @Field("token") String stripe_token,
            @Field("total_tickets") String total_tickets,
            @Field("ticket_type") String tickettype,
            @Field("card_number") String card_number,
            @Field("card_sdate") String card_sdate,
            @Field("card_edate") String  card_edate,
            @Field("card_holder_name") String  card_holder_name,
            @Field("card_save") boolean card_save
    );

    @GET("past-events")
    Call<Normal_past_booked> past_booked(
            @Header("Authorization") String token,
            @Header("Accept") String accept
    );

    @GET("upcoming-events")
    Call<Normal_past_booked> upcoming_booked(
            @Header("Authorization") String token,
            @Header("Accept") String accept
    );


    @GET("see-events")
    Call<Booked_User> booked_users(
            @Header("Authorization") String token,
            @Query("eventid") String event_id
    );

    @GET("total-booked-tickets")
    Call<TotalTickets> totalEvents(
            @Header("Authorization") String token,
            @Query("eventid") String event_id
    );

    //click on bookevent message button
    @POST("event-message")
    Call<EventMessageClick> eventMEsgClick(
            @Header("Authorization") String token,
            @Query("eventid") String event_id
    );

    @POST("chat")
    Call<RetroChat> chat(
            @Header("Authorization") String token,
            @Query("event_id") String event_id,
            @Query("title") String title,
            @Query("message") String message,
            @Query("sender_id") String event_host_user
    );

    @GET("event-chat-list")
    Call<RetroAllChats> allChats(
            @Header("Authorization") String token,
            @Header("Accept") String accept
    );

    @POST("delete-ques")
    Call<RetroDeleteQUEs> deleteQues(
            @Header("Authorization") String token,
            @Query("ques_id") String event_id
    );

    @GET("message-list")
    Call<RetroGetMessages> getMessages(
            @Header("Authorization") String token,
            @Query("event_id") String event_id,
            @Query("seen_status") String seen_status
    );

    @POST("rating")
    Call<Rating> rating(
            @Header("Authorization") String token,
            @Query("event_id") String eventID,
            @Query("rating") String rating,
            @Query("feedback") String feedback
    );

    @GET("remain-tickets")
    Call<RemainingTickets> remainingTickets(
            @Header("Authorization") String token,
            @Query("eventid") String eventID
    );

    @POST("book-free-events")
    Call<BookFreeEvents> freebookevent(
            @Header("Authorization") String token,
            @Query("event_id") String eventID,
            @Query("total_tickets") String total_tickets
    );

    @POST("search")
    Call<SearchMessages> search(
            @Header("Authorization") String token,
            @Query("event_title") String tittel
    );

    @POST("guest-check-in")
    Call<CheckInGuest> checkinguest(
            @Header("Authorization") String token,
            @Query("eventid") String evenID,
            @Query("checkin") String checkin,
            @Query("userid") String userId
    );

  /*  @GET("total-check-in")
    Call<TotalCheckIn> totleTickets(
            @Header("Authorization") String token
    );*/

    @GET("total-check-in")
    Call<TotalCheckIn> totleTickets(
            @Header("Authorization") String token,
            @Query("eventid") String id);

    @POST("guest-check-in")
    Call<GusetCheckIns> guestCheckIns(
            @Header("Authorization") String token,
            @Query("eventid") String id,
            @Query("checkin") String checkIn,
            @Query("userid") String userid
    );

    @GET("contact-email")
    Call<GetAdminEmail> getAdminemail(
            @Header("Authorization") String token
    );

    @POST("contact")
    Call<SendContactUSmesg> sendcontactmesg(
            @Header("Authorization") String token,
            @Query("message") String message
    );

    @POST("faq")
    Call<FAQ> faq(
            @Header("Authorization") String token
    );

    @POST("changepassword")
    Call<ChangePassword> changepassword(
            @Header("Authorization") String token,
            @Query("old_password") String old_password,
            @Query("password") String password,
            @Query("confirm_password") String confirm_password
    );

    @POST("follow")
    Call<FollowUnfollow> followunfollow(
            @Header("Authorization") String token,
            @Query("follow_user_id") String follow_user_id
    );

    @POST("inappropriate_behaviour")
    Call<InappropriateBehaviour> inappropriate_behaviour(
            @Header("Authorization") String token,
            @Query("event_id") String event_id,
            @Query("user_type") String user_type,
            @Query("description") String description
    );

    @POST("payment-refund")
    Call<RefundAPI> refundapi(
            @Header("Authorization") String token,
            @Query("event_id") String event_id
    );


    @GET("savedcard")
    Call<SavedCardsResponse> savedCard(
            @Header("Authorization") String token
    );
}
