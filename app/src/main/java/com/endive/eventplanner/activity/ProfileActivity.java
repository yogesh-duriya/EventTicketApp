package com.endive.eventplanner.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.endive.eventplanner.R;
import com.endive.eventplanner.pojo.FilterPojo;
import com.endive.eventplanner.util.EventConstant;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.plus.People;

public class ProfileActivity extends BaseActivity implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, ResultCallback<People.LoadPeopleResult> {

    private ProfileActivity ctx = this;
    private ImageView map, back, nav_invisible, nav, profile_image;
    private LinearLayout upcoming_events, past_events, fav_events, sign_out;
    private TextView user_name, login_sign_up;
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        setDrawerAndToolbar("My Profile");
        EventConstant.ACTIVITIES.add(ctx);
        initialize();
        setListeners();
    }

    private void initialize() {
        map = (ImageView) findViewById(R.id.map);
        back = (ImageView) findViewById(R.id.back);
        profile_image = (ImageView) findViewById(R.id.profile_image);
        user_name = (TextView) findViewById(R.id.user_name);
        login_sign_up = (TextView) findViewById(R.id.login_sign_up);
        nav_invisible = (ImageView) findViewById(R.id.nav_invisible);
        nav = (ImageView) findViewById(R.id.nav);
        nav.setVisibility(View.VISIBLE);
        map.setVisibility(View.GONE);
        back.setVisibility(View.GONE);
        nav_invisible.setBackground(getResources().getDrawable(R.mipmap.edit));

        upcoming_events = (LinearLayout) findViewById(R.id.upcoming_events);
        past_events = (LinearLayout) findViewById(R.id.past_events);
        fav_events = (LinearLayout) findViewById(R.id.fav_events);
        sign_out = (LinearLayout) findViewById(R.id.sign_out);

        upcoming_events.setOnClickListener(this);
        past_events.setOnClickListener(this);
        fav_events.setOnClickListener(this);
        sign_out.setOnClickListener(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(ctx)
                .enableAutoManage(ctx /* FragmentActivity */, 1, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        setData();
    }

    private void setListeners() {
        nav_invisible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ctx, EditProfileActivity.class);
                startActivity(intent);
            }
        });
        login_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToLogin();
            }
        });
    }

    private void setData() {
        if (!getFromPrefs(EventConstant.USER_ID).equals("")) {
            sign_out.setVisibility(View.VISIBLE);
            user_name.setText(getFromPrefs(EventConstant.NAME));
            login_sign_up.setVisibility(View.GONE);
            nav_invisible.setVisibility(View.VISIBLE);
            user_name.setTextColor(ContextCompat.getColor(ctx, R.color.button_color));
            if (getFromPrefs(EventConstant.IMAGE) != null && !getFromPrefs(EventConstant.IMAGE).equals(""))
                setProfileImageInLayout(ctx, (int) getResources().getDimension(R.dimen.profile_image), (int) getResources().getDimension(R.dimen.profile_image), getFromPrefs(EventConstant.IMAGE), profile_image);
        } else {
            login_sign_up.setVisibility(View.VISIBLE);
            nav_invisible.setVisibility(View.INVISIBLE);
            user_name.setText(getResources().getString(R.string.welcome));
            user_name.setTextColor(ContextCompat.getColor(ctx, R.color.hint_color));
            sign_out.setVisibility(View.GONE);
            profile_image.setBackground(getResources().getDrawable(R.mipmap.profile_bg));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (user_name != null)
            setData();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.upcoming_events:
                if (!getFromPrefs(EventConstant.USER_ID).equals(""))
                    navigateToSearchResult("1", "UpcomingEvents", getResources().getString(R.string.upcoming));
                else
                    navigateToLogin();
                break;

            case R.id.past_events:
                if (!getFromPrefs(EventConstant.USER_ID).equals(""))
                    navigateToSearchResult("0", "PastEvents", getResources().getString(R.string.past));
                else
                    navigateToLogin();
                break;

            case R.id.fav_events:
                if (!getFromPrefs(EventConstant.USER_ID).equals(""))
                    navigateToSearchResult("", "FavoriteEvents", getResources().getString(R.string.favorite));
                else
                    navigateToLogin();
                break;

            case R.id.sign_out:
                displayCommonDialogWithCancel(getResources().getString(R.string.logout_msg));
                break;

            default:
                break;
        }
    }

    private void navigateToLogin() {
        Intent intent = new Intent(ctx, LoginActivity.class);
        intent.putExtra("from", "profile");
        startActivity(intent);
    }

    private void navigateToSearchResult(String keyword, String api, String header) {
        Intent intent = new Intent(ctx, SearchResultActivity.class);
        intent.putExtra("keyword", keyword);
        intent.putExtra("api", api);
        intent.putExtra("type", "");
        intent.putExtra("header", header);
        startActivity(intent);
    }

    private void logout() {
        if (getFromPrefs(EventConstant.LOGGEDINFROM).equals("gmail"))
            googlePlusLogout();
        else if (getFromPrefs(EventConstant.LOGGEDINFROM).equals("fb")) {
            FacebookSdk.sdkInitialize(getApplicationContext());
            LoginManager.getInstance().logOut();
        }
        Intent intent = new Intent(ctx, LoginActivity.class);
        startActivity(intent);
        if (!getFromPrefs(EventConstant.USER_ID).equals("")) {
            for (int i = 0; i < EventConstant.ACTIVITIES.size(); i++) {
                if (EventConstant.ACTIVITIES.get(i) != null)
                    EventConstant.ACTIVITIES.get(i).finish();
            }
            finish();
            saveIntoPrefs(EventConstant.USER_ID, "");
            saveIntoPrefs(EventConstant.USER_ROLE, "");
            saveIntoPrefs(EventConstant.NAME, "");
            saveIntoPrefs(EventConstant.IMAGE, "");
            saveFilterData(new FilterPojo());
        }
    }

    private void googlePlusLogout() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                    }
                });
    }

    public void displayCommonDialogWithCancel(String msg) {
        LinearLayout btn_yes_exit_LL;
        LinearLayout btn_no_exit_LL;
        TextView btn_yes_exit;
        TextView btn_no_exit;
        final Dialog dialog = new Dialog(ctx, android.R.style.Theme_Translucent_NoTitleBar);
        dialog.setContentView(R.layout.custom_dialog_withheader);

        TextView msg_textView = (TextView) dialog.findViewById(R.id.text_exit);

        msg_textView.setText(msg);
        btn_yes_exit_LL = (LinearLayout) dialog.findViewById(R.id.btn_yes_exit_LL);
        btn_no_exit_LL = (LinearLayout) dialog.findViewById(R.id.btn_no_exit_LL);
        btn_yes_exit = (TextView) dialog.findViewById(R.id.btn_yes_exit);
        btn_no_exit = (TextView) dialog.findViewById(R.id.btn_no_exit);
        btn_no_exit_LL.setVisibility(View.VISIBLE);

        btn_yes_exit.setText(getResources().getString(R.string.yes));
        btn_no_exit.setText(getResources().getString(R.string.no));

        ImageView dialog_header_cross = (ImageView) dialog.findViewById(R.id.dialog_header_cross);
        dialog_header_cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btn_yes_exit_LL.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
                logout();
            }
        });
        btn_no_exit_LL.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        if (ctx != null && !ctx.isFinishing())
            dialog.show();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onResult(@NonNull People.LoadPeopleResult loadPeopleResult) {

    }
}
