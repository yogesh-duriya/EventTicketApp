package com.endive.eventplanner.model;

import com.endive.eventplanner.pojo.BasePojo;
import com.endive.eventplanner.pojo.CategoriesPojo;
import com.endive.eventplanner.pojo.ContactPojo;
import com.endive.eventplanner.pojo.CountriesPojo;
import com.endive.eventplanner.pojo.CouponPojo;
import com.endive.eventplanner.pojo.EventDetailPojo;
import com.endive.eventplanner.pojo.HistoryPojo;
import com.endive.eventplanner.pojo.LocationPojo;
import com.endive.eventplanner.pojo.LoginPojo;
import com.endive.eventplanner.pojo.OrganizerPojo;
import com.endive.eventplanner.pojo.PackageModel;
import com.endive.eventplanner.pojo.PaymentNonceDataPojo;
import com.endive.eventplanner.pojo.SeatsPojo;
import com.endive.eventplanner.pojo.SomethingInterestingPojo;
import com.endive.eventplanner.pojo.TicketPojo;
import com.endive.eventplanner.pojo.VenuePojo;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;


/**
 * Created by arpit.jain on 10/6/2017.
 */

public interface ApiInterface {

    @FormUrlEncoded
    @POST(".")
    Call<LoginPojo> postRegister(@Field("method") String method, @Field("first_name") String first_name, @Field("last_name") String last_name, @Field("email") String email, @Field("gender") String gender, @Field("dob") String dob, @Field("phone") String phone, @Field("address") String address, @Field("city") String city, @Field("state") String state, @Field("country") String country, @Field("zipcode") String zipcode, @Field("password") String password, @Field("device_token") String device_token, @Field("device_type") String device_type, @Field("social_id") String social_id, @Field("social_type") String social_type, @Field("user_role") String user_role);

    @FormUrlEncoded
    @POST(".")
    Call<CountriesPojo> getCountries(@Field("method") String method);

    @FormUrlEncoded
    @POST(".")
    Call<LoginPojo> postLogin(@Field("method") String method, @Field("email") String email, @Field("password") String password, @Field("device_token") String device_token, @Field("device_type") String device_type, @Field("user_role") String user_role);

    @FormUrlEncoded
    @POST(".")
    Call<LoginPojo> forgotPassword(@Field("method") String method, @Field("email") String email, @Field("user_role") String user_role);

    @FormUrlEncoded
    @POST(".")
    Call<SomethingInterestingPojo> getInterestingEvents(@Field("method") String method, @Field("user_id") String user_id, @Field("category") String category_id, @Field("type") String type, @Field("price_min") String price_min, @Field("price_max") String price_max, @Field("sort") String sort, @Field("date") String date, @Field("date2") String date2, @Field("keyword") String keyword, @Field("location") String location, @Field("attend_events") String attend_events);

    @FormUrlEncoded
    @POST(".")
    Call<SomethingInterestingPojo> getRecommendedEvents(@Field("method") String method, @Field("user_id") String user_id, @Field("event_tags") String event_tags, @Field("latitude") String latitude, @Field("longitude") String longitude, @Field("location") String location, @Field("start_date") String start_date, @Field("end_date") String end_date, @Field("event_id") String event_id);

    @FormUrlEncoded
    @POST(".")
    Call<CategoriesPojo> getCategories(@Field("method") String method);

    @FormUrlEncoded
    @POST(".")
    Call<BasePojo> getClientToken(@Field("method") String method);

    @FormUrlEncoded
    @POST(".")
    Call<PaymentNonceDataPojo> sendPaymentNonce(@Field("method") String method, @Field("nonce") String nonce, @Field("amount") String amount, @Field("event_id") String event_id, @Field("user_id") String user_id, @Field("package_id") String package_id, @Field("event_group_ticket_id") String event_group_ticket_id, @Field("coupon_code") String coupon_code, @Field("discount") String discount, @Field("venue_id") String venue_id, @Field("paid_price") String paid_price, @Field("no_of_tickets") String no_of_tickets, @Field("booked_seats") String booked_seats, @Field("booking_type") String booking_type, @Field("is_like_or_share") String is_like_or_share, @Field("package_name") String package_name, @Field("organizer_name") String organizer_name, @Field("ticket_name") String ticket_name, @Field("coupon_discount_amount") String coupon_discount_amount, @Field("share_discount_amount") String share_discount_amount, @Field("user_detail") JSONObject user_detail);

    @FormUrlEncoded
    @POST("get-selected-seats")
    Call<SeatsPojo> getSeats(@Field("device_id") String device_id);

    @FormUrlEncoded
    @POST(".")
    Call<LocationPojo> getLocations(@Field("method") String method);

    @FormUrlEncoded
    @POST(".")
    Call<BasePojo> setFavorite(@Field("method") String method, @Field("user_id") String user_id, @Field("event_id") String event_id);

    @FormUrlEncoded
    @POST(".")
    Call<EventDetailPojo> eventDetails(@Field("method") String method, @Field("user_id") String user_id, @Field("event_id") String event_id);

    @FormUrlEncoded
    @POST(".")
    Call<LoginPojo> getProfile(@Field("method") String method, @Field("user_id") String user_id);

    @FormUrlEncoded
    @POST(".")
    Call<VenuePojo> getVenues(@Field("method") String method, @Field("user_id") String user_id, @Field("event_id") String event_id);

    @FormUrlEncoded
    @POST(".")
    Call<PackageModel> getPackages(@Field("method") String method, @Field("user_id") String user_id, @Field("event_id") String event_id);

    @FormUrlEncoded
    @POST(".")
    Call<TicketPojo> getTickets(@Field("method") String method, @Field("user_id") String user_id, @Field("event_id") String event_id);

    @FormUrlEncoded
    @POST(".")
    Call<LoginPojo> checkIsRegistered(@Field("method") String method, @Field("social_id") String social_id, @Field("social_type") String social_type, @Field("device_token") String device_token, @Field("device_type") String device_type, @Field("user_role") String user_role);

    @FormUrlEncoded
    @POST(".")
    Call<OrganizerPojo> getOrganizerData(@Field("method") String method, @Field("organiser_id") String organizer_id, @Field("user_id") String user_id, @Field("type") String type);

    @FormUrlEncoded
    @POST(".")
    Call<CouponPojo> getCouponData(@Field("method") String method, @Field("event_id") String event_id);

    @FormUrlEncoded
    @POST(".")
    Call<ContactPojo> getContactDetails(@Field("method") String method);

    @FormUrlEncoded
    @POST(".")
    Call<HistoryPojo> getHistory(@Field("method") String method, @Field("user_id") String user_id);

    @FormUrlEncoded
    @POST(".")
    Call<BasePojo> passwordChange(@Field("method") String method, @Field("user_id") String user_id, @Field("old_pass") String old_pass, @Field("new_pass") String new_pass);

}