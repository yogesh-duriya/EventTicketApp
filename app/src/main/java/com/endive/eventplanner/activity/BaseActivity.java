package com.endive.eventplanner.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.text.style.ClickableSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.endive.eventplanner.R;
import com.endive.eventplanner.fragments.DrawerFragment;
import com.endive.eventplanner.interfaces.UpdateImageIntercace;
import com.endive.eventplanner.model.ApiClient;
import com.endive.eventplanner.model.ApiInterface;
import com.endive.eventplanner.pojo.BasePojo;
import com.endive.eventplanner.pojo.CategoriesPojo;
import com.endive.eventplanner.pojo.FilterPojo;
import com.endive.eventplanner.pojo.LocationPojo;
import com.endive.eventplanner.pojo.PackageMerchandiseDetailPojo;
import com.endive.eventplanner.pojo.SomethingInterestingDataPojo;
import com.endive.eventplanner.util.ConnectionDetector;
import com.endive.eventplanner.util.EventConstant;
import com.endive.eventplanner.util.EventDialogs;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by arpit.jain on 10/4/2017.
 */

public class BaseActivity extends AppCompatActivity {
    public int REQUEST_CHECK_SETTINGS = 0x1;
    public DrawerLayout mDrawerLayout;
    private SharedPreferences sharedPreferences;
    private BaseActivity ctx = this;
    private boolean check = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
    }

    public void setHeader(String header) {
        TextView tv_header = (TextView) findViewById(R.id.header);
        tv_header.setText(header);

        ImageView back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSoftKeyboard();
                BaseActivity.this.onBackPressed();
            }
        });

        TextView skip = (TextView) findViewById(R.id.header_skip);
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    public Toolbar setDrawerAndToolbar(String name) {
        Toolbar appbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(appbar);
        getSupportActionBar().setTitle("");
        final ImageView drawer_button = (ImageView) findViewById(R.id.nav);

        TextView header = (TextView) findViewById(R.id.header);

        header.setText(name);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard();
                if (mDrawerLayout != null && !mDrawerLayout.isDrawerOpen(Gravity.LEFT))
                    mDrawerLayout.openDrawer(Gravity.LEFT);
                else if (mDrawerLayout != null)
                    mDrawerLayout.closeDrawers();
            }
        });
        if (mDrawerLayout != null) {
            DrawerFragment fragment = (DrawerFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_drawer);
            fragment.setUp(mDrawerLayout);
        }

        return appbar;
    }

    public void hideSoftKeyboard() {
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    public boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public void saveIntoPrefs(String key, String value) {
        SharedPreferences prefs = getSharedPreferences(EventConstant.PREF_NAME, MODE_PRIVATE);
        SharedPreferences.Editor edit = prefs.edit();
        edit.putString(key, value);
        edit.commit();
    }

    public void saveIntIntoPrefs(String key, int value) {
        SharedPreferences prefs = getSharedPreferences(EventConstant.PREF_NAME, MODE_PRIVATE);
        SharedPreferences.Editor edit = prefs.edit();
        edit.putInt(key, value);
        edit.commit();
    }

    public void saveDoubleIntoPrefs(String key, double value) {
        SharedPreferences prefs = getSharedPreferences(EventConstant.PREF_NAME, MODE_PRIVATE);
        SharedPreferences.Editor edit = prefs.edit();
        edit.putLong(key, Double.doubleToRawLongBits(value));
        edit.commit();
    }

    public String getFromPrefs(String key) {
        SharedPreferences prefs = getSharedPreferences(EventConstant.PREF_NAME, MODE_PRIVATE);
        return prefs.getString(key, EventConstant.DEFAULT_VALUE);
    }

    public int getIntFromPrefs(String key) {
        SharedPreferences prefs = getSharedPreferences(EventConstant.PREF_NAME, MODE_PRIVATE);
        return prefs.getInt(key, 0);
    }

    public double getDoubleFromPrefs(String key) {
        SharedPreferences prefs = getSharedPreferences(EventConstant.PREF_NAME, MODE_PRIVATE);
        return Double.longBitsToDouble(prefs.getLong(key, Double.doubleToLongBits(0)));
    }


    public void saveBooleanIntoPrefs(String key, boolean value) {
        SharedPreferences prefs = getSharedPreferences(EventConstant.PREF_NAME, MODE_PRIVATE);
        SharedPreferences.Editor edit = prefs.edit();
        edit.putBoolean(key, value);
        edit.commit();

    }

    public boolean getBooleanFromPrefs(String key) {
        SharedPreferences prefs = getSharedPreferences(EventConstant.PREF_NAME, MODE_PRIVATE);
        return prefs.getBoolean(key, false);
    }

    public void saveFilterData(FilterPojo filterData) {
        SharedPreferences sharedPreferences = getSharedPreferences(EventConstant.PREF_NAME, Activity.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(filterData);
        prefsEditor.putString("filterObject", json);
        prefsEditor.commit();
    }

    public FilterPojo getFilterData() {
        SharedPreferences sharedPreferences = getSharedPreferences(EventConstant.PREF_NAME, Activity.MODE_PRIVATE);
        final Gson gson = new Gson();
        String json = sharedPreferences.getString("filterObject", "");
        FilterPojo obj = gson.fromJson(json, FilterPojo.class);

        if (obj != null) {
            return obj;
        }
        return null;
    }

    public void saveCategoryData(CategoriesPojo filterData) {
        SharedPreferences sharedPreferences = getSharedPreferences(EventConstant.PREF_NAME, Activity.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(filterData);
        prefsEditor.putString("categoryAndEventType", json);
        prefsEditor.commit();
    }

    public CategoriesPojo getCategoryData() {
        SharedPreferences sharedPreferences = getSharedPreferences(EventConstant.PREF_NAME, Activity.MODE_PRIVATE);
        final Gson gson = new Gson();
        String json = sharedPreferences.getString("categoryAndEventType", "");
        CategoriesPojo obj = gson.fromJson(json, CategoriesPojo.class);

        if (obj != null) {
            return obj;
        }
        return null;
    }

    public void saveLocationData(LocationPojo locationData) {
        SharedPreferences sharedPreferences = getSharedPreferences(EventConstant.PREF_NAME, Activity.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(locationData);
        prefsEditor.putString("locations", json);
        prefsEditor.commit();
    }

    public LocationPojo getLocationData() {
        SharedPreferences sharedPreferences = getSharedPreferences(EventConstant.PREF_NAME, Activity.MODE_PRIVATE);
        final Gson gson = new Gson();
        String json = sharedPreferences.getString("locations", "");
        LocationPojo obj = gson.fromJson(json, LocationPojo.class);

        if (obj != null) {
            return obj;
        }
        return null;
    }

    public void clearPrefs() {
        SharedPreferences prefs = getSharedPreferences(EventConstant.PREF_NAME, MODE_PRIVATE);
        prefs.edit().clear().commit();
    }

    public SimpleDateFormat getDateFormat() {
        String myFormat = "yyy/MM/dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        return sdf;
    }

    public void displayToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    public void displayToastLong(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    public void setImageInLayout(Context ctx, int width, int height, String url, ImageView image) {
        if(url != null) {
            Picasso picasso = Picasso.with(ctx);
            picasso.load(url).resize(width, height).placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher).into(image);
        }
    }

    public void setProfileImageInLayout(Context ctx, int width, int height, String url, ImageView image) {
        if (url != null && !url.equals(""))
            Picasso.with(ctx).load(url).placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher).into(image);
    }

    public void setCategoryImageInLayout(Context ctx, String url, ImageView image) {
        if (url != null && !url.equals(""))
            Picasso.with(ctx).load(url).fit().centerCrop().placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher).into(image);
    }

    public String getDate(String date_val) {
        Date date = getFormattedDate(date_val);
        String modified_date = "";
        String dayOfTheWeek = getDayOfWeek(date);
        String day = getDay(date);
        String monthString = getMonth(date);

        modified_date = dayOfTheWeek + ", " + monthString + " " + day + "  " + getTimeSuffix(date);
        return modified_date;
    }

    public String getDateDetail(String date_val) {
        Date date = getFormattedDate(date_val);
        String modified_date = "";
        String dayOfTheWeek = getDayOfWeek(date);
        String day = getDay(date);
        String monthString = getMonth(date);

        modified_date = dayOfTheWeek + ", " + day + " " + monthString + " " + getYear(date);
        return modified_date;
    }

    public Date getFormattedDate(String date_val) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        Date date = null;
        try {
            date = format.parse(date_val);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public String getDayOfWeek(Date date) {
        return (String) DateFormat.format("EEE", date);
    }

    public String getMonth(Date date) {
        return (String) DateFormat.format("MMM", date);
    }

    public String getMonthValue(Date date) {
        return (String) DateFormat.format("MM", date);
    }

    public String getDay(Date date) {
        return (String) DateFormat.format("dd", date);
    }

    public String getYear(Date date) {
        return (String) DateFormat.format("yyy", date);
    }

    public String getTime(Date date) {
        String hour = (String) DateFormat.format("hh", date);
        String min = (String) DateFormat.format("mm", date);

        return hour + ":" + min;
    }

    public String getTimeSuffix(Date date) {
        String delegate = "hh:mm a";
        return (String) DateFormat.format(delegate, date);
    }

    public String getDate(Date date) {
        return getYear(date) + "/" + getMonthValue(date) + "/" + getDay(date);
    }

    public SpannableString getColoredString(String str, final int color) {
        if (str != null) {
            SpannableString str2 = new SpannableString(str);
            ClickableSpan clickSpan = new ClickableSpan() {
                @Override
                public void updateDrawState(TextPaint ds) {
                    ds.setColor(color);    // you can use custom color
                    ds.setUnderlineText(false);    // this remove the underline
                }

                @Override
                public void onClick(View textView) {
                    // handle click event
                }
            };

            str2.setSpan(clickSpan, 0, str2.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            return str2;
        } else
            return null;
    }

    public void setTagList(LinearLayout layout, SomethingInterestingDataPojo data) {
        for (int i = 0; i < data.getTags().size(); i++) {
            if (i < 3) {
                LinearLayout innerLayout = new LinearLayout(this);
                innerLayout.setOrientation(LinearLayout.VERTICAL);
                innerLayout.setBackground(this.getResources().getDrawable(R.drawable.white_circular_bg_with_border));
                innerLayout.setPadding(pixelsToDp(getResources().getDimensionPixelSize(R.dimen.padding_left)), pixelsToDp(getResources().getDimensionPixelSize(R.dimen.padding_top)), pixelsToDp(getResources().getDimensionPixelSize(R.dimen.padding_left)), pixelsToDp(getResources().getDimensionPixelSize(R.dimen.padding_top)));
                LinearLayout.LayoutParams innerLayoutLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                innerLayoutLayoutParams.setMargins(0, 0, 20, 0);
                innerLayout.setAlpha(0.8f);
                innerLayout.setLayoutParams(innerLayoutLayoutParams);

                TextView tag_name = new TextView(this);
                tag_name.setText(data.getTags().get(i).toUpperCase());
                tag_name.setTextSize(pixelsToSp(getResources().getDimensionPixelSize(R.dimen.onwards_text_size)));
                tag_name.setTextColor(ContextCompat.getColor(this, R.color.black));
                tag_name.setTypeface(tag_name.getTypeface(), Typeface.BOLD);

                LinearLayout.LayoutParams descLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                descLayoutParams.gravity = Gravity.CENTER;
                tag_name.setLayoutParams(descLayoutParams);

                innerLayout.addView(tag_name);
                layout.addView(innerLayout);
            } else
                break;
        }
    }

    public void setMerchandiseList(LinearLayout layout, ArrayList<PackageMerchandiseDetailPojo> arr){
        for (int position = 0; position < arr.size(); position++) {
            View itemView = LayoutInflater.from(ctx).inflate(R.layout.merchandise_list_order_summary, null, false);

            ImageView merchandise_image_small = (ImageView) itemView.findViewById(R.id.merchandise_image_small);
            TextView merchandise_name = (TextView) itemView.findViewById(R.id.merchandise_name);
            TextView merchandise_quantity = (TextView) itemView.findViewById(R.id.merchandise_quantity);

            merchandise_quantity.setText(" ( " + arr.get(position).getMerchandise_quantity() + " )");
            merchandise_name.setText(arr.get(position).getMerchandise_detail().getName());

            setCategoryImageInLayout(ctx, arr.get(position).getMerchandise_detail().getImage(), merchandise_image_small);

            layout.addView(itemView);
        }
    }

    public float pixelsToSp(float px) {
        float scaledDensity = getResources().getDisplayMetrics().scaledDensity;
        return px / scaledDensity;
    }

    public int pixelsToDp(float px) {
        float scale = getResources().getDisplayMetrics().density;
        return (int) (px * scale + 0.5f);
    }

    public String getDayOfMonthSuffix(final int n) {
        if (n >= 11 && n <= 13) {
            return "th";
        }
        switch (n % 10) {
            case 1:
                return "st";
            case 2:
                return "nd";
            case 3:
                return "rd";
            default:
                return "th";
        }
    }

    public void setFavorite(final SomethingInterestingDataPojo data, final String method, @Nullable final UpdateImageIntercace callbacks) {
        ConnectionDetector cd = new ConnectionDetector(ctx);
        final EventDialogs dialog = new EventDialogs(ctx);

        if (cd.isConnectingToInternet()) {
            final ProgressDialog d = EventDialogs.showLoading(ctx);
            d.setCanceledOnTouchOutside(false);

            ApiInterface apiService =
                    ApiClient.getClient().create(ApiInterface.class);

            Call<BasePojo> call = apiService.setFavorite(method, getFromPrefs(EventConstant.USER_ID), data.getId());
            call.enqueue(new Callback<BasePojo>() {
                @Override
                public void onResponse(Call<BasePojo> call, Response<BasePojo> response) {
                    if (response.body().getStatus_code().equals("1")) {
                        if (method.equals("SetFavourite"))
                            data.setIs_favourite(response.body().is_favourite());
                        else if (method.equals("SetLike"))
                            data.setIs_liked(response.body().is_liked());

                        if (callbacks != null)
                            callbacks.onSuccess("");
//                        adapter.notifyDataSetChanged();
                    } else {
                        dialog.displayCommonDialog(response.body().getError_message());
                    }
                    d.dismiss();
                }

                @Override
                public void onFailure(Call<BasePojo> call, Throwable t) {
                    // Log error here since request failed
                    if (callbacks != null)
                        callbacks.onError(t);
                    d.dismiss();
                }
            });
        } else {
            dialog.displayCommonDialog(getResources().getString(R.string.no_internet_connection));
        }
    }

    public void displaySharingIntent(SomethingInterestingDataPojo data) {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.share_text));
        sharingIntent.putExtra(Intent.EXTRA_TEXT, data.getEvent_url());
        startActivity(Intent.createChooser(sharingIntent, getResources().getString(R.string.share_using)));
    }

    // dynamically permission added

    /**
     * method that will return whether the permission is accepted. By default it is true if the user is using a device below
     * version 23
     *
     * @param permission
     * @return
     */

    public boolean hasPermission(String permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (canMakeSmores()) {
                return (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }

    /**
     * method to determine whether we have asked
     * for this permission before.. if we have, we do not want to ask again.
     * They either rejected us or later removed the permission.
     *
     * @param permission
     * @return
     */
    public boolean shouldWeAsk(String permission) {
        return (sharedPreferences.getBoolean(permission, true));
    }

    /**
     * we will save that we have already asked the user
     *
     * @param permission
     */
    public void markAsAsked(String permission, SharedPreferences sharedPreferences) {
        sharedPreferences.edit().putBoolean(permission, false).apply();
    }

    /**
     * We may want to ask the user again at their request.. Let's clear the
     * marked as seen preference for that permission.
     *
     * @param permission
     */
    public void clearMarkAsAsked(String permission) {
        sharedPreferences.edit().putBoolean(permission, true).apply();
    }


    /**
     * This method is used to determine the permissions we do not have accepted yet and ones that we have not already
     * bugged the user about.  This comes in handle when you are asking for multiple permissions at once.
     *
     * @param wanted
     * @return
     */
    public ArrayList<String> findUnAskedPermissions(ArrayList<String> wanted) {
        ArrayList<String> result = new ArrayList<String>();

        for (String perm : wanted) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }

        return result;
    }

    /**
     * this will return us all the permissions we have previously asked for but
     * currently do not have permission to use. This may be because they declined us
     * or later revoked our permission. This becomes useful when you want to tell the user
     * what permissions they declined and why they cannot use a feature.
     *
     * @param wanted
     * @return
     */
    public ArrayList<String> findRejectedPermissions(ArrayList<String> wanted) {
        ArrayList<String> result = new ArrayList<String>();

        for (String perm : wanted) {
            if (!hasPermission(perm) && !shouldWeAsk(perm)) {
                result.add(perm);
            }
        }

        return result;
    }

    /**
     * Just a check to see if we have marshmallows (version 23)
     *
     * @return
     */
    public boolean canMakeSmores() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    /**
     * a method that will centralize the showing of a snackbar
     */
    public void makePostRequestSnack(String message, int size) {

        Toast.makeText(getApplicationContext(), String.valueOf(size) + " " + message, Toast.LENGTH_SHORT).show();

        finish();
    }

    public void setRecyclerLayoutManager(RecyclerView recycler) {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recycler.setLayoutManager(mLayoutManager);
    }

    public void removeActivity(String activity) {
        for (int i = 0; i < EventConstant.ACTIVITIES.size(); i++) {
            if (EventConstant.ACTIVITIES.get(i) != null && EventConstant.ACTIVITIES.get(i).toString().contains(activity)) {
                EventConstant.ACTIVITIES.get(i).finish();
                EventConstant.ACTIVITIES.remove(i);
                break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout != null && mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
            mDrawerLayout.closeDrawers();
        } else
            super.onBackPressed();
    }
}
