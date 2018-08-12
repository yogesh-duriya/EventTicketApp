package com.endive.eventplanner.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.endive.eventplanner.R;
import com.endive.eventplanner.activity.BaseActivity;
import com.endive.eventplanner.activity.ChangePasswordActivity;
import com.endive.eventplanner.activity.ContactActivity;
import com.endive.eventplanner.activity.HistoryActivity;
import com.endive.eventplanner.activity.HomeActivity;
import com.endive.eventplanner.activity.LoginActivity;
import com.endive.eventplanner.activity.ProfileActivity;
import com.endive.eventplanner.activity.WebLinkActivity;
import com.endive.eventplanner.pojo.FilterPojo;
import com.endive.eventplanner.util.ConnectionDetector;
import com.endive.eventplanner.util.EventConstant;
import com.endive.eventplanner.util.EventDialogs;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.plus.People;


public class DrawerFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, ResultCallback<People.LoadPeopleResult> {

    public ActionBarDrawerToggle mDrawerToggle;
    private int selectedPos = -1;
    private GoogleApiClient mGoogleApiClient;
    private DrawerLayout drawerlayout;
    private TextView logout_title;
    private EventDialogs dialog;
    private ConnectionDetector cd;
    private TextView my_profile_text, user_name;
    private ImageView home_menu_UserImage;
    private LinearLayout change_password_option;
    private View view_change_password;
    private LinearLayout my_profile_detail, logout, login;

    public DrawerFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.left_slide_list_layout, container, false);

        selectedPos = -1;

        dialog = new EventDialogs(getActivity());
        cd = new ConnectionDetector(getActivity());

        final LinearLayout home = (LinearLayout) v.findViewById(R.id.home_menu_option);
        final LinearLayout history = (LinearLayout) v.findViewById(R.id.history_menu_option);
        final LinearLayout faq = (LinearLayout) v.findViewById(R.id.faq_menu_option);
        final LinearLayout chat = (LinearLayout) v.findViewById(R.id.chat_menu_option);
        final LinearLayout notifications = (LinearLayout) v.findViewById(R.id.contact_menu_option);
        logout = (LinearLayout) v.findViewById(R.id.logout_menu_option);
        login = (LinearLayout) v.findViewById(R.id.login);
        final LinearLayout my_profile = (LinearLayout) v.findViewById(R.id.my_profile);
        final LinearLayout tnc_menu_option = (LinearLayout) v.findViewById(R.id.tnc_menu_option);
        my_profile_detail = (LinearLayout) v.findViewById(R.id.my_profile_detail);
        change_password_option = v.findViewById(R.id.change_password_option);
        view_change_password = v.findViewById(R.id.view_change_password);

        user_name = (TextView) v.findViewById(R.id.user_name);
        my_profile_text = (TextView) v.findViewById(R.id.my_profile_text);
        home_menu_UserImage = (ImageView) v.findViewById(R.id.home_menu_UserImage);

        ImageView home_icon = (ImageView) v.findViewById(R.id.home_icon);
        ImageView history_icon = (ImageView) v.findViewById(R.id.history_icon);
        ImageView faq_icon = (ImageView) v.findViewById(R.id.faq_icon);
        ImageView contact_icon = (ImageView) v.findViewById(R.id.contact_icon);
        ImageView chat_icon = (ImageView) v.findViewById(R.id.chat_icon);
        ImageView logout_icon = (ImageView) v.findViewById(R.id.logout_icon);
        ImageView tnc_icon = (ImageView) v.findViewById(R.id.tnc_icon);
        ImageView change_password_icon = v.findViewById(R.id.change_password_icon);

        TextView home_title = (TextView) v.findViewById(R.id.home_title);
        TextView history_title = (TextView) v.findViewById(R.id.history_title);
        TextView faq_title = (TextView) v.findViewById(R.id.faq_title);
        TextView contact_title = (TextView) v.findViewById(R.id.contact_title);
        TextView chat_title = (TextView) v.findViewById(R.id.chat_title);
        logout_title = (TextView) v.findViewById(R.id.logout_title);
        TextView tnc_title = (TextView) v.findViewById(R.id.tnc_title);
        TextView change_password_title = v.findViewById(R.id.change_password_title);

        ImageView fb = (ImageView) v.findViewById(R.id.fb);
        ImageView twitter = (ImageView) v.findViewById(R.id.twitter);
        ImageView google = (ImageView) v.findViewById(R.id.google);
        ImageView insta = (ImageView) v.findViewById(R.id.insta);
        ImageView you_tube = (ImageView) v.findViewById(R.id.you_tube);

        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWeb("https://www.facebook.com/", "Facebook");
            }
        });
        twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWeb("https://twitter.com/", "Twitter");
            }
        });
        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWeb("https://plus.google.com/", "Google +");
            }
        });
        insta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWeb("https://www.instagram.com/", "Instagram");
            }
        });
        you_tube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWeb("https://www.youtube.com/", "You Tube");
            }
        });

        setImage(home, home_title, home_icon, ResourcesCompat.getDrawable(getResources(), R.mipmap.home, null), ResourcesCompat.getDrawable(getResources(), R.mipmap.home, null), 0);
        setImage(history, history_title, history_icon, ResourcesCompat.getDrawable(getResources(), R.mipmap.history, null), ResourcesCompat.getDrawable(getResources(), R.mipmap.history, null), 1);
        setImage(change_password_option, change_password_title, change_password_icon, ResourcesCompat.getDrawable(getResources(), R.mipmap.change_password, null), ResourcesCompat.getDrawable(getResources(), R.mipmap.change_password, null), 2);
        setImage(faq, faq_title, faq_icon, ResourcesCompat.getDrawable(getResources(), R.mipmap.faq, null), ResourcesCompat.getDrawable(getResources(), R.mipmap.faq, null), 3);
        setImage(chat, chat_title, chat_icon, ResourcesCompat.getDrawable(getResources(), R.mipmap.chat, null), ResourcesCompat.getDrawable(getResources(), R.mipmap.chat, null), 4);
        setImage(notifications, contact_title, contact_icon, ResourcesCompat.getDrawable(getResources(), R.mipmap.contact, null), ResourcesCompat.getDrawable(getResources(), R.mipmap.contact, null), 5);
        setImage(tnc_menu_option, tnc_title, tnc_icon, ResourcesCompat.getDrawable(getResources(), R.mipmap.terms_icon, null), ResourcesCompat.getDrawable(getResources(), R.mipmap.terms_icon, null), 6);
        setImage(logout, logout_title, logout_icon, ResourcesCompat.getDrawable(getResources(), R.mipmap.logout, null), ResourcesCompat.getDrawable(getResources(), R.mipmap.logout, null), 7);

        if (getFromPrefs(EventConstant.USER_ID) != null && getFromPrefs(EventConstant.USER_ID).length() > 0) {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f);
            layoutParams.gravity = Gravity.NO_GRAVITY;
            logout_title.setText(getResources().getString(R.string.logout));
            user_name.setText(getFromPrefs(EventConstant.NAME));
            change_password_option.setVisibility(View.VISIBLE);
            view_change_password.setVisibility(View.VISIBLE);
            my_profile_detail.setVisibility(View.VISIBLE);
            my_profile_text.setText(getResources().getString(R.string.my_profile));
            logout.setVisibility(View.VISIBLE);
            login.setVisibility(View.GONE);
            ((BaseActivity) getActivity()).setProfileImageInLayout(getActivity(), (int) getResources().getDimension(R.dimen.profile_image), (int) getResources().getDimension(R.dimen.profile_image), getFromPrefs(EventConstant.IMAGE), home_menu_UserImage);
        } else {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f);
            layoutParams.gravity = Gravity.CENTER;
            logout_title.setText(getResources().getString(R.string.login_small));
            user_name.setText(getActivity().getResources().getString(R.string.welcome));
            my_profile_detail.setVisibility(View.GONE);
            change_password_option.setVisibility(View.GONE);
            view_change_password.setVisibility(View.GONE);
            my_profile_text.setText(getResources().getString(R.string.login));
            logout.setVisibility(View.GONE);
            login.setVisibility(View.VISIBLE);
            home_menu_UserImage.setBackground(getResources().getDrawable(R.mipmap.no_image));
        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
        my_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToProfile();
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleApiClient with access to the Google Sign-In API and the
// options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .enableAutoManage(getActivity() /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        return v;
    }

    private void openWeb(String web_url, String header) {
        drawerlayout.closeDrawers();
        Intent web_intent = new Intent(getActivity(), WebLinkActivity.class);
        web_intent.putExtra("web_url", web_url);
        web_intent.putExtra("header", header);
        startActivity(web_intent);
    }

    private void setImage(final LinearLayout layout, final TextView text, final ImageView image, final Drawable drawableSelected, final Drawable drawable, final int pos) {
        layout.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:
                        image.setImageDrawable(drawableSelected);
                        layout.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.divider_bg));
                        break;
                    case MotionEvent.ACTION_UP:
                        image.setImageDrawable(drawable);
                        selectedPos = pos;
                        layout.setBackground(null);
                        drawerlayout.closeDrawers();
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        selectedPos = -1;
                        layout.setBackground(null);
                        image.setImageDrawable(drawable);
                        break;
                }
                return true;
            }
        });
    }

    private void googlePlusLogout() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                    }
                });
    }

    @SuppressLint("NewApi")
    public void setUp(final DrawerLayout drawerlayout) {

        this.drawerlayout = drawerlayout;
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerlayout, R.string.open_drawer, R.string.close_drawer) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                ((BaseActivity) getActivity()).hideSoftKeyboard();
                if (getFromPrefs(EventConstant.USER_ID).length() > 0) {
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f);
                    layoutParams.gravity = Gravity.NO_GRAVITY;
                    logout_title.setText(getResources().getString(R.string.logout));
                    user_name.setText(getFromPrefs(EventConstant.NAME));
                    ((BaseActivity) getActivity()).setProfileImageInLayout(getActivity(), (int) getResources().getDimension(R.dimen.profile_image), (int) getResources().getDimension(R.dimen.profile_image), getFromPrefs(EventConstant.IMAGE), home_menu_UserImage);
                    my_profile_detail.setVisibility(View.VISIBLE);
                    logout.setVisibility(View.VISIBLE);
                    login.setVisibility(View.GONE);
                    my_profile_text.setText(getResources().getString(R.string.my_profile));
                } else {
                    logout_title.setText(getResources().getString(R.string.login_small));
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f);
                    layoutParams.gravity = Gravity.CENTER;
                    user_name.setText(getActivity().getResources().getString(R.string.welcome));
                    my_profile_detail.setVisibility(View.GONE);
                    logout.setVisibility(View.GONE);
                    login.setVisibility(View.VISIBLE);
                    my_profile_text.setText(getResources().getString(R.string.login));
                    home_menu_UserImage.setBackground(getResources().getDrawable(R.mipmap.no_image));
                }

                getActivity().invalidateOptionsMenu();
            }

            @SuppressLint("NewApi")
            @Override
            public void onDrawerClosed(View drawerView) {
                ActivityManager am = (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);
                ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
                super.onDrawerClosed(drawerView);
                if (selectedPos != -1) {
                    switch (selectedPos) {
                        case 0:
                            if (!cn.getShortClassName().equals(".activity.HomeActivity")) {
                                navigateToHome();
                            } else {
                                drawerlayout.closeDrawers();
                            }
                            break;

                        case 1:
                            if (getFromPrefs(EventConstant.USER_ID) != null && getFromPrefs(EventConstant.USER_ID).length() > 0) {
                                if (!cn.getShortClassName().equals(".activity.HistoryActivity")) {
                                    navigateToHistoryScreen();
                                } else {
                                    drawerlayout.closeDrawers();
                                }
                            }
                            else{
                                Intent intent = new Intent(getActivity(), LoginActivity.class);
                                intent.putExtra("from", "home");
                                startActivity(intent);
                            }
                            break;

                        case 2:
                            if (!cn.getShortClassName().equals(".activity.ChangePasswordActivity")) {
                                navigateToChangePassword();
                            } else {
                                drawerlayout.closeDrawers();
                            }
                            break;

                        case 3:
                            if (!cn.getShortClassName().equals(".activity.WebLinkActivity")) {
                                navigateToGeneralScreen(getResources().getString(R.string.faq), "faq.html");
                            } else {
                                drawerlayout.closeDrawers();
                            }
                            break;

                        case 4:
//                            VisitorInfo visitorData = new VisitorInfo.Builder()
//                                    .name("Arpit")
//                                    .build();
//                            ZopimChat.setVisitorInfo(visitorData);
//
//                            startActivity(new Intent(getActivity(), ZopimChatActivity.class));
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_SEND);
//                            intent.putExtra(Intent.EXTRA_TEXT, "Hello");
                            intent.setType("text/plain");
                            intent.setPackage("com.facebook.orca");

                            try {
                                startActivity(intent);
                            } catch (ActivityNotFoundException ex) {
                                ((BaseActivity) getActivity()).displayToast(getResources().getString(R.string.fb_messenger));
                            }
                            break;

                        case 5:
                            if (!cn.getShortClassName().equals(".activity.ContactActivity")) {
                                navigateToContactUs();
                                //navigateToSettings();
                            } else {
                                drawerlayout.closeDrawers();
                            }
                            break;

                        case 6:
                            if (!cn.getShortClassName().equals(".activity.WebLinkActivity")) {
                                navigateToGeneralScreen(getResources().getString(R.string.tnc_menu), "terms-conditions.html");
                            } else {
                                drawerlayout.closeDrawers();
                            }
                            break;

                        case 7:
                            displayCommonDialogWithCancel(getResources().getString(R.string.logout_msg));
                            break;

                        default:
                            break;
                    }
                    selectedPos = -1;
                }
            }
        };

        drawerlayout.setDrawerListener(mDrawerToggle);

    }

    private void navigateToChangePassword() {
        ActivityManager am = (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);
        ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
        drawerlayout.closeDrawers();
        if (!cn.getShortClassName().equals(".activity.ChangePasswordActivity")) {
            ((BaseActivity) getActivity()).removeActivity(getResources().getString(R.string.package_name) + ".ChangePasswordActivity");
            Intent intent = new Intent(getActivity(), ChangePasswordActivity.class);
            startActivity(intent);
        }
    }

    public String getFromPrefs(String key) {
        SharedPreferences prefs = getActivity().getSharedPreferences(EventConstant.PREF_NAME, Context.MODE_PRIVATE);
        return prefs.getString(key, EventConstant.DEFAULT_VALUE);
    }

    private void navigateToProfile() {
        ActivityManager am = (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);
        ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
        drawerlayout.closeDrawers();
        if (!cn.getShortClassName().equals(".activity.ProfileActivity")) {
            ((BaseActivity) getActivity()).removeActivity(getResources().getString(R.string.package_name) + ".ProfileActivity");
            Intent intent = new Intent(getActivity(), ProfileActivity.class);
            startActivity(intent);
        }
    }

    private void navigateToHome() {
        drawerlayout.closeDrawers();
        for (int i = 0; i < EventConstant.ACTIVITIES.size(); i++) {
            if (EventConstant.ACTIVITIES.get(i) != null)
                EventConstant.ACTIVITIES.get(i).finish();
        }
        getActivity().finish();
        Intent intent = new Intent(getActivity(), HomeActivity.class);
        startActivity(intent);
    }

    private void navigateToGeneralScreen(String header, String url) {
        String web_url = EventConstant.BASE_URL + url;
        drawerlayout.closeDrawers();
        Intent faq_intent = new Intent(getActivity(), WebLinkActivity.class);
        faq_intent.putExtra("web_url", web_url);
        faq_intent.putExtra("header", header);
        startActivity(faq_intent);
    }

    private void navigateToHistoryScreen() {
        drawerlayout.closeDrawers();
        Intent history = new Intent(getActivity(), HistoryActivity.class);
        startActivity(history);
    }

    private void navigateToNotifications() {
        drawerlayout.closeDrawers();
    }

    private void navigateToContactUs() {
        drawerlayout.closeDrawers();
        startActivity(new Intent(getActivity(), ContactActivity.class));
    }

    private void logout() {
        drawerlayout.closeDrawers();
        if (getFromPrefs(EventConstant.LOGGEDINFROM).equals("gmail"))
            googlePlusLogout();
        else if (getFromPrefs(EventConstant.LOGGEDINFROM).equals("fb")) {
            FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
            LoginManager.getInstance().logOut();
            ((BaseActivity) getActivity()).saveIntoPrefs(EventConstant.LOGGEDINFROM, "null");
        }
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
        if (!((BaseActivity) getActivity()).getFromPrefs(EventConstant.USER_ID).equals("")) {
            for (int i = 0; i < EventConstant.ACTIVITIES.size(); i++) {
                if (EventConstant.ACTIVITIES.get(i) != null)
                    EventConstant.ACTIVITIES.get(i).finish();
            }
            getActivity().finish();
            ((BaseActivity) getActivity()).saveIntoPrefs(EventConstant.USER_ID, "");
            ((BaseActivity) getActivity()).saveIntoPrefs(EventConstant.USER_ROLE, "");
            ((BaseActivity) getActivity()).saveIntoPrefs(EventConstant.NAME, "");
            ((BaseActivity) getActivity()).saveIntoPrefs(EventConstant.IMAGE, "");
            ((BaseActivity) getActivity()).saveFilterData(new FilterPojo());
        }
    }

    public void displayCommonDialogWithCancel(String msg) {
        LinearLayout btn_yes_exit_LL;
        LinearLayout btn_no_exit_LL;
        TextView btn_yes_exit;
        TextView btn_no_exit;
        final Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_Translucent_NoTitleBar);
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

        if (getActivity() != null && !getActivity().isFinishing())
            dialog.show();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
