package com.endive.eventplanner.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.endive.eventplanner.R;
import com.endive.eventplanner.model.ApiClient;
import com.endive.eventplanner.model.ApiInterface;
import com.endive.eventplanner.pojo.BasePojo;
import com.endive.eventplanner.util.ConnectionDetector;
import com.endive.eventplanner.util.EventConstant;
import com.endive.eventplanner.util.EventDialogs;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by upasna.mishra on 12/13/2017.
 */

public class ChangePasswordActivity extends BaseActivity implements View.OnClickListener {

    private EditText old_password;
    private EditText password;
    private EditText confirm_password;
    private LinearLayout submit;
    private ConnectionDetector cd = null;
    private ChangePasswordActivity ctx = this;
    private EventDialogs dialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_password_activity);
        setHeader(getResources().getString(R.string.change_password));
        initialize();
        setListener();
    }

    private void initialize() {
        cd = new ConnectionDetector(ctx);
        old_password = (EditText) findViewById(R.id.old_password);
        password = (EditText) findViewById(R.id.password);
        confirm_password = (EditText) findViewById(R.id.confirm_password);
        submit = (LinearLayout) findViewById(R.id.submit);
    }

    private void setListener() {
        submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.submit:
                if(validate()){
                    passwordChange(getFromPrefs(EventConstant.USER_ID), old_password.getText().toString(), password.getText().toString());
                }
                break;
        }
    }

    private boolean validate(){
        dialog = new EventDialogs(ctx);
        String old_pass = old_password.getText().toString();
        String pass = password.getText().toString();
        String confirm_pass = confirm_password.getText().toString();
        if(old_pass.length() == 0){
            dialog.displayCommonDialog(getResources().getString(R.string.old_pass_msg));
            return false;
        }else if(pass.length() == 0){
            dialog.displayCommonDialog(getResources().getString(R.string.pass_msg));
            return false;
        }else if(confirm_pass.length() == 0){
            dialog.displayCommonDialog(getResources().getString(R.string.confirm_pass_msg));
            return false;
        }else if (pass.length() < 6) {
            dialog.displayCommonDialog(getResources().getString(R.string.min_pass_msg));
            return false;
        } else if (!pass.matches(EventConstant.PASSWORD_REGEX)) {
            dialog.displayCommonDialog(getResources().getString(R.string.combination_pass_msg));
            return false;
        }else if (!pass.equals(confirm_pass)) {
            dialog.displayCommonDialog(getResources().getString(R.string.confirm_pass_not_msg));
            return false;
        }else {
            return true;
        }
    }

    private void passwordChange(String user_id, String old_pass, String new_pass) {
        if (cd.isConnectingToInternet()) {
            final ProgressDialog d = EventDialogs.showLoading(ctx);
            d.setCanceledOnTouchOutside(false);

            ApiInterface apiService =
                    ApiClient.getClient().create(ApiInterface.class);
            Call<BasePojo> call = apiService.passwordChange("ChangePassword", user_id, old_pass, new_pass);
            call.enqueue(new Callback<BasePojo>() {
                @Override
                public void onResponse(Call<BasePojo> call, Response<BasePojo> response) {
                    if (response.body().getStatus_code().equals("1")) {
                        dialog.displayCommonDialog(response.body().getMessage());
                    } else {
                        dialog.displayCommonDialog(response.body().getError_message());
                    }
                    d.dismiss();
                }

                @Override
                public void onFailure(Call<BasePojo> call, Throwable t) {
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
