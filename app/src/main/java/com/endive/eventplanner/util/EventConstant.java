package com.endive.eventplanner.util;

import android.app.Activity;

import java.util.ArrayList;

/**
 * Created by arpit.jain on 10/6/2017.
 */

public class EventConstant {

//    public final static String BASE_URL = "http://192.168.1.4/sites/eventmanagement/api/";
//    public final static String BASE_URL = "http://in.endivesoftware.com/sites/eventmanagement/api/";
//public final static String SEAT_BASE_URL = "http://in.endivesoftware.com/sites/eventmanagement/mobile-api/event/";
    public final static String BASE_URL = "http://in.endivesoftware.com/sites/eventmanagementuat/api/";
    public final static String SEAT_BASE_URL = "http://in.endivesoftware.com/sites/eventmanagementuat/mobile-api/event/";
    public final static String USER_ROLE_VAL = "1";
    public final static String PREF_NAME = "com.harry.event.prefs";
    public final static String DEFAULT_VALUE = "";
    public final static String DEVICE_ID = "device_id";
    public final static String USER_ID = "user_id";
    public final static String NAME = "name";
    public final static String USER_ROLE = "user_role";
    public final static String IMAGE = "image";
    public final static String ANDROID_ID = "Android_ID";
    public final static String LOGGEDINFROM = "logged_in_from";
    public final static String REFRESH_REQUIRED = "refresh";
    public static final String CLIENT_TOKEN = "CLIENT_TOKEN";
    public static final String EMAIL_REGEX = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}";
    public static final String PASSWORD_REGEX = "(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{6,}$";
    public static ArrayList<Activity> ACTIVITIES = new ArrayList<>();
    public static final int START_SIGN_UP = 1;
    public static final int START_MAP = 2;
    public static final int SHOW_DETAIL = 3;
    public static final int EDIT_DETAILS = 4;
}
