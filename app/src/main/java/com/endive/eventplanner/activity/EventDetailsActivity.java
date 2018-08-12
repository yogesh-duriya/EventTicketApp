package com.endive.eventplanner.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.endive.eventplanner.R;
import com.endive.eventplanner.adapter.NothingSelectedSpinnerAdapter;
import com.endive.eventplanner.adapter.TicketCountAdapter;
import com.endive.eventplanner.interfaces.UpdateImageIntercace;
import com.endive.eventplanner.model.ApiClient;
import com.endive.eventplanner.model.ApiInterface;
import com.endive.eventplanner.pojo.EventDetailPojo;
import com.endive.eventplanner.pojo.EventPackageDetailPojo;
import com.endive.eventplanner.pojo.SomethingInterestingDataPojo;
import com.endive.eventplanner.pojo.TicketCountPojo;
import com.endive.eventplanner.pojo.TicketDataPojo;
import com.endive.eventplanner.pojo.VenueDataPojo;
import com.endive.eventplanner.util.ConnectionDetector;
import com.endive.eventplanner.util.EventConstant;
import com.endive.eventplanner.util.EventDialogs;
import com.endive.eventplanner.util.ViewAnimationUtils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventDetailsActivity extends BaseActivity implements View.OnClickListener, UpdateImageIntercace, OnMapReadyCallback {

    private LinearLayout recommended_events;

    private EventDetailsActivity ctx = this;
    private ConnectionDetector cd;
    private EventDialogs dialog;
    private EventDialogs dialogErr;
    public SomethingInterestingDataPojo data;
    private ImageView event_image, heart, share, cart;
    private TextView type, event_date, event_name, organizer_name, event_location;
    private ScrollView scrollview;
    private ArrayList<TicketCountPojo> countArr;
    private ArrayList<TicketDataPojo> ticketArr;
    private ArrayList<EventPackageDetailPojo> packageArr;
    private TicketCountAdapter countAdapter;
    private int ticketCount = 0;
    private ImageView discount;
    private ImageView no_of_person;
    private LinearLayout buy_now;

    private LinearLayout desc_layout, desc, tnc_layout, tnc, map_layout, map_view;
    private ImageView desc_down, tnc_down, map_down;
    private TextView desc_data, tnc_data;

    private GoogleMap googleMap;
    private ImageView transparent_image;

    private ArrayList<VenueDataPojo> venueArr;

    private LayoutInflater mInflator;
    private Spinner venue;
    private boolean is_ticket = false;
    private String from;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        data = (SomethingInterestingDataPojo) getIntent().getSerializableExtra("data");
        EventConstant.ACTIVITIES.add(ctx);
        setHeader(data.getTitle());
        initialize();
        setListeners();
    }

    private void initialize() {
        desc_layout = (LinearLayout) findViewById(R.id.desc_layout);
        desc = (LinearLayout) findViewById(R.id.desc);
        tnc_layout = (LinearLayout) findViewById(R.id.tnc_layout);
        tnc = (LinearLayout) findViewById(R.id.tnc);
        map_layout = (LinearLayout) findViewById(R.id.map_layout);
        map_view = (LinearLayout) findViewById(R.id.map_view);
        desc_down = (ImageView) findViewById(R.id.desc_down);
        tnc_down = (ImageView) findViewById(R.id.tnc_down);
        map_down = (ImageView) findViewById(R.id.map_down);
        desc_data = (TextView) findViewById(R.id.desc_data);
        tnc_data = (TextView) findViewById(R.id.tnc_data);

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map_data);
        mapFragment.getMapAsync(this);
        transparent_image = (ImageView) findViewById(R.id.transparent_image);

        mInflator = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

        scrollview = (ScrollView) findViewById(R.id.scrollview);
        from = getIntent().getStringExtra("from");

        ticketArr = new ArrayList<>();
        packageArr = new ArrayList<>();
        countArr = new ArrayList<>();
        for (int i = 10; i >= 1; i--) {
            TicketCountPojo obj = new TicketCountPojo();
            obj.setCount("" + i);
            obj.setSelected(false);
            countArr.add(obj);
        }

        countAdapter = new TicketCountAdapter(ctx, countArr, "eventDetail");

        discount = (ImageView) findViewById(R.id.discount);
        dialog = new EventDialogs(ctx);
        buy_now = (LinearLayout) findViewById(R.id.buy_now);
        type = (TextView) findViewById(R.id.type);
        event_date = (TextView) findViewById(R.id.event_date);
        event_name = (TextView) findViewById(R.id.event_name);
        organizer_name = (TextView) findViewById(R.id.organizer_name);
        event_location = (TextView) findViewById(R.id.event_location);
        event_image = (ImageView) findViewById(R.id.event_image);
        heart = (ImageView) findViewById(R.id.heart);
        share = (ImageView) findViewById(R.id.share);
        cart = (ImageView) findViewById(R.id.cart);
        recommended_events = (LinearLayout) findViewById(R.id.recommended_events);
        cd = new ConnectionDetector(ctx);
        dialogErr = new EventDialogs(ctx);

        if (from != null && from.equalsIgnoreCase("past")) {
            buy_now.setVisibility(View.GONE);
            recommended_events.setVisibility(View.GONE);
        }

        getEventDetails();
        setData();
    }

    private void setData() {
        setImageInLayout(ctx, 600, (int) getResources().getDimension(R.dimen.home_row_layout_height), data.getImage(), event_image);
        event_name.setText(data.getTitle());
        event_date.setText(getResources().getString(R.string.from) + ": " + getDate(data.getStart_time()) + " - " + getDate(data.getEnd_time()));
//        event_date.setText("From: " + getDateDetail(data.getStart_time()) + ", To: " + getDateDetail(data.getEnd_time()));
        event_location.setText(data.getVenue());
        if (data.getEvent_type_id().equals("1")) {
            type.setText(getResources().getString(R.string.free));
        } else {
            type.setText(getResources().getString(R.string.paid));
        }

        setHeartImage(data.isIs_favourite());

        SpannableStringBuilder builder = new SpannableStringBuilder();

        builder.append(getColoredString("By ", ContextCompat.getColor(ctx, R.color.hint_color)));
        if (data.getOrganizer_name() != null && !data.getOrganizer_name().equals(""))
            builder.append(getColoredString(data.getOrganizer_name(), ContextCompat.getColor(ctx, R.color.button_color)));

        organizer_name.setText(builder, TextView.BufferType.SPANNABLE);

        organizer_name.setVisibility(View.VISIBLE);

//        tags_list.removeAllViews();
//        LinearLayout topLinearLayout = new LinearLayout(ctx);
//        topLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
//        topLinearLayout.setPadding(5, 5, 5, 5);
//        if (data.getTags() != null)
//            setTagList(topLinearLayout, data);
//
//        tags_list.addView(topLinearLayout);

        if (data != null) {
            tnc_data.setText(Html.fromHtml(data.getTerms_condition()));
            desc_data.setText(Html.fromHtml(data.getDescription()));
            if (data.getIs_social_media_discount().equals("0"))
                discount.setVisibility(View.GONE);
        }
    }

    private void setHeartImage(boolean value) {
        if (value)
            heart.setImageResource(R.mipmap.map_heart_list_hover);
        else
            heart.setImageResource(R.mipmap.map_heart_list);
    }

    private void setListeners() {
//        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//
//            @Override
//            public void onPageSelected(int position) {
//                setStyle(position);
//            }
//
//            @Override
//            public void onPageScrolled(int arg0, float arg1, int arg2) {
//                viewPager.getParent().requestDisallowInterceptTouchEvent(true);
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int arg0) {
//            }
//        });

//        viewPager.setOnTouchListener(new View.OnTouchListener() {
//
//            int dragthreshold = 30;
//            int downX;
//            int downY;
//
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
//                        downX = (int) event.getRawX();
//                        downY = (int) event.getRawY();
//                        break;
//                    case MotionEvent.ACTION_MOVE:
//                        int distanceX = Math.abs((int) event.getRawX() - downX);
//                        int distanceY = Math.abs((int) event.getRawY() - downY);
//
//                        if (distanceY > distanceX && distanceY > dragthreshold) {
//                            viewPager.getParent().requestDisallowInterceptTouchEvent(false);
//                            scrollview.getParent().requestDisallowInterceptTouchEvent(true);
//                        } else if (distanceX > distanceY && distanceX > dragthreshold) {
//                            viewPager.getParent().requestDisallowInterceptTouchEvent(true);
//                            scrollview.getParent().requestDisallowInterceptTouchEvent(false);
//                        }
//                        break;
//                    case MotionEvent.ACTION_UP:
//                        scrollview.getParent().requestDisallowInterceptTouchEvent(false);
//                        viewPager.getParent().requestDisallowInterceptTouchEvent(false);
//                        break;
//                }
//                return false;
//            }
//        });
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

        heart.setOnClickListener(this);
        share.setOnClickListener(this);
        discount.setOnClickListener(this);
        recommended_events.setOnClickListener(this);
        organizer_name.setOnClickListener(this);
        buy_now.setOnClickListener(this);

        event_location.setOnClickListener(this);

        desc_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                descLayoutClick();
            }
        });

        map_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapLayoutClick();
            }
        });

        tnc_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tncLayoutClick();
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.heart:
                if (!getFromPrefs(EventConstant.USER_ID).equals("")) {
                    setFavorite(data, "SetFavourite", this);
                } else {
                    Intent intent = new Intent(ctx, LoginActivity.class);
                    intent.putExtra("from", "home");
                    startActivity(intent);
                }
                break;

            case R.id.share:
                displaySharingIntent(data);
                break;

            case R.id.organizer_name:
                Intent intent = new Intent(ctx, OrganizeProfileActivity.class);
                intent.putExtra("id", data.getOrganizer_id());
                startActivity(intent);
                break;

            case R.id.buy_ticket:
                TicketDataPojo dataTicket = (TicketDataPojo) v.getTag(R.string.data);
                is_ticket = true;
                displayPurchaseTicketDialog(dataTicket, null);
                break;

            case R.id.buy_now:
                is_ticket = true;
                if (ticketArr != null && ticketArr.size() > 0) {
                    Intent intentBuy = new Intent(ctx, BuyTicketActivity.class);
                    intentBuy.putExtra("data", data);
                    intentBuy.putExtra("packageArr", packageArr);
                    intentBuy.putExtra("ticketArr", ticketArr);
                    intentBuy.putExtra("venueArr", venueArr);
                    startActivity(intentBuy);
                } else {
                    TicketDataPojo dataTicketDetail = null;
                    displayPurchaseTicketDialog(dataTicketDetail, null);
                }
                break;

            case R.id.number_of_ticket:
                TicketCountPojo obj = (TicketCountPojo) v.getTag(R.string.data);
                for (int i = 0; i < 10; i++)
                    countArr.get(i).setSelected(false);
                obj.setSelected(true);
                ticketCount = Integer.parseInt(obj.getCount());
                displayImage(ticketCount);
                countAdapter.notifyDataSetChanged();
                break;

            case R.id.discount:
                String like_discount;
                String share_discount;
                String like_share_discount;
                if (data.getShare_discount_type().equals("1")) {
                    share_discount = getResources().getString(R.string.dollar) + " " + data.getShare_discount();
                } else {
                    share_discount = data.getShare_discount() + "%";
                }
                if (data.getLike_discount_type().equals("1")) {
                    like_discount = getResources().getString(R.string.dollar) + " " + data.getLike_discount();
                } else {
                    like_discount = data.getLike_discount() + "%";
                }
                if (data.getLike_share_discount_type().equals("1")) {
                    like_share_discount = getResources().getString(R.string.dollar) + " " + data.getLike_share_discount();
                } else {
                    like_share_discount = data.getLike_share_discount() + "%";
                }
                dialog.displayCommonDialog(getResources().getString(R.string.like_discount)+" " + like_discount + "\n" + getResources().getString(R.string.share_discount)+" " + share_discount + "\n" + getResources().getString(R.string.like_share)+" " + like_share_discount);
                break;

            case R.id.event_location:
                if (data.getLatitude() != null && data.getTitle() != null && !data.getLatitude().equals("") && !data.getLongitude().equals(""))
                    loadGoogleMap(this.data.getLatitude(), this.data.getLongitude(), this.data.getTitle());
                break;

            case R.id.recommended_events:
                removeActivity(getResources().getString(R.string.package_name) + ".RecommendedEventsActivity");
                Intent intentRec = new Intent(ctx, RecommendedEventsActivity.class);
                intentRec.putExtra("data", this.data);
                startActivity(intentRec);
                break;

            case R.id.tv_buy_now:
                is_ticket = false;
                EventPackageDetailPojo package_list = (EventPackageDetailPojo) v.getTag(R.string.packageList);
                TicketDataPojo dataTicketDetail = null;
                displayPurchaseTicketDialog(dataTicketDetail, package_list);
                break;

        }
    }


    private void descLayoutClick() {
        if (desc.getVisibility() == View.GONE) {
            ViewAnimationUtils.expand(desc);
            setArrow(desc_down, true);
        } else {
            ViewAnimationUtils.collapse(desc);
            setArrow(desc_down, false);
        }
    }

    private void mapLayoutClick() {
        if (map_view.getVisibility() == View.GONE) {
            ViewAnimationUtils.expand(map_view);
            setArrow(map_down, true);
        } else {
            ViewAnimationUtils.collapse(map_view);
            setArrow(map_down, false);
        }
    }

    private void tncLayoutClick() {
        if (tnc.getVisibility() == View.GONE) {
            ViewAnimationUtils.expand(tnc);
            setArrow(tnc_down, true);
        } else {
            ViewAnimationUtils.collapse(tnc);
            setArrow(tnc_down, false);
        }
    }

    private void setArrow(ImageView image, boolean check) {
        if (check)
            image.setImageResource(R.mipmap.upperarrow);
        else
            image.setImageResource(R.mipmap.downarrow_grey);
    }

    private void loadGoogleMap(String lat, String lng, String label) {
        double latitude = Float.parseFloat(lat);
        double longitude = Float.parseFloat(lng);
        String uriBegin = "geo:" + latitude + "," + longitude;
        String query = latitude + "," + longitude + "(" + label + ")";
        String encodedQuery = Uri.encode(query);
        String uriString = uriBegin + "?q=" + encodedQuery + "&z=16";
        Uri uri = Uri.parse(uriString);
        Intent intentMapView = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intentMapView);
    }

    private void displayImage(int ticketCount) {
        if (ticketCount == 1)
            no_of_person.setBackground(ResourcesCompat.getDrawable(ctx.getResources(), R.mipmap.one_person, null));
        else if (ticketCount == 2)
            no_of_person.setBackground(ResourcesCompat.getDrawable(ctx.getResources(), R.mipmap.two_person, null));
        else if (ticketCount == 3)
            no_of_person.setBackground(ResourcesCompat.getDrawable(ctx.getResources(), R.mipmap.three_person, null));
        else if (ticketCount == 4)
            no_of_person.setBackground(ResourcesCompat.getDrawable(ctx.getResources(), R.mipmap.four_person, null));
        else if (ticketCount == 5)
            no_of_person.setBackground(ResourcesCompat.getDrawable(ctx.getResources(), R.mipmap.five_person, null));
        else if (ticketCount == 6)
            no_of_person.setBackground(ResourcesCompat.getDrawable(ctx.getResources(), R.mipmap.six_person, null));
        else if (ticketCount == 7)
            no_of_person.setBackground(ResourcesCompat.getDrawable(ctx.getResources(), R.mipmap.seven_person, null));
        else if (ticketCount == 8)
            no_of_person.setBackground(ResourcesCompat.getDrawable(ctx.getResources(), R.mipmap.eight_person, null));
        else if (ticketCount == 9)
            no_of_person.setBackground(ResourcesCompat.getDrawable(ctx.getResources(), R.mipmap.nine_person, null));
        else if (ticketCount == 10)
            no_of_person.setBackground(ResourcesCompat.getDrawable(ctx.getResources(), R.mipmap.ten_person, null));
    }

    private void displayPurchaseTicketDialog(final TicketDataPojo dataTicket, final EventPackageDetailPojo package_list) {
        LinearLayout proceed;
        final Dialog dialog = new Dialog(ctx, android.R.style.Theme_Translucent_NoTitleBar);
        dialog.setContentView(R.layout.custom_dialog_book_ticket);

        RecyclerView ticket_count_list = (RecyclerView) dialog.findViewById(R.id.ticket_count_list);
        ticket_count_list.setHasFixedSize(true);
        no_of_person = (ImageView) dialog.findViewById(R.id.no_of_person);

        TextView dialog_header = (TextView) dialog.findViewById(R.id.dialog_header);

        venue = (Spinner) dialog.findViewById(R.id.venue);
        venue.setPrompt(getResources().getString(R.string.select_venue));

        venue.setAdapter(
                new NothingSelectedSpinnerAdapter(
                        typeSpinnerAdapter,
                        R.layout.spinner_nothing_selected,
                        this));

        if (!is_ticket) {
            ticket_count_list.setVisibility(View.INVISIBLE);
            no_of_person.setVisibility(View.GONE);
            dialog_header.setText(getResources().getString(R.string.select_venue));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(0, 0, 0, 0);
            ticket_count_list.setLayoutParams(params);
        } else {
            dialog_header.setText(getResources().getString(R.string.how_many_ticket));
            ticket_count_list.setVisibility(View.VISIBLE);
            no_of_person.setVisibility(View.VISIBLE);
        }

        ticket_count_list.setAdapter(countAdapter);
        ticket_count_list.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true));

        proceed = (LinearLayout) dialog.findViewById(R.id.proceed);
        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = venue.getSelectedItemPosition();
                if (pos > 0) {
                    if (is_ticket) {
                        if (ticketCount > 0) {
                            dialog.dismiss();
                            if (data.isIs_seating_arrangement()) {
                                Intent intent = new Intent(ctx, WebUrlActivity.class);
                                intent.putExtra("ticketCount", ticketCount);
                                intent.putExtra("event_detail", data);
                                intent.putExtra("venue_id", venueArr.get(pos - 1).getId());
                                intent.putExtra("dataTicket", dataTicket);
                                intent.putExtra("header", getResources().getString(R.string.header_select_seats));
                                startActivity(intent);
                            } else {
                                Intent intent = new Intent(ctx, FillTicketDetailsActivity.class);
                                intent.putExtra("ticketCount", ticketCount);
                                intent.putExtra("event_detail", data);
                                intent.putExtra("venue_id", venueArr.get(pos - 1).getId());
                                intent.putExtra("dataTicket", dataTicket);
                                intent.putExtra("seats", "");
                                intent.putExtra("packageData", "");
                                startActivity(intent);
                            }
                        } else {
                            dialogErr.displayCommonDialog(getResources().getString(R.string.ticket_quantity_msg));
                        }
                    } else {
                        dialog.dismiss();
                        Intent package_detail = new Intent(ctx, PackageDetailActivity.class);
                        package_detail.putExtra("package_list", package_list);
                        package_detail.putExtra("venue_id", venueArr.get(pos - 1).getId());
                        package_detail.putExtra("event_detail", data);
                        startActivity(package_detail);
                    }
                } else {
                    dialogErr.displayCommonDialog(getResources().getString(R.string.venue_msg));
                }
            }
        });
        displayImage(ticketCount);
        ImageView close_dialog = (ImageView) dialog.findViewById(R.id.close_dialog);
        close_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        if (ctx != null && !ctx.isFinishing())
            dialog.show();
    }

    private SpinnerAdapter typeSpinnerAdapter = new BaseAdapter() {

        private TextView text;

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = mInflator.inflate(R.layout.row_spinner, null);
            }
            text = (TextView) convertView.findViewById(R.id.spinnerTarget);
            text.setText(venueArr.get(position).getVenue_name());
            return convertView;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public Object getItem(int position) {
            return venueArr.get(position).getVenue_name();
        }

        @Override
        public int getCount() {
            if (venueArr != null)
                return venueArr.size();
            else return 0;
        }

        public View getDropDownView(final int position, View convertView,
                                    ViewGroup parent) {
            if (convertView == null) {
                convertView = mInflator.inflate(
                        R.layout.row_spinner, null);
            }
            text = (TextView) convertView.findViewById(R.id.spinnerTarget);
            text.setText(venueArr.get(position).getVenue_name());
            return convertView;
        }
    };

    private void getEventDetails() {
        if (cd.isConnectingToInternet()) {
            ApiInterface apiService =
                    ApiClient.getClient().create(ApiInterface.class);

            Call<EventDetailPojo> call = apiService.eventDetails("EventDetail", getFromPrefs(EventConstant.USER_ID), data.getId());
            call.enqueue(new Callback<EventDetailPojo>() {
                @Override
                public void onResponse(Call<EventDetailPojo> call, Response<EventDetailPojo> response) {
                    if (response.body() != null && response.body().getStatus_code().equals("1")) {
                        ticketArr = response.body().getTicket_detail();
                        packageArr = response.body().getPackage_detail().getEventpackage_detail();
                        venueArr = response.body().getVenue_detail();
                    }
                }

                @Override
                public void onFailure(Call<EventDetailPojo> call, Throwable t) {
                    // Log error here since request failed
                    System.out.println("retrofit hh failure " + t.getMessage());
                }
            });
        }
    }

    @Override
    public void onSuccess(@NonNull String value) {
        setHeartImage(data.isIs_favourite());
    }

    @Override
    public void onError(@NonNull Throwable throwable) {

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("data", data);
        setResult(Activity.RESULT_OK, intent);
        finish();
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
            if (data.getLatitude() != null && data.getLongitude() != null && !data.getLatitude().equals("") && !data.getLongitude().equals("")) {
                googleMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(data.getLatitude()), Double.parseDouble(data.getLongitude()))).icon(BitmapDescriptorFactory.fromResource(R.mipmap.map_pin))
                        .title(data.getTitle()).snippet(getDate(data.getStart_time())));
                CameraPosition cameraPosition = new CameraPosition.Builder().target(
                        new LatLng(Double.parseDouble(data.getLatitude()), Double.parseDouble(data.getLongitude()))).zoom(15).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 2000, null);

            }
        }
    }
}
