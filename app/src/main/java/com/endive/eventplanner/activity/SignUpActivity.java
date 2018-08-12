package com.endive.eventplanner.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.endive.eventplanner.R;
import com.endive.eventplanner.adapter.NothingSelectedSpinnerAdapter;
import com.endive.eventplanner.model.ApiClient;
import com.endive.eventplanner.model.ApiInterface;
import com.endive.eventplanner.pojo.CountriesDataPojo;
import com.endive.eventplanner.pojo.CountriesPojo;
import com.endive.eventplanner.pojo.LoginPojo;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends BaseActivity implements GoogleApiClient.OnConnectionFailedListener {

    private EditText first_name, last_name, email, address, city, state, zip_code, dob, password, confirm_password, mobile;
    private LinearLayout male, female, other, sign_up;
    private ImageView radio_male, radio_female, radio_other;
    private TextView header_skip;
    private String gender = "1";
    private SignUpActivity ctx = this;
    private EventDialogs dialog = null;
    private ConnectionDetector cd = null;
    private ArrayList<CountriesDataPojo> countriesArr;
    private Spinner country;
    private LayoutInflater mInflator;
    private String countryId;
    private Calendar calendar;
    private int year;
    private int month;
    private int day;
    private String social_id = "", social_type = "";
    private String from = "";
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        setHeader(getResources().getString(R.string.sign_up));

        initialize();
        setListener();
    }

    private void initialize() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestServerAuthCode("81880132578-ge8n6c5pk6nlcqbco715v1996ia80s4r.apps.googleusercontent.com")
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(ctx, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        first_name = (EditText) findViewById(R.id.first_name);
        last_name = (EditText) findViewById(R.id.last_name);
        email = (EditText) findViewById(R.id.email);
        address = (EditText) findViewById(R.id.address);
        city = (EditText) findViewById(R.id.city);
        state = (EditText) findViewById(R.id.state);
        country = (Spinner) findViewById(R.id.country);
        zip_code = (EditText) findViewById(R.id.zip_code);
        dob = (EditText) findViewById(R.id.dob);
        password = (EditText) findViewById(R.id.password);
        confirm_password = (EditText) findViewById(R.id.confirm_password);
        mobile = (EditText) findViewById(R.id.mobile);
        male = (LinearLayout) findViewById(R.id.male);
        female = (LinearLayout) findViewById(R.id.female);
        other = (LinearLayout) findViewById(R.id.other);
        sign_up = (LinearLayout) findViewById(R.id.sign_up);
        radio_male = (ImageView) findViewById(R.id.radio_male);
        radio_female = (ImageView) findViewById(R.id.radio_female);
        radio_other = (ImageView) findViewById(R.id.radio_other);
//        country.setOnItemSelectedListener(typeSelectedListener);
        ImageView nav = (ImageView) findViewById(R.id.nav);
        ImageView nav_invisible = (ImageView) findViewById(R.id.nav_invisible);
        nav.setVisibility(View.GONE);
        nav_invisible.setVisibility(View.GONE);
        header_skip = (TextView) findViewById(R.id.header_skip);
        header_skip.setVisibility(View.VISIBLE);

        calendar = Calendar.getInstance();

        year = calendar.get(Calendar.YEAR);

        month = calendar.get(Calendar.MONTH) + 1;
        day = calendar.get(Calendar.DAY_OF_MONTH);

        from = getIntent().getStringExtra("from");
        social_id = getIntent().getStringExtra("id");
        social_type = getIntent().getStringExtra("social_type");
        email.setText(getIntent().getStringExtra("email"));
        if (getIntent().getStringExtra("first_name") != null && !getIntent().getStringExtra("first_name").equals(""))
            first_name.setText(getIntent().getStringExtra("first_name"));
        if (getIntent().getStringExtra("last_name") != null && !getIntent().getStringExtra("last_name").equals(""))
            last_name.setText(getIntent().getStringExtra("last_name"));

        if (getIntent().getStringExtra("birthday") != null && !getIntent().getStringExtra("birthday").equals("")) {
            String date[] = getIntent().getStringExtra("birthday").split("/");
            year = Integer.parseInt(date[2]);
            month = Integer.parseInt(date[0]) - 1;
            day = Integer.parseInt(date[1]);
            String date_val = year + "/" + (month + 1) + "/" + day;
            dob.setText(date_val);
        }
        if (social_id != null && !social_id.equals("") && !getIntent().getStringExtra("email").equals(""))
            email.setFocusable(false);

        mInflator = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        cd = new ConnectionDetector(ctx);
        getCountries();
    }

    private void setListener() {

        male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gender = "1";
                radio_male.setImageResource(R.mipmap.circle_colour);
                radio_female.setImageResource(R.mipmap.circle);
                radio_other.setImageResource(R.mipmap.circle);
            }
        });

        female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gender = "2";
                radio_male.setImageResource(R.mipmap.circle);
                radio_female.setImageResource(R.mipmap.circle_colour);
                radio_other.setImageResource(R.mipmap.circle);
            }
        });

        other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gender = "3";
                radio_male.setImageResource(R.mipmap.circle);
                radio_female.setImageResource(R.mipmap.circle);
                radio_other.setImageResource(R.mipmap.circle_colour);
            }
        });

        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSoftKeyboard();
                showDatePicker();
            }
        });

        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateSignUpForm();
            }
        });

        header_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSoftKeyboard();
                logout();
                Intent intent = new Intent();
                intent.putExtra("from", from);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
    }

    private void validateSignUpForm() {
        String firstName = first_name.getText().toString().trim();
        String lastName = last_name.getText().toString().trim();
        String email_val = email.getText().toString().trim();
        String address_val = address.getText().toString().trim();
        String city_val = city.getText().toString().trim();
        String state_val = state.getText().toString().trim();
        String dob_val = dob.getText().toString().trim();
        String zip_val = zip_code.getText().toString();
        String password_val = password.getText().toString();
        String confirmPassword = confirm_password.getText().toString();
        String mobile_val = mobile.getText().toString();

        dialog = new EventDialogs(ctx);

        int pos = country.getSelectedItemPosition();
        if (pos > 0)
            countryId = countriesArr.get(pos - 1).getId();

        if (firstName.equals("")) {
            dialog.displayCommonDialog(getResources().getString(R.string.first_name_msg));
        } else if (lastName.equals("")) {
            dialog.displayCommonDialog(getResources().getString(R.string.last_name_msg));
        } else if (email_val.equals("")) {
            dialog.displayCommonDialog(getResources().getString(R.string.email_id_msg));
        } else if (!email_val.equals("") && !isValidEmail(email_val)) {
            dialog.displayCommonDialog(getResources().getString(R.string.email_vaild_id_msg));
        } else if (!email_val.matches(EventConstant.EMAIL_REGEX)) {
            dialog.displayCommonDialog(getResources().getString(R.string.email_vaild_id_msg));
        } else if (mobile_val.equals("")) {
            dialog.displayCommonDialog(getResources().getString(R.string.mobile_no_msg));
        } else if (!mobile_val.equals("") && !mobile_val.matches("[0-9]+")) {
            dialog.displayCommonDialog(getResources().getString(R.string.valid_mobile_no_msg));
        } else if (!mobile_val.equals("") && mobile_val.length() < 10) {
            dialog.displayCommonDialog(getResources().getString(R.string.no_between_msg));
        } else if (gender.equals("")) {
            dialog.displayCommonDialog(getResources().getString(R.string.gender_msg));
        } else if (address_val.equals("")) {
            dialog.displayCommonDialog(getResources().getString(R.string.address_msg));
        } else if (city_val.equals("")) {
            dialog.displayCommonDialog(getResources().getString(R.string.city_msg));
        } else if (state_val.equals("")) {
            dialog.displayCommonDialog(getResources().getString(R.string.state_msg));
        } else if (pos == 0) {
            dialog.displayCommonDialog(getResources().getString(R.string.country_msg));
        } else if (zip_val.equals("")) {
            dialog.displayCommonDialog(getResources().getString(R.string.zip_code_msg));
        } else if (!zip_val.equals("") && zip_val.length() < 5) {
            dialog.displayCommonDialog(getResources().getString(R.string.zip_code_no_msg));
        } else if (dob_val.equals("")) {
            dialog.displayCommonDialog(getResources().getString(R.string.dob_msg));
        } else if (password_val.equals("")) {
            dialog.displayCommonDialog(getResources().getString(R.string.pass_msg));
        } else if (password_val.length() < 6) {
            dialog.displayCommonDialog(getResources().getString(R.string.min_pass_msg));
        } else if (!password_val.matches(EventConstant.PASSWORD_REGEX)) {
            dialog.displayCommonDialog(getResources().getString(R.string.combination_pass_msg));
        } else if (confirmPassword.equals("")) {
            dialog.displayCommonDialog(getResources().getString(R.string.confirm_pass_msg));
        } else if (!password_val.equals(confirmPassword)) {
            dialog.displayCommonDialog(getResources().getString(R.string.confirm_pass_not_msg));
        } else {
            doSignUp(firstName, lastName, email_val, mobile_val, gender, dob_val, address_val, city_val, state_val, countryId, zip_val, password_val);
            hideSoftKeyboard();
        }
    }

    private void doSignUp(String firstName, final String lastName, final String email_val, String mobile_val, String gender, String dob, String address, String city_val, String state_val, String country_val, String zip_val, String password_val) {
        if (cd.isConnectingToInternet()) {
            final ProgressDialog d = EventDialogs.showLoading(ctx);
            d.setCanceledOnTouchOutside(false);

            ApiInterface apiService =
                    ApiClient.getClient().create(ApiInterface.class);

            Call<LoginPojo> call = apiService.postRegister("SignUp", firstName, lastName, email_val, gender, dob, mobile_val, address, city_val,
                    state_val, country_val, zip_val, password_val, getFromPrefs(EventConstant.DEVICE_ID), "Android", social_id, social_type, EventConstant.USER_ROLE_VAL);
            call.enqueue(new Callback<LoginPojo>() {
                @Override
                public void onResponse(Call<LoginPojo> call, Response<LoginPojo> response) {
                    if (response.body().getStatus_code().equals("1")) {
                        displayToast(response.body().getMessage());
                        logout();
                        Intent intent = new Intent();
                        intent.putExtra("from", from);
                        setResult(Activity.RESULT_OK, intent);
                        finish();
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

    private SpinnerAdapter typeSpinnerAdapter = new BaseAdapter() {

        private TextView text;

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = mInflator.inflate(R.layout.row_spinner, null);
            }
            text = (TextView) convertView.findViewById(R.id.spinnerTarget);
            text.setText(countriesArr.get(position).getName());
            return convertView;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public Object getItem(int position) {
            return countriesArr.get(position).getName();
        }

        @Override
        public int getCount() {
            return countriesArr.size();
        }

        public View getDropDownView(int position, View convertView,
                                    ViewGroup parent) {
            if (convertView == null) {
                convertView = mInflator.inflate(
                        R.layout.row_spinner, null);
            }
            text = (TextView) convertView.findViewById(R.id.spinnerTarget);
            text.setText(countriesArr.get(position).getName());
            return convertView;
        }
    };

    private void showDatePicker() {
        DatePickerDialog dialog = new DatePickerDialog(ctx, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker arg0, int year, int month, int day) {
                calendar.set(year, month, day);
                ctx.year = year;
                ctx.month = month;
                ctx.day = day;
                updateLabel();
            }
        }, year, month, day);
        dialog.getDatePicker().setMaxDate(new Date().getTime());
        dialog.show();
    }

    private void updateLabel() {
        SimpleDateFormat sdf = getDateFormat();
        dob.setText(sdf.format(calendar.getTime()));
    }

    private void getCountries() {
        EventDialogs dialog = new EventDialogs(this);
        if (cd.isConnectingToInternet()) {
            final ProgressDialog d = EventDialogs.showLoading(this);
            d.setCanceledOnTouchOutside(false);

            ApiInterface apiService =
                    ApiClient.getClient().create(ApiInterface.class);

            Call<CountriesPojo> call = apiService.getCountries("GetCountries");
            call.enqueue(new Callback<CountriesPojo>() {
                @Override
                public void onResponse(Call<CountriesPojo> call, Response<CountriesPojo> response) {
                    d.dismiss();
                    if (response.body().getStatus_code().equals("1")) {
                        countriesArr = response.body().getData();

                        country.setPrompt(getResources().getString(R.string.select_country));

                        country.setAdapter(
                                new NothingSelectedSpinnerAdapter(
                                        typeSpinnerAdapter,
                                        R.layout.spinner_nothing_selected_country,
                                        ctx));
                    }
                }

                @Override
                public void onFailure(Call<CountriesPojo> call, Throwable t) {
                    // Log error here since request failed
                    d.dismiss();
                }
            });
        } else {
            dialog.displayCommonDialog(getResources().getString(R.string.no_internet_connection));
        }
    }

    public void googlePlusLogout() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                    }
                });
    }

    @Override
    public void onBackPressed() {
        logout();
        Intent intent = new Intent();
        intent.putExtra("from", from);
        setResult(Activity.RESULT_CANCELED, intent);
        finish();
    }

    private void logout() {
        if (getFromPrefs(EventConstant.LOGGEDINFROM).equals("gmail"))
            googlePlusLogout();
        else if (getFromPrefs(EventConstant.LOGGEDINFROM).equals("fb")) {
            FacebookSdk.sdkInitialize(getApplicationContext());
            LoginManager.getInstance().logOut();
            saveIntoPrefs(EventConstant.LOGGEDINFROM, "null");
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
