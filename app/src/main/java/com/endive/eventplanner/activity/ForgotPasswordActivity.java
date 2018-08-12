package com.endive.eventplanner.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.endive.eventplanner.R;
import com.endive.eventplanner.model.ApiClient;
import com.endive.eventplanner.model.ApiInterface;
import com.endive.eventplanner.pojo.LoginPojo;
import com.endive.eventplanner.util.ConnectionDetector;
import com.endive.eventplanner.util.EventConstant;
import com.endive.eventplanner.util.EventDialogs;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordActivity extends BaseActivity {

    private ForgotPasswordActivity ctx = this;
    private LinearLayout submit;
    private EventDialogs dialog = null;
    private ConnectionDetector cd = null;
    private EditText email_forgot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        setHeader(getResources().getString(R.string.header_forgot_pass));

        initialize();
        setListener();
    }

    private void initialize() {
        ImageView nav = (ImageView) findViewById(R.id.nav);
        nav.setVisibility(View.GONE);
        submit = (LinearLayout) findViewById(R.id.submit);
        email_forgot = (EditText) findViewById(R.id.email_forgot);
        cd = new ConnectionDetector(ctx);
    }

    private void setListener() {
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateForgotPassword();
            }
        });
    }

    private void validateForgotPassword(){
        String email_val = email_forgot.getText().toString().trim();
        dialog = new EventDialogs(ctx);
        if (email_val.equals("")) {
            dialog.displayCommonDialog(getResources().getString(R.string.email_id_msg));
        } else if (!email_val.equals("") && !isValidEmail(email_val)) {
            dialog.displayCommonDialog(getResources().getString(R.string.email_vaild_id_msg));
        } else {
            doLogin(email_val);
            hideSoftKeyboard();
        }
    }

    private void doLogin(String email_val) {
        if (cd.isConnectingToInternet()) {
            final ProgressDialog d = EventDialogs.showLoading(ctx);
            d.setCanceledOnTouchOutside(false);

            ApiInterface apiService =
                    ApiClient.getClient().create(ApiInterface.class);
            Call<LoginPojo> call = apiService.forgotPassword("ForgotPassword", email_val, EventConstant.USER_ROLE_VAL);
            call.enqueue(new Callback<LoginPojo>() {
                @Override
                public void onResponse(Call<LoginPojo> call, Response<LoginPojo> response) {
                    if (response.body().getStatus_code().equals("1")) {
                        displayToastLong(response.body().getMessage());
                        finish();
                    } else {
                        dialog.displayCommonDialog(response.body().getMessage());
                    }
                    d.dismiss();
                }

                @Override
                public void onFailure(Call<LoginPojo> call, Throwable t) {
                    // Log error here since request failed
                    System.out.println("retrofit hh failure " + t.getMessage());
                    d.dismiss();
                }
            });
        } else {
            dialog.displayCommonDialog(getResources().getString(R.string.no_internet_connection));
        }
    }
}
