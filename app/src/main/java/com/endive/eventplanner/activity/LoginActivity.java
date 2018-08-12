package com.endive.eventplanner.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.endive.eventplanner.R;
import com.endive.eventplanner.model.ApiClient;
import com.endive.eventplanner.model.ApiInterface;
import com.endive.eventplanner.pojo.LoginPojo;
import com.endive.eventplanner.util.ConnectionDetector;
import com.endive.eventplanner.util.EventConstant;
import com.endive.eventplanner.util.EventDialogs;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends BaseActivity implements GoogleApiClient.OnConnectionFailedListener {

    private EditText et_email, et_password;
    private TextView signUp, forgotPassword;
    private LinearLayout skip, login;
    private ImageView fb_login, google_login;
    private LoginActivity ctx = this;
    private EventDialogs dialog = null;
    private ConnectionDetector cd = null;
    private GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 006;
    private CallbackManager callbackManager;
    private String mFacebookAccesstoken;
    private String from = "";
    private LoginButton loginButton;
    private String responsefacebook, fb_id, name, fname = "", lname = "", email, email_id, profile_image, password, birthday;
    private Bundle mBundle;
    private GifImageView gif_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initialize();
        setListeners();
        printKeyHash(LoginActivity.this);
    }

    private void initialize() {

        et_email = (EditText) findViewById(R.id.et_email);
        et_password = (EditText) findViewById(R.id.et_password);
        signUp = (TextView) findViewById(R.id.tv_sign_up);
        forgotPassword = (TextView) findViewById(R.id.tv_forgot);
        skip = (LinearLayout) findViewById(R.id.skip);
        login = (LinearLayout) findViewById(R.id.login);
        fb_login = (ImageView) findViewById(R.id.fb_login);
        google_login = (ImageView) findViewById(R.id.google_login);
        loginButton = (LoginButton) findViewById(R.id.fb_login_button);
        cd = new ConnectionDetector(ctx);
        dialog = new EventDialogs(ctx);

        from = getIntent().getStringExtra("from");
        callbackManager = CallbackManager.Factory.create();

        gif_image = (GifImageView) findViewById(R.id.gif_image);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestServerAuthCode("81880132578-ge8n6c5pk6nlcqbco715v1996ia80s4r.apps.googleusercontent.com")
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    private void setListeners() {
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ctx, SignUpActivity.class);
                intent.putExtra("from", from);
                startActivityForResult(intent, EventConstant.START_SIGN_UP);
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateLoginForm();
            }
        });
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToHome();
            }
        });
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ctx, ForgotPasswordActivity.class);
                intent.putExtra("from", from);
                startActivity(intent);
            }
        });
        google_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInGoogle();
            }
        });
        fb_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginButton.performClick();
                facebokData();
            }
        });
    }

    private void facebokData() {
        loginButton.setReadPermissions(Arrays.asList("public_profile, email, user_birthday , user_friends"));
        callbackManager = CallbackManager.Factory.create();
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult loginResult) {

                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                try {
                                    responsefacebook = response.toString();
                                    if (object.has("email")) {
                                        email_id = object.getString("email");
                                    } else {
                                        email_id = "";
                                    }
                                    name = object.getString("name");
                                    fb_id = String.valueOf(object.getLong("id")); //use this for logout
                                    profile_image = "https://graph.facebook.com/" + object.getString("id").toString() + "/picture?type=large&width=720";
                                    String[] arr = name.split(" ");
                                    fname = arr[0];
                                    lname = arr[1];
                                    birthday = object.getString("birthday");

                                    mBundle = new Bundle();
                                    mBundle.putString("fname", fname);
                                    mBundle.putString("lname", lname);
                                    mBundle.putString("email_id", email_id);
                                    mBundle.putString("fb_id", fb_id);
                                    mBundle.putString("profile_image", profile_image);
                                    //first = true;

                                } catch (Exception e) {
                                }

                                if (fb_id != null) {
                                    saveIntoPrefs(EventConstant.LOGGEDINFROM, "fb");
                                    checkIsRegistered(fb_id, "1", fname, lname, email_id, birthday);
                                }
                            }

                        });


                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender, birthday");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                //toastShow(getResources().getString(R.string.login_canceled_facebooklogin));
            }

            @Override
            public void onError(FacebookException error) {
                //toastShow(getResources().getString(R.string.login_failed_facebooklogin));
            }
        });
    }

    private void signInGoogle() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void validateLoginForm() {
        String email_val = et_email.getText().toString().trim();
        String password_val = et_password.getText().toString();

        if (email_val.equals("")) {
            dialog.displayCommonDialog(getResources().getString(R.string.email_id_msg));
        } else if (!email_val.equals("") && !isValidEmail(email_val)) {
            dialog.displayCommonDialog(getResources().getString(R.string.email_vaild_id_msg));
        } else if (password_val.equals("")) {
            dialog.displayCommonDialog(getResources().getString(R.string.pass_msg));
        } else {
            doLogin(email_val, password_val);
            hideSoftKeyboard();
        }
    }

    private void doLogin(String email_val, String password_val) {
        if (cd.isConnectingToInternet()) {
            final ProgressDialog d = EventDialogs.showLoading(ctx);
            d.setCanceledOnTouchOutside(false);

            ApiInterface apiService =
                    ApiClient.getClient().create(ApiInterface.class);

            Call<LoginPojo> call = apiService.postLogin("Login", email_val, password_val, getFromPrefs(EventConstant.DEVICE_ID), "2", EventConstant.USER_ROLE_VAL);
            call.enqueue(new Callback<LoginPojo>() {
                @Override
                public void onResponse(Call<LoginPojo> call, Response<LoginPojo> response) {
                    if (response.body().getStatus_code().equals("1")) {
                        saveIntoPrefs(EventConstant.USER_ID, response.body().getData().getId());
                        saveIntoPrefs(EventConstant.USER_ROLE, response.body().getData().getUser_role());
                        saveIntoPrefs(EventConstant.IMAGE, response.body().getData().getProfile_pic());
                        saveIntoPrefs(EventConstant.NAME, response.body().getData().getFirst_name() + " " + response.body().getData().getLast_name());
                        saveIntoPrefs(EventConstant.LOGGEDINFROM, "email");
                        navigateToHome();
                    } else {
                        dialog.displayCommonDialog(response.body().getError_message());
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == EventConstant.START_SIGN_UP) {
                from = data.getStringExtra("from");
//                navigateToHome();
            } else if (requestCode == RC_SIGN_IN) {
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                handleSignInResult(result);
            } else {

                callbackManager.onActivityResult(requestCode, resultCode, data);
                try {
                    if (AccessToken.getCurrentAccessToken() == null) {
                        //Toast.makeText(Login.this, "Something went wrong", Toast.LENGTH_LONG).show();
                    } else {
                        AccessToken accesstoken = AccessToken.getCurrentAccessToken();
                        mFacebookAccesstoken = accesstoken.getToken();
                        Log.e("AccessToken", "" + mFacebookAccesstoken);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void navigateToHome() {
        if (from != null && !from.equals("")) {
            if (!getFromPrefs(EventConstant.USER_ID).equals(""))
                saveBooleanIntoPrefs(EventConstant.REFRESH_REQUIRED, true);
            finish();
        } else {
            removeActivity(getResources().getString(R.string.package_name) + ".HomeActivity");
            Intent intent = new Intent(ctx, HomeActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
//        mGoogleApiClient.stopAutoManage(ctx);
//        mGoogleApiClient.disconnect();

    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();

            saveIntoPrefs(EventConstant.LOGGEDINFROM, "gmail");
            name = acct.getDisplayName();
            if (name != null && name.contains("+\\s")) {
                String[] arr = name.trim().split("\\s");
                fname = arr[0];
                lname = arr[1];
            } else {
                fname = acct.getGivenName();
                lname = acct.getFamilyName();
            }
            checkIsRegistered(acct.getId(), "2", fname, lname, acct.getEmail(), "");
        }
    }

    private void checkIsRegistered(final String id, final String social_type, final String first_name, final String last_name, final String email, final String birthday) {
        if (cd.isConnectingToInternet()) {
            final ProgressDialog d = EventDialogs.showLoading(ctx);
            d.setCanceledOnTouchOutside(false);

            ApiInterface apiService =
                    ApiClient.getClient().create(ApiInterface.class);

            Call<LoginPojo> call = apiService.checkIsRegistered("SocialLogin", id, social_type, getFromPrefs(EventConstant.DEVICE_ID), "2", EventConstant.USER_ROLE_VAL);
            call.enqueue(new Callback<LoginPojo>() {
                @Override
                public void onResponse(Call<LoginPojo> call, Response<LoginPojo> response) {
                    if (response.body().getStatus_code().equals("1")) {
                        saveIntoPrefs(EventConstant.USER_ID, response.body().getData().getId());
                        saveIntoPrefs(EventConstant.USER_ROLE, response.body().getData().getUser_role());
                        saveIntoPrefs(EventConstant.IMAGE, response.body().getData().getProfile_pic());
                        saveIntoPrefs(EventConstant.NAME, response.body().getData().getFirst_name() + " " + response.body().getData().getLast_name());

                        navigateToHome();
                    } else if (response.body().getStatus_code().equals("2")) {
                        dialog.displayCommonDialog(response.body().getError_message());
                        if (getFromPrefs(EventConstant.LOGGEDINFROM).equals("gmail"))
                            googlePlusLogout();
                        else if (getFromPrefs(EventConstant.LOGGEDINFROM).equals("fb")) {
                            FacebookSdk.sdkInitialize(getApplicationContext());
                            LoginManager.getInstance().logOut();
                            saveIntoPrefs(EventConstant.LOGGEDINFROM, "null");
                        }
                    } else if (response.body().getStatus_code().equals("3")) {
                        dialog.displayCommonDialog(response.body().getError_message());
                    } else {
                        Intent intent = new Intent(ctx, SignUpActivity.class);
                        System.out.println("First name is   "+first_name);
                        System.out.println("Last name is   "+last_name);
                        if (first_name == null || first_name == "null" || first_name == "")
                            intent.putExtra("first_name", "");
                        else
                            intent.putExtra("first_name", first_name);
                        if (last_name == null || last_name == "null" || last_name == "")
                            intent.putExtra("last_name", "");
                        else
                            intent.putExtra("last_name", last_name);
                        intent.putExtra("email", email);
                        intent.putExtra("social_type", social_type);
                        intent.putExtra("birthday", birthday);
                        intent.putExtra("id", id);
                        intent.putExtra("from", from);
                        startActivityForResult(intent, EventConstant.START_SIGN_UP);
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

    private void googlePlusLogout() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                    }
                });
    }

    private static String printKeyHash(Activity context) {
        PackageInfo packageInfo;
        String key = null;
        try {
            //getting application package name, as defined in manifest
            String packageName = context.getApplicationContext().getPackageName();

            //Retriving package info
            packageInfo = context.getPackageManager().getPackageInfo(packageName, PackageManager.GET_SIGNATURES);

            for (Signature signature : packageInfo.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                key = new String(Base64.encode(md.digest(), 0));
                System.out.println("Hash Key value is   " + key);
                // String key = new String(Base64.encodeBytes(md.digest()));

            }
        } catch (PackageManager.NameNotFoundException e1) {
            //Log.e("Name not found", e1.toString());
            System.out.println("Name not found.");
        } catch (NoSuchAlgorithmException e) {
            System.out.println("No such an algorithm");
            //Log.e("No such an algorithm", e.toString());
        } catch (Exception e) {
            System.out.println("Exception");
            //Log.e("Exception", e.toString());
        }

        return key;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
