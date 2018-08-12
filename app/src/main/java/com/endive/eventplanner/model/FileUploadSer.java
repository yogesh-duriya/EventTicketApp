package com.endive.eventplanner.model;


import com.endive.eventplanner.pojo.LoginPojo;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by yesh on 10/14/2015.
 */
public interface FileUploadSer {

    public interface FileUploadService {
        @Multipart
        @POST(".")
        Call<LoginPojo> updateProfile(@Part("method") RequestBody method, @Part("user_id") RequestBody user_id, @Part("first_name") RequestBody first_name, @Part("last_name") RequestBody last_name, @Part("gender") RequestBody gender, @Part("dob") RequestBody dob, @Part("mobile_no") RequestBody phone, @Part("address") RequestBody address, @Part("city") RequestBody city, @Part("state") RequestBody state, @Part("country_id") RequestBody country, @Part("postcode") RequestBody zipcode, @Part("user_role") RequestBody user_role, @Part MultipartBody.Part uploadedFile);

        @Multipart
        @POST(".")
        Call<LoginPojo> updateProfileWithoutImage(@Part("method") RequestBody method, @Part("user_id") RequestBody user_id, @Part("first_name") RequestBody first_name, @Part("last_name") RequestBody last_name, @Part("gender") RequestBody gender, @Part("dob") RequestBody dob, @Part("mobile_no") RequestBody phone, @Part("address") RequestBody address, @Part("city") RequestBody city, @Part("state") RequestBody state, @Part("country_id") RequestBody country, @Part("postcode") RequestBody zipcode, @Part("user_role") RequestBody user_role);

    }

}
