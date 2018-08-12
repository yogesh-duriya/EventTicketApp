package com.endive.eventplanner.pojo;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by arpit.jain on 10/7/2017.
 */

public class SomethingInterestingDataPojo implements Serializable {

    private String id;
    private String title;
    private String organizer_id;
    private String event_category_id;
    private String event_subcategory_id;
    private String event_type_id;
    private String seating_map_id;
    private String start_time;
    private String end_time;
    private String image;
    private String description;
    private String terms_condition;
    private String tag_ids;
    private String sales_close_time;
    private String is_social_media_discount;
    private String like_discount_type;
    private String like_discount;
    private String share_discount_type;
    private String share_discount;
    private String like_share_discount_type;
    private String like_share_discount;
    private String coupon_code;
    private String coupon_code_price;
    private String social_facebook_link;
    private String is_recurring_event;
    private String social_twitter_link;
    private String social_google_link;
    private String social_linkedin_link;
    private String social_instagram_link;
    private String social_youtube_link;
    private String is_merchant_sale;
    private String event_sell_type;
    private String status;
    private String created_at;
    private String venue;
    private boolean is_liked, is_favourite, is_in_cart;
    private boolean is_share;
    private String price;
    private String latitude;
    private String longitude;
    private ArrayList<String> tags;
    private String venue_id;
    private String like_count;
    private String organizer_name;
    private String event_url;
    private boolean is_seating_arrangement;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOrganizer_id() {
        return organizer_id;
    }

    public void setOrganizer_id(String organizer_id) {
        this.organizer_id = organizer_id;
    }

    public String getEvent_category_id() {
        return event_category_id;
    }

    public void setEvent_category_id(String event_category_id) {
        this.event_category_id = event_category_id;
    }

    public String getEvent_subcategory_id() {
        return event_subcategory_id;
    }

    public void setEvent_subcategory_id(String event_subcategory_id) {
        this.event_subcategory_id = event_subcategory_id;
    }

    public String getEvent_type_id() {
        return event_type_id;
    }

    public void setEvent_type_id(String event_type_id) {
        this.event_type_id = event_type_id;
    }

    public String getSeating_map_id() {
        return seating_map_id;
    }

    public void setSeating_map_id(String seating_map_id) {
        this.seating_map_id = seating_map_id;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTerms_condition() {
        return terms_condition;
    }

    public void setTerms_condition(String terms_condition) {
        this.terms_condition = terms_condition;
    }

    public String getTag_ids() {
        return tag_ids;
    }

    public void setTag_ids(String tag_ids) {
        this.tag_ids = tag_ids;
    }

    public String getSales_close_time() {
        return sales_close_time;
    }

    public void setSales_close_time(String sales_close_time) {
        this.sales_close_time = sales_close_time;
    }

    public String getIs_social_media_discount() {
        return is_social_media_discount;
    }

    public void setIs_social_media_discount(String is_social_media_discount) {
        this.is_social_media_discount = is_social_media_discount;
    }

    public String getLike_discount_type() {
        return like_discount_type;
    }

    public void setLike_discount_type(String like_discount_type) {
        this.like_discount_type = like_discount_type;
    }

    public String getLike_discount() {
        return like_discount;
    }

    public void setLike_discount(String like_discount) {
        this.like_discount = like_discount;
    }

    public String getShare_discount_type() {
        return share_discount_type;
    }

    public void setShare_discount_type(String share_discount_type) {
        this.share_discount_type = share_discount_type;
    }

    public String getShare_discount() {
        return share_discount;
    }

    public void setShare_discount(String share_discount) {
        this.share_discount = share_discount;
    }

    public String getLike_share_discount_type() {
        return like_share_discount_type;
    }

    public void setLike_share_discount_type(String like_share_discount_type) {
        this.like_share_discount_type = like_share_discount_type;
    }

    public String getLike_share_discount() {
        return like_share_discount;
    }

    public void setLike_share_discount(String like_share_discount) {
        this.like_share_discount = like_share_discount;
    }

    public String getCoupon_code() {
        return coupon_code;
    }

    public void setCoupon_code(String coupon_code) {
        this.coupon_code = coupon_code;
    }

    public String getCoupon_code_price() {
        return coupon_code_price;
    }

    public void setCoupon_code_price(String coupon_code_price) {
        this.coupon_code_price = coupon_code_price;
    }

    public String getSocial_facebook_link() {
        return social_facebook_link;
    }

    public void setSocial_facebook_link(String social_facebook_link) {
        this.social_facebook_link = social_facebook_link;
    }

    public String getIs_recurring_event() {
        return is_recurring_event;
    }

    public void setIs_recurring_event(String is_recurring_event) {
        this.is_recurring_event = is_recurring_event;
    }

    public String getSocial_twitter_link() {
        return social_twitter_link;
    }

    public void setSocial_twitter_link(String social_twitter_link) {
        this.social_twitter_link = social_twitter_link;
    }

    public String getSocial_google_link() {
        return social_google_link;
    }

    public void setSocial_google_link(String social_google_link) {
        this.social_google_link = social_google_link;
    }

    public String getSocial_linkedin_link() {
        return social_linkedin_link;
    }

    public void setSocial_linkedin_link(String social_linkedin_link) {
        this.social_linkedin_link = social_linkedin_link;
    }

    public String getSocial_instagram_link() {
        return social_instagram_link;
    }

    public void setSocial_instagram_link(String social_instagram_link) {
        this.social_instagram_link = social_instagram_link;
    }

    public String getSocial_youtube_link() {
        return social_youtube_link;
    }

    public void setSocial_youtube_link(String social_youtube_link) {
        this.social_youtube_link = social_youtube_link;
    }

    public String getIs_merchant_sale() {
        return is_merchant_sale;
    }

    public void setIs_merchant_sale(String is_merchant_sale) {
        this.is_merchant_sale = is_merchant_sale;
    }

    public String getEvent_sell_type() {
        return event_sell_type;
    }

    public void setEvent_sell_type(String event_sell_type) {
        this.event_sell_type = event_sell_type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public boolean isIs_liked() {
        return is_liked;
    }

    public void setIs_liked(boolean is_liked) {
        this.is_liked = is_liked;
    }

    public boolean isIs_favourite() {
        return is_favourite;
    }

    public void setIs_favourite(boolean isIs_favourite) {
        this.is_favourite = isIs_favourite;
    }

    public boolean isIs_in_cart() {
        return is_in_cart;
    }

    public void setIs_in_cart(boolean is_in_cart) {
        this.is_in_cart = is_in_cart;
    }

    public boolean isIs_seating_arrangement() {
        return is_seating_arrangement;
    }

    public void setIs_seating_arrangement(boolean is_seating_arrangement) {
        this.is_seating_arrangement = is_seating_arrangement;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }

    public String getVenue_id() {
        return venue_id;
    }

    public void setVenue_id(String venue_id) {
        this.venue_id = venue_id;
    }

    public String getLike_count() {
        return like_count;
    }

    public void setLike_count(String like_count) {
        this.like_count = like_count;
    }

    public String getOrganizer_name() {
        return organizer_name;
    }

    public void setOrganizer_name(String organizer_name) {
        this.organizer_name = organizer_name;
    }

    public String getEvent_url() {
        return event_url;
    }

    public void setEvent_url(String event_url) {
        this.event_url = event_url;
    }

    public boolean isIs_share() {
        return is_share;
    }

    public void setIs_share(boolean is_share) {
        this.is_share = is_share;
    }
}
