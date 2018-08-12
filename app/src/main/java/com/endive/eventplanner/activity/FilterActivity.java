package com.endive.eventplanner.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.exceptions.OutOfDateRangeException;
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.endive.eventplanner.R;
import com.endive.eventplanner.adapter.CategoriesAdapter;
import com.endive.eventplanner.adapter.LocationAdapter;
import com.endive.eventplanner.pojo.CategoryListPojo;
import com.endive.eventplanner.pojo.EventListPojo;
import com.endive.eventplanner.pojo.FilterPojo;
import com.endive.eventplanner.pojo.LocationDataPojo;
import com.endive.eventplanner.util.EventConstant;
import com.endive.eventplanner.util.EventDialogs;
import com.endive.eventplanner.util.ViewAnimationUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class FilterActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout sort_by_layout, sort_by_option, relevance, date;
    private LinearLayout event_type_layout, event_type_option;
    private LinearLayout event_categories_layout, event_categories_option;
    private LinearLayout event_locations_layout, event_locations_option;
    private LinearLayout price_layout, price_option;
    private LinearLayout date_layout, date_option;
    private ImageView sort_by_down, relevance_circle, date_circle, price_down, event_categories_down, event_locations_down, date_down;
    private ImageView event_type_down;
    private FilterPojo obj;
    private TextView apply, clear;
    private ImageView cross;
    private RecyclerView events_list;
    private RecyclerView category_list;
    private RecyclerView location_list;
    private CategoriesAdapter adapterCategory = null;
    private CategoriesAdapter adapterEventType = null;
    private LocationAdapter adapterLocation = null;
    private boolean relevance_selected, date_selected;
    private FilterActivity ctx = this;
    private TextView low_price, high_price;
    private CrystalRangeSeekbar price_seekbar;
    private EventDialogs dialog;
    private String startDate = null;
    private ArrayList<LocationDataPojo> arr;
    private EditText search_location;

    private Calendar calendar;
    private int year;
    private int month;
    private int day;
    private Calendar min;

    private CalendarView calendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        initialize();

        setListener();
    }

    private void initialize() {
        events_list = (RecyclerView) findViewById(R.id.events_list);
        category_list = (RecyclerView) findViewById(R.id.category_list);
        location_list = (RecyclerView) findViewById(R.id.location_list);
        setRecyclerLayoutManager(events_list);
        setRecyclerLayoutManager(category_list);
        setRecyclerLayoutManager(location_list);
        calendar = Calendar.getInstance();

        min = Calendar.getInstance();
//        min.add(Calendar.DAY_OF_MONTH, 0);

        Calendar max = Calendar.getInstance();
        max.add(Calendar.DAY_OF_MONTH, 1000);

        calendarView = (CalendarView) findViewById(R.id.calendarView);

        calendarView.setMinimumDate(Calendar.getInstance());
        calendarView.setMaximumDate(max);

        sort_by_layout = (LinearLayout) findViewById(R.id.sort_by_layout);
        sort_by_option = (LinearLayout) findViewById(R.id.sort_by_option);
        relevance = (LinearLayout) findViewById(R.id.relevance);
        date = (LinearLayout) findViewById(R.id.date);
        event_type_layout = (LinearLayout) findViewById(R.id.event_type_layout);
        event_type_option = (LinearLayout) findViewById(R.id.event_type_option);
        event_categories_layout = (LinearLayout) findViewById(R.id.event_categories_layout);
        event_categories_option = (LinearLayout) findViewById(R.id.event_categories_option);
        event_locations_layout = (LinearLayout) findViewById(R.id.event_locations_layout);
        event_locations_option = (LinearLayout) findViewById(R.id.event_locations_option);
        price_layout = (LinearLayout) findViewById(R.id.price_layout);
        price_option = (LinearLayout) findViewById(R.id.price_option);
        date_layout = (LinearLayout) findViewById(R.id.date_layout);
        date_option = (LinearLayout) findViewById(R.id.date_option);
        sort_by_down = (ImageView) findViewById(R.id.sort_by_down);
        event_categories_down = (ImageView) findViewById(R.id.event_categories_down);
        date_circle = (ImageView) findViewById(R.id.date_circle);
        relevance_circle = (ImageView) findViewById(R.id.relevance_circle);
        event_type_down = (ImageView) findViewById(R.id.event_type_down);
        price_down = (ImageView) findViewById(R.id.price_down);
        date_down = (ImageView) findViewById(R.id.date_down);
        event_locations_down = (ImageView) findViewById(R.id.event_locations_down);
        apply = (TextView) findViewById(R.id.apply);
        clear = (TextView) findViewById(R.id.clear);
        cross = (ImageView) findViewById(R.id.cross);
        price_seekbar = (CrystalRangeSeekbar) findViewById(R.id.price_seekbar);
        low_price = (TextView) findViewById(R.id.low_price);
        high_price = (TextView) findViewById(R.id.high_price);
        search_location = (EditText) findViewById(R.id.search_location);

        dialog = new EventDialogs(ctx);
        startDate = null;
        obj = getFilterData();
        price_seekbar.setMinStartValue(Integer.parseInt(obj.getPrice_min())).setMaxStartValue(Integer.parseInt(obj.getPrice_max())).apply();
        if (obj == null)
            obj = new FilterPojo();
        if (obj.getData() == null)
            obj.setData(getCategoryData().getData());
        if (obj.getLocationArr() == null) {
            arr = new ArrayList<>();
            LocationDataPojo objLoc = new LocationDataPojo();
            objLoc.setCity("All Locations");
            objLoc.setSelected(true);
            arr.add(objLoc);
            arr.addAll(getLocationData().getData());
            obj.setLocationArr(arr);
        }
        if (obj.getData() != null) {
            adapterCategory = new CategoriesAdapter(ctx, obj.getData(), "filter", "category");
            adapterEventType = new CategoriesAdapter(ctx, obj.getData(), "filter", "event");

            events_list.setAdapter(adapterEventType);
            category_list.setAdapter(adapterCategory);

            setLocationAdapter(obj.getLocationArr());
        }

        getFilterValues();
    }


    private void setListener() {
        cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSoftKeyboard();
                finish();
            }
        });

        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSoftKeyboard();
                setFilterValues();
            }
        });
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSoftKeyboard();
                saveFilterData(new FilterPojo());
//                initialize();
                Intent intent = new Intent(ctx, FilterActivity.class);
                startActivity(intent);
                finish();
            }
        });
        price_seekbar.setMinStartValue(Integer.parseInt(obj.getPrice_min())).setMaxStartValue(Integer.parseInt(obj.getPrice_max())).apply();
        price_seekbar.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                low_price.setText(getResources().getString(R.string.dollar) + minValue);
                high_price.setText(getResources().getString(R.string.dollar) + maxValue);
                obj.setPrice_max(maxValue + "");
                obj.setPrice_min(minValue + "");
            }
        });

        search_location.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ArrayList arrFilter = new ArrayList<>();
                if (arr != null) {
                    for (int i = 0; i < arr.size(); i++) {
                        if (arr.get(i).getCity().toLowerCase().contains(s.toString().toLowerCase()))
                            arrFilter.add(arr.get(i));
                    }
                }
                setLocationAdapter(arrFilter);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        sort_by_layout.setOnClickListener(this);
        event_type_layout.setOnClickListener(this);
        event_categories_layout.setOnClickListener(this);
        event_locations_layout.setOnClickListener(this);
        relevance.setOnClickListener(this);
        date.setOnClickListener(this);
        price_layout.setOnClickListener(this);
        date_layout.setOnClickListener(this);
    }

    private void setLocationAdapter(ArrayList<LocationDataPojo> arrLoc) {
        adapterLocation = new LocationAdapter(ctx, arrLoc);
        location_list.setAdapter(adapterLocation);
        adapterLocation.notifyDataSetChanged();
    }

    private void setImage(ImageView selected, ImageView unSelected) {
        selected.setImageResource(R.mipmap.circle_colour);
        unSelected.setImageResource(R.mipmap.circle);
    }

    private void setArrow(ImageView image, boolean check) {
        if (check)
            image.setImageResource(R.mipmap.upperarrow);
        else
            image.setImageResource(R.mipmap.downarrow_grey);
    }

    private void getFilterValues() {
        relevance_selected = obj.isRelevance();
        date_selected = obj.isDate();
        startDate = obj.getStartDate();
        if (startDate != null) {
            String date[] = startDate.split("/");
            year = Integer.parseInt(date[0]);
            month = Integer.parseInt(date[1]) - 1;
            day = Integer.parseInt(date[2]);

            calendar.set(year, month, day);
            try {
                calendarView.setDate(calendar);
            } catch (OutOfDateRangeException e) {
                e.printStackTrace();
            }
        }

        if (relevance_selected)
            setImage(relevance_circle, date_circle);
        if (date_selected)
            setImage(date_circle, relevance_circle);
    }

    private void setFilterValues() {
        Date selectedDate = calendarView.getFirstSelectedDate().getTime();
        startDate = getDate(selectedDate);
        obj.setDate(date_selected);
        obj.setRelevance(relevance_selected);
        if (!startDate.equals(getDate(min.getTime())))
            obj.setStartDate(startDate);
        saveBooleanIntoPrefs(EventConstant.REFRESH_REQUIRED, true);
        saveFilterData(obj);
        finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sort_by_layout:
                sortByLayoutClick();
                break;

            case R.id.event_type_layout:
                eventTypeLayoutClick();
                break;

            case R.id.event_categories_layout:
                eventCategoriesLayoutClick();
                break;

            case R.id.event_locations_layout:
                eventLocationsLayoutClick();
                break;

            case R.id.price_layout:
                priceLayoutClick();
                break;

            case R.id.date_layout:
                dateLayoutClick();
                break;

            case R.id.root_layout_categories:
                String type = (String) view.getTag(R.string.type);
                if (type != null && type.equals("event")) {
                    EventListPojo obj = (EventListPojo) view.getTag(R.string.data);
                    if (obj.is_selected()) {
                        obj.setIs_selected(false);
                    } else {
                        for (int i = 0; i < this.obj.getData().getEvent_type().size(); i++) {
                            this.obj.getData().getEvent_type().get(i).setIs_selected(false);
                        }
                        obj.setIs_selected(true);
                    }
                    adapterEventType.notifyDataSetChanged();
                } else if (type != null && type.equals("category")) {
                    CategoryListPojo categoryData = (CategoryListPojo) view.getTag(R.string.data);
                    if (categoryData.is_selected()) {
                        categoryData.setIs_selected(false);
                    } else {
                        for (int i = 0; i < this.obj.getData().getCategory().size(); i++) {
                            this.obj.getData().getCategory().get(i).setIs_selected(false);
                        }
                        categoryData.setIs_selected(true);
                    }
                    adapterCategory.notifyDataSetChanged();
                } else {
                    LocationDataPojo locationData = (LocationDataPojo) view.getTag(R.string.data);
                    if (locationData.isSelected()) {
                        locationData.setSelected(false);
                    } else {
                        for (int i = 0; i < this.obj.getLocationArr().size(); i++) {
                            this.obj.getLocationArr().get(i).setSelected(false);
                        }
                        locationData.setSelected(true);
                    }
                    adapterLocation.notifyDataSetChanged();
                }
                break;

            case R.id.relevance:
                setImage(relevance_circle, date_circle);
                relevance_selected = true;
                date_selected = false;
                break;

            case R.id.date:
                setImage(date_circle, relevance_circle);
                relevance_selected = false;
                date_selected = true;
                break;

            default:
                break;
        }
    }

    private void sortByLayoutClick() {
        if (sort_by_option.getVisibility() == View.GONE) {
            ViewAnimationUtils.expand(sort_by_option);
            setArrow(sort_by_down, true);
        } else {
            ViewAnimationUtils.collapse(sort_by_option);
            setArrow(sort_by_down, false);
        }
    }

    private void eventTypeLayoutClick() {
        if (event_type_option.getVisibility() == View.GONE) {
            ViewAnimationUtils.expand(event_type_option);
            setArrow(event_type_down, true);
        } else {
            ViewAnimationUtils.collapse(event_type_option);
            setArrow(event_type_down, false);
        }
    }

    private void eventCategoriesLayoutClick() {
        if (event_categories_option.getVisibility() == View.GONE) {
            ViewAnimationUtils.expand(event_categories_option);
            setArrow(event_categories_down, true);
        } else {
            ViewAnimationUtils.collapse(event_categories_option);
            setArrow(event_categories_down, false);
        }
    }

    private void eventLocationsLayoutClick() {
        if (event_locations_option.getVisibility() == View.GONE) {
            ViewAnimationUtils.expand(event_locations_option);
            setArrow(event_locations_down, true);
        } else {
            ViewAnimationUtils.collapse(event_locations_option);
            setArrow(event_locations_down, false);
        }
    }

    private void priceLayoutClick() {
        if (price_option.getVisibility() == View.GONE) {
            ViewAnimationUtils.expand(price_option);
            setArrow(price_down, true);
        } else {
            ViewAnimationUtils.collapse(price_option);
            setArrow(price_down, false);
        }
    }

    private void dateLayoutClick() {
        if (date_option.getVisibility() == View.GONE) {
            ViewAnimationUtils.expand(date_option);
            setArrow(date_down, true);
        } else {
            ViewAnimationUtils.collapse(date_option);
            setArrow(date_down, false);
        }
    }

//    private void call() {
//        int size = calendar_view.getSelectedDates().size();
//        if (size > 0) {
//            startDate = calendar_view.getSelectedDates().get(0);
//            endDate = calendar_view.getSelectedDates().get(size - 1);
//        }
//    }
}
