package com.endive.eventplanner.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.braintreepayments.api.BraintreeFragment;
import com.braintreepayments.api.dropin.DropInActivity;
import com.braintreepayments.api.dropin.DropInRequest;
import com.braintreepayments.api.dropin.DropInResult;
import com.braintreepayments.api.dropin.utils.PaymentMethodType;
import com.braintreepayments.api.exceptions.InvalidArgumentException;
import com.braintreepayments.api.models.PaymentMethodNonce;
import com.braintreepayments.api.models.VenmoAccountNonce;
import com.endive.eventplanner.R;
import com.endive.eventplanner.model.ApiClient;
import com.endive.eventplanner.model.ApiInterface;
import com.endive.eventplanner.pojo.ColorSizeListPojo;
import com.endive.eventplanner.pojo.CouponDataPojo;
import com.endive.eventplanner.pojo.EventPackageDetailPojo;
import com.endive.eventplanner.pojo.PackageMerchandiseDetailPojo;
import com.endive.eventplanner.pojo.PackageTicketTypeDetailPojo;
import com.endive.eventplanner.pojo.PaymentNonceDataPojo;
import com.endive.eventplanner.pojo.PersonTicketDetailPojo;
import com.endive.eventplanner.pojo.SomethingInterestingDataPojo;
import com.endive.eventplanner.pojo.TicketDataPojo;
import com.endive.eventplanner.util.ConnectionDetector;
import com.endive.eventplanner.util.EventConstant;
import com.endive.eventplanner.util.EventDialogs;
import com.endive.eventplanner.util.GenerateJsonObject;
import com.endive.eventplanner.view.CircularTextView;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.wallet.Cart;
import com.google.android.gms.wallet.LineItem;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by upasna.mishra on 11/7/2017.
 */

public class OrderSummaryActivity extends BaseActivity implements View.OnClickListener, OnMapReadyCallback {

    private TextView tv_event_date, tv_event_name, tv_book_date, tv_order_id, event_location, tv_seat_no, tv_ticket_price,
            tv_discount_price, tv_total_price, apply_coupon, tv_pack_name,
            tv_desc, tv_buy_now;
    private EditText coupon_value;
    private LinearLayout pay_now, lin_package_details, merchandise_list, apply_coupon_layout, discount_layout, lin_ticket_info;
    private String amount = null, coupon_code = null;
    private SomethingInterestingDataPojo data;
    private ArrayList<PersonTicketDetailPojo> personListArr;
    private GoogleMap googleMap;
    private CouponDataPojo couponData;
    private ScrollView scrollview;
    private ImageView transparent_image;
    private EventPackageDetailPojo packageData;
    private BigDecimal ticketTotal, discountPrice, payablePrice;
    private BigDecimal coupon_discount;
    private BigDecimal totalDiscount;
    private String tag = "ticket";
    private String seats = "";
    private TicketDataPojo dataTicket;
    private LinearLayout user_list;
    private OrderSummaryActivity ctx = this;

    private final static int REQUEST_CODE = 1000;
    private BraintreeFragment mBraintreeFragment;
    private ConnectionDetector cd;
    private EventDialogs dialog;
    private int color_pos = 0;
    private int size_pos = 0;
    private LinearLayout edit;
    private int ticketCount;
    private HorizontalScrollView merchandise_list_scroll;
    private String package_id = "", venue_id = "", ticket_id = "";
    private String booking_type = "", package_name = "", ticket_name = "";
    private String is_like_or_share = "0";
    //  Below parameters used for FB sharing.
    private ShareDialog shareDialog;
    private CallbackManager callbackManager;
    private ShareLinkContent contentLink;
    private ImageView nav_invisible;
    boolean shared = false;
    private ImageView remove_coupon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_summary);
        tv_buy_now = (TextView) findViewById(R.id.tv_buy_now);
        data = (SomethingInterestingDataPojo) getIntent().getSerializableExtra("event_detail");
        personListArr = (ArrayList<PersonTicketDetailPojo>) getIntent().getSerializableExtra("tickets_detail");
        dataTicket = (TicketDataPojo) getIntent().getSerializableExtra("dataTicket");
        packageData = (EventPackageDetailPojo) getIntent().getSerializableExtra("packageData");
        seats = getIntent().getStringExtra("seats");
        venue_id = getIntent().getStringExtra("venue_id");
        ticketCount = getIntent().getIntExtra("ticketCount", 0);
        tv_buy_now.setVisibility(View.GONE);

        setHeader(getResources().getString(R.string.header_order_summary));
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map_view);
        mapFragment.getMapAsync(this);

        initialize();
        setListener();
        fbShareContent();
    }

    private void fbShareContent() {
        contentLink = new ShareLinkContent.Builder()
                .setContentUrl(Uri.parse(data.getEvent_url()))
                .setQuote(getResources().getString(R.string.share_text))
                .build();

        callbackManager = CallbackManager.Factory.create();

        shareDialog = new ShareDialog(this);
        // this part is optional
        shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result) {
                if (!shared) {
                    shared = true;
                    if (data.getShare_discount_type().equals("2")) {
                        discountPrice = payablePrice.multiply(new BigDecimal(data.getShare_discount())).divide(new BigDecimal(100));
                    } else {
                        discountPrice = new BigDecimal(data.getShare_discount());
                    }
                    totalDiscount = totalDiscount.add(discountPrice);
                    nav_invisible.setVisibility(View.GONE);
                    is_like_or_share = "2";
                    manageDiscount();
                }
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException error) {
            }
        });
    }

    private void fbShare() {
        ShareDialog.show(this, contentLink);
    }

    private void initialize() {
        try {
            mBraintreeFragment = BraintreeFragment.newInstance(ctx, getFromPrefs(EventConstant.CLIENT_TOKEN));
            // mBraintreeFragment is ready to use!
        } catch (InvalidArgumentException e) {
            // There was an issue with your authorization string.
        }
        cd = new ConnectionDetector(this);
        dialog = new EventDialogs(ctx);
        nav_invisible = (ImageView) findViewById(R.id.nav_invisible);
        if (data.getIs_social_media_discount().equals("1") && !data.isIs_share())
            nav_invisible.setVisibility(View.VISIBLE);
        nav_invisible.setBackground(ContextCompat.getDrawable(ctx, R.mipmap.share));
        edit = (LinearLayout) findViewById(R.id.edit);
        tv_event_date = (TextView) findViewById(R.id.tv_event_date);
        tv_event_name = (TextView) findViewById(R.id.tv_event_name);
        scrollview = (ScrollView) findViewById(R.id.scrollview);
        transparent_image = (ImageView) findViewById(R.id.transparent_image);
        tv_book_date = (TextView) findViewById(R.id.tv_book_date);
        tv_order_id = (TextView) findViewById(R.id.tv_order_id);
        event_location = (TextView) findViewById(R.id.event_location);
        tv_seat_no = (TextView) findViewById(R.id.tv_seat_no);
        tv_ticket_price = (TextView) findViewById(R.id.tv_ticket_price);
        tv_discount_price = (TextView) findViewById(R.id.tv_discount_price);
        tv_total_price = (TextView) findViewById(R.id.tv_total_price);
        apply_coupon = (TextView) findViewById(R.id.apply_coupon);
        pay_now = (LinearLayout) findViewById(R.id.pay_now);
        user_list = (LinearLayout) findViewById(R.id.user_list);
        apply_coupon_layout = (LinearLayout) findViewById(R.id.apply_coupon_layout);
        remove_coupon = (ImageView) findViewById(R.id.remove_coupon);
        discount_layout = (LinearLayout) findViewById(R.id.discount_layout);
        coupon_value = (EditText) findViewById(R.id.coupon_value);
        tv_pack_name = (TextView) findViewById(R.id.tv_pack_name);
        tv_desc = (TextView) findViewById(R.id.tv_desc);
        lin_package_details = (LinearLayout) findViewById(R.id.lin_package_details);
        lin_ticket_info = (LinearLayout) findViewById(R.id.lin_ticket_info);
        merchandise_list = (LinearLayout) findViewById(R.id.merchandise_list);
        merchandise_list_scroll = (HorizontalScrollView) findViewById(R.id.merchandise_list_scroll);

        if (dataTicket != null) {
            lin_package_details.setVisibility(View.GONE);
            ticketTotal = new BigDecimal(dataTicket.getPrice());
            ticket_id = dataTicket.getEvent_group_ticket_id();
            booking_type = "1";
            package_name = "NA";
            ticket_name = dataTicket.getTitle();
            if (personListArr.size() == 1)
                tag = "(" + personListArr.size() + " " + getResources().getString(R.string.ticket) + ")";
            else
                tag = "(" + personListArr.size() + " " + getResources().getString(R.string.tickets) + ")";
        } else if (packageData != null) {
            lin_package_details.setVisibility(View.VISIBLE);
            ticketTotal = new BigDecimal(packageData.getPrice());
            package_name = packageData.getPackage_detail().getTitle();
            package_id = packageData.getPackage_id();
            for (int i = 0; i < packageData.getPackage_detail().getPackage_ticket_type_detail().size(); i++) {
                ticket_name += packageData.getPackage_detail().getPackage_ticket_type_detail().get(i).getTicket_type_detail().getTitle() + "|";
            }
            ticket_name = ticket_name.substring(0, ticket_name.length() - 1);
            booking_type = "2";
        } else {
            lin_package_details.setVisibility(View.GONE);
            ticketTotal = new BigDecimal("0.00");
            apply_coupon_layout.setVisibility(View.GONE);
            package_name = "NA";
            booking_type = "3";
            ticket_name = "NA";
            if (personListArr.size() == 1)
                tag = "(" + personListArr.size() + " " + getResources().getString(R.string.ticket) + ")";
            else
                tag = "(" + personListArr.size() + " " + getResources().getString(R.string.tickets) + ")";
        }
        discount_layout.setVisibility(View.GONE);
        discountPrice = new BigDecimal("0.00");
        if (packageData != null) {
            tv_pack_name.setText(packageData.getPackage_detail().getTitle());

            ArrayList<PackageTicketTypeDetailPojo> arrTicket = packageData.getPackage_detail().getPackage_ticket_type_detail();
            lin_ticket_info.removeAllViews();
            for (int i = 0; i < arrTicket.size(); i++) {
                View itemView = LayoutInflater.from(ctx).inflate(R.layout.package_ticket_row_layout, null, false);
                TextView tv_ticket_type = (TextView) itemView.findViewById(R.id.tv_ticket_type);
                TextView tv_total_ticket = (TextView) itemView.findViewById(R.id.tv_total_ticket);
                tv_ticket_type.setText(arrTicket.get(i).getTicket_type_detail().getTitle());
                tv_total_ticket.setText(arrTicket.get(i).getTicket_quantity());
                lin_ticket_info.addView(itemView);
            }

            tv_desc.setText(packageData.getPackage_detail().getPackage_ticket_type_detail().get(0).getTicket_type_detail().getDescription());
            tag = "(" + getResources().getString(R.string.package_price) + ")";
            ArrayList<PackageMerchandiseDetailPojo> arr = packageData.getPackage_detail().getPackage_merchandise_detail();
            merchandise_list_scroll.removeAllViews();
            LinearLayout topLinearLayout = new LinearLayout(ctx);
            topLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
            topLinearLayout.setPadding(5, 5, 5, 5);

            setMerchandiseList(topLinearLayout, arr);

            merchandise_list_scroll.addView(topLinearLayout);

        }
        if (seats == null) {
            seats = "";
        } else
            seats = seats + " ";
        tv_seat_no.setText(seats + tag);

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("MMMM dd, yyyy");
        String formattedDate = df.format(c.getTime());

        tv_book_date.setText(getResources().getString(R.string.booked_on) + " " + formattedDate);

        tv_event_date.setText(getResources().getString(R.string.from) + ": " + getDate(data.getStart_time()) + ", " + getResources().getString(R.string.to) + ": " + getDate(data.getEnd_time()));
        tv_event_name.setText(data.getTitle());
        event_location.setText(data.getVenue());

        ticketTotal = ticketTotal.multiply(new BigDecimal(personListArr.size()));
        tv_ticket_price.setText(getResources().getString(R.string.dollar) + " " + getScaledData(ticketTotal));

        totalDiscount = discountPrice;
        payablePrice = ticketTotal.subtract(discountPrice);

        tv_discount_price.setText(getResources().getString(R.string.dollar) + " " + getScaledData(discountPrice));
        tv_total_price.setText(getResources().getString(R.string.dollar) + " " + getScaledData(payablePrice));
        displayPersonList();
        scrollview.smoothScrollTo(0, 0);
    }

    private void displayPersonList() {
        user_list.removeAllViews();
        for (int i = 0; i < personListArr.size(); i++) {
            View v = LayoutInflater.from(this).inflate(R.layout.row_show_fill_details, null, false);
            TextView tv_gender, tv_user_name, tv_age;
            LinearLayout merchandise_list;
            tv_gender = (TextView) v.findViewById(R.id.tv_gender);
            tv_user_name = (TextView) v.findViewById(R.id.tv_user_name);
            tv_age = (TextView) v.findViewById(R.id.tv_age);
            merchandise_list = (LinearLayout) v.findViewById(R.id.merchandise_list_users);
            if (packageData != null) {
                merchandise_list.removeAllViews();
                ArrayList<PackageMerchandiseDetailPojo> arr_mer = personListArr.get(i).getPackageData().getPackage_detail().getPackage_merchandise_detail();
                for (int j = 0; j < arr_mer.size(); j++) {
                    View merchandise = LayoutInflater.from(this).inflate(R.layout.row_layout_merchandise_list_order_summary, null, false);
                    TextView merchandise_name = (TextView) merchandise.findViewById(R.id.merchandise_name);
                    TextView size_view = (TextView) merchandise.findViewById(R.id.size_view);
                    CircularTextView tv_solid_color_set = (CircularTextView) merchandise.findViewById(R.id.tv_solid_color_set);
                    merchandise_name.setText(arr_mer.get(j).getMerchandise_detail().getName());

                    ArrayList<ColorSizeListPojo> color = arr_mer.get(j).getMerchandise_detail().getMerchandise_property_detail().getColor();
                    ArrayList<ColorSizeListPojo> size = arr_mer.get(j).getMerchandise_detail().getMerchandise_property_detail().getSize();
                    color_pos = 0;
                    size_pos = 0;
                    for (int k = 0; k < color.size(); k++) {
                        if (color.get(k).isSelected()) {
                            color_pos = k;
                            break;
                        }
                    }
                    for (int k = 0; k < size.size(); k++) {
                        if (size.get(k).isSelected()) {
                            size_pos = k;
                            break;
                        }
                    }

                    tv_solid_color_set.setSolidColor("#" + color.get(color_pos).getColor());
                    tv_solid_color_set.setStrokeWidth(1);
                    tv_solid_color_set.setStrokeColor();
                    size_view.setText(size.get(size_pos).getSize());

                    merchandise_list.addView(merchandise);
                }
            } else {
                merchandise_list.setVisibility(View.GONE);
            }
            tv_user_name.setText(personListArr.get(i).getName());
            tv_age.setText(personListArr.get(i).getAge()+" "+getResources().getString(R.string.years));
            if (personListArr.get(i).getGender().equals("1")) {
                tv_gender.setText(getResources().getString(R.string.male));
            } else if (personListArr.get(i).getGender().equals("2")) {
                tv_gender.setText(getResources().getString(R.string.female));
            } else {
                tv_gender.setText(getResources().getString(R.string.other));
            }
            user_list.addView(v);
        }

        System.out.println("jsonString: " + GenerateJsonObject.getJsonObject(personListArr).toString());
    }

    private void setListener() {
        transparent_image.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        scrollview.requestDisallowInterceptTouchEvent(true);
                        // Disable touch on transparent view
                        return false;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        scrollview.requestDisallowInterceptTouchEvent(false);
                        return true;

                    case MotionEvent.ACTION_MOVE:
                        scrollview.requestDisallowInterceptTouchEvent(true);
                        return false;

                    default:
                        return true;
                }
            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ctx, FillTicketDetailsActivity.class);
                intent.putExtra("ticketCount", ticketCount);
                intent.putExtra("event_detail", data);
                intent.putExtra("dataTicket", dataTicket);
                intent.putExtra("color_pos", color_pos);
                intent.putExtra("seats", seats);
                intent.putExtra("from", "order_summary");
                intent.putExtra("tickets_detail", personListArr);
                intent.putExtra("packageData", packageData);
                startActivityForResult(intent, EventConstant.EDIT_DETAILS);
            }
        });
        nav_invisible.setOnClickListener(this);
        apply_coupon.setOnClickListener(this);
        pay_now.setOnClickListener(this);
        remove_coupon.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.apply_coupon:
                Intent couponIntent = new Intent(this, CouponActivity.class);
                couponIntent.putExtra("coupon_data", couponData);
                couponIntent.putExtra("event_id", data.getId());
                startActivityForResult(couponIntent, 1);
                break;
            case R.id.nav_invisible:
                fbShare();
                break;
            case R.id.pay_now:
                if (!getFromPrefs(EventConstant.USER_ID).equals("")) {
                    if (booking_type.equals("3")) {
                        sendPaymentNonce("");
                    } else {
                        onBraintreeSubmit();
                    }
                } else {
                    Intent intent = new Intent(ctx, LoginActivity.class);
                    intent.putExtra("from", "home");
                    startActivity(intent);
                }
                break;

            case R.id.remove_coupon:
                removeCoupon();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                couponData = (CouponDataPojo) data.getSerializableExtra("coupon_data");
                amount = couponData.getCoupon_code_amount();
                coupon_code = couponData.getCoupon_code();
                String type = couponData.getCoupon_code_price_type();

                if (type.equals("1")) {
                    coupon_discount = new BigDecimal(getData(amount));
                } else {
                    coupon_discount = new BigDecimal(amount.replace("%", "").trim());
                    coupon_discount = (payablePrice.multiply(coupon_discount.divide(new BigDecimal(100))));
                }
                totalDiscount = discountPrice.add(coupon_discount);
                if (coupon_code == null) {
                    coupon_value.setText("");
                } else {
                    manageDiscount();
                }
            }
            if (resultCode == Activity.RESULT_CANCELED && data != null) {

            }
        } else if (requestCode == REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                DropInResult result = data.getParcelableExtra(DropInResult.EXTRA_DROP_IN_RESULT);
                System.out.println("xhxhxhxhx " + result.getPaymentMethodNonce().getNonce().toString());
                sendPaymentNonce(result.getPaymentMethodNonce().getNonce().toString());
                String deviceData = result.getDeviceData();

                if (result.getPaymentMethodType() == PaymentMethodType.PAY_WITH_VENMO) {
                    VenmoAccountNonce venmoAccountNonce = (VenmoAccountNonce) result.getPaymentMethodNonce();
                    String venmoUsername = venmoAccountNonce.getUsername();
                }
                // use the result to update your UI and send the payment method nonce to your server
            } else if (resultCode == Activity.RESULT_CANCELED) {
                // the user canceled
            } else {
                // handle errors here, an exception may be available in
                Exception error = (Exception) data.getSerializableExtra(DropInActivity.EXTRA_ERROR);
            }
        } else if (requestCode == EventConstant.EDIT_DETAILS) {
            if (resultCode == Activity.RESULT_OK) {
                this.data = (SomethingInterestingDataPojo) data.getSerializableExtra("event_detail");
                personListArr = (ArrayList<PersonTicketDetailPojo>) data.getSerializableExtra("tickets_detail");
                dataTicket = (TicketDataPojo) data.getSerializableExtra("dataTicket");
                packageData = (EventPackageDetailPojo) data.getSerializableExtra("packageData");
                seats = data.getStringExtra("seats");
                ticketCount = data.getIntExtra("ticketCount", 0);
                displayPersonList();

                // use the result to update your UI and send the payment method nonce to your server
            } else if (resultCode == Activity.RESULT_CANCELED) {
                // the user canceled
            }
        } else if (requestCode == 64207) {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void manageDiscount() {
        if (ticketTotal.compareTo(totalDiscount) >= 0) {
            discount_layout.setVisibility(View.VISIBLE);

            tv_discount_price.setText("$ " + getScaledData(totalDiscount));
            payablePrice = ticketTotal.subtract(totalDiscount);
            tv_total_price.setText("$ " + getScaledData(payablePrice));
            if (couponData != null) {
                remove_coupon.setVisibility(View.VISIBLE);
                apply_coupon.setVisibility(View.GONE);
                SpannableStringBuilder builder = new SpannableStringBuilder();
                builder.append(getColoredString(coupon_code, ContextCompat.getColor(ctx, R.color.button_color)));
                builder.append(ctx.getColoredString("  Coupon Applied", ContextCompat.getColor(ctx, R.color.dark_grey)));
                coupon_value.setText(builder, TextView.BufferType.SPANNABLE);
            }
        } else {
            if (couponData != null) {
                couponData.setSelected(false);
            }
            displayToast(getResources().getString(R.string.coupon_discount));
        }
    }

    private void removeCoupon() {
        remove_coupon.setVisibility(View.INVISIBLE);
        apply_coupon.setVisibility(View.VISIBLE);
        couponData.setSelected(false);
        coupon_value.setText("");
        totalDiscount = new BigDecimal(0.00);
        payablePrice = ticketTotal;
        if (shared) {
            if (data.getShare_discount_type().equals("2")) {
                discountPrice = payablePrice.multiply(new BigDecimal(data.getShare_discount())).divide(new BigDecimal(100));
            } else {
                discountPrice = new BigDecimal(data.getShare_discount());
            }
        }
        totalDiscount = discountPrice;
        payablePrice = ticketTotal.subtract(totalDiscount);
        tv_discount_price.setText("$ " + getScaledData(totalDiscount));
        tv_total_price.setText("$ " + getScaledData(payablePrice));
        if (totalDiscount.compareTo(BigDecimal.ZERO) == 0)
            discount_layout.setVisibility(View.GONE);
    }

    private void sendPaymentNonce(String nonce) {
        if (cd.isConnectingToInternet()) {
            final ProgressDialog d = EventDialogs.showLoading(ctx);
            d.setCanceledOnTouchOutside(false);
            ApiInterface apiService =
                    ApiClient.getClient().create(ApiInterface.class);

            Call<PaymentNonceDataPojo> call = apiService.sendPaymentNonce("sendNonce", nonce, ticketTotal + "", data.getId(), getFromPrefs(EventConstant.USER_ID), package_id, ticket_id, coupon_code, totalDiscount + "", venue_id, payablePrice + "", ticketCount + "", seats, booking_type, is_like_or_share, package_name, data.getOrganizer_name(), ticket_name, coupon_discount + "", discountPrice + "", GenerateJsonObject.getJsonObject(personListArr));
            call.enqueue(new Callback<PaymentNonceDataPojo>() {
                @Override
                public void onResponse(Call<PaymentNonceDataPojo> call, Response<PaymentNonceDataPojo> response) {
                    if (response.body() != null && response.body().getStatus_code().equals("1")) {
                        for (int i = 0; i < EventConstant.ACTIVITIES.size(); i++) {
                            if (EventConstant.ACTIVITIES.get(i) != null)
                                EventConstant.ACTIVITIES.get(i).finish();
                        }
                        displayToast(response.body().getMessage());
                        Intent intent = new Intent(ctx, PlacedOrderDetailActivity.class);
                        intent.putExtra("payment_response", response.body().getResult());
                        startActivity(intent);
                        finish();
                    }
                    d.dismiss();
                }

                @Override
                public void onFailure(Call<PaymentNonceDataPojo> call, Throwable t) {
                    // Log error here since request failed
                    System.out.println("hh failure " + t.getMessage());
                    d.dismiss();
                }
            });
        } else {
            dialog.displayCommonDialog(getResources().getString(R.string.no_internet_connection));
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        this.googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        this.googleMap.getUiSettings().setZoomControlsEnabled(false);
        this.googleMap.getUiSettings().setCompassEnabled(false);
        this.googleMap.getUiSettings().setZoomGesturesEnabled(true);

        updateMapView();
    }

    private void updateMapView() {
        if (googleMap != null) {
            if (data.getLatitude() != null && data.getLongitude() != null) {
                googleMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(data.getLatitude()), Double.parseDouble(data.getLongitude()))).icon(BitmapDescriptorFactory.fromResource(R.mipmap.map_pin))
                        .title(data.getTitle()).snippet(getDate(data.getStart_time())));
                CameraPosition cameraPosition = new CameraPosition.Builder().target(
                        new LatLng(Double.parseDouble(data.getLatitude()), Double.parseDouble(data.getLongitude()))).zoom(15).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 2000, null);

            }
        }
    }

    private String getData(String data) {
        return data.replace(getResources().getString(R.string.dollar), "").trim();
    }

    private BigDecimal getScaledData(BigDecimal num) {
        return num.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public void onBraintreeSubmit() {
        DropInResult.fetchDropInResult(this, getFromPrefs(EventConstant.CLIENT_TOKEN), new DropInResult.DropInResultListener() {
            @Override
            public void onError(Exception exception) {
                // an error occurred
                System.out.println("xhxhxhxhxhx " + exception.getMessage());
            }

            @Override
            public void onResult(DropInResult result) {
                if (result.getPaymentMethodType() != null) {
                    // use the icon and name to show in your UI
                    int icon = result.getPaymentMethodType().getDrawable();
                    int name = result.getPaymentMethodType().getLocalizedName();
                    if (result.getPaymentMethodType() == PaymentMethodType.ANDROID_PAY) {
                        // The last payment method the user used was Android Pay.
                        // The Android Pay flow will need to be performed by the
                        // user again at the time of checkout.
                    } else {
                        // Use the payment method show in your UI and charge the user
                        // at the time of checkout.
                        PaymentMethodNonce paymentMethod = result.getPaymentMethodNonce();
                    }
                } else {
                    // there was no existing payment method
                }
            }
        });
        DropInRequest dropInRequest = new DropInRequest()
                .clientToken(getFromPrefs(EventConstant.CLIENT_TOKEN))
                .collectDeviceData(true)
                .amount(payablePrice + "")
                .androidPayShippingAddressRequired(true)
                .androidPayPhoneNumberRequired(true)
                .requestThreeDSecureVerification(true)
                .androidPayCart(getAndroidPayCart());
        startActivityForResult(dropInRequest.getIntent(this), REQUEST_CODE);

    }

    private Cart getAndroidPayCart() {
        return Cart.newBuilder()
                .setCurrencyCode("USD")
                .setTotalPrice(payablePrice + "")
                .addLineItem(LineItem.newBuilder()
                        .setCurrencyCode("USD")
                        .setDescription("Description")
                        .setQuantity("1")
                        .setUnitPrice(payablePrice + "")
                        .setTotalPrice(payablePrice + "")
                        .build())
                .build();
    }

    @Override
    public void onBackPressed() {
        EventDialogs dialog = new EventDialogs(ctx);
        dialog.displayCommonDialogWithCancel(getResources().getString(R.string.cancel_booking));
    }

    private static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
