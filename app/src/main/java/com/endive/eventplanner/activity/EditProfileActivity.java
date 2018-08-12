package com.endive.eventplanner.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.endive.eventplanner.R;
import com.endive.eventplanner.model.ApiClient;
import com.endive.eventplanner.model.ApiInterface;
import com.endive.eventplanner.model.FileUploadSer;
import com.endive.eventplanner.model.ServiceGenerator;
import com.endive.eventplanner.pojo.CountriesDataPojo;
import com.endive.eventplanner.pojo.CountriesPojo;
import com.endive.eventplanner.pojo.LoginDataPojo;
import com.endive.eventplanner.pojo.LoginPojo;
import com.endive.eventplanner.util.ConnectionDetector;
import com.endive.eventplanner.util.EventConstant;
import com.endive.eventplanner.util.EventDialogs;
import com.endive.eventplanner.util.ImageLoadingUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class EditProfileActivity extends BaseActivity {

    private EditText first_name, last_name, email, address, city, state, zip_code, dob, password, confirm_password, mobile;
    private LinearLayout male, female, other, submit;
    private ImageView radio_male, radio_female, radio_other, profile_image, cross_dialog;
    private String gender = "1";
    private EditProfileActivity ctx = this;
    private EventDialogs dialog = null;
    private ConnectionDetector cd = null;
    private ArrayList<CountriesDataPojo> countriesArr;
    private Spinner country;
    private String countryVal, dobVal;
    private LayoutInflater mInflator;
    private boolean selected;
    private String countryId;
    private Calendar calendar;
    private Dialog image_picker_dialog;
    private int year;
    private int month;
    private int day;
    private final int SELECT_PHOTO = 457;
    private final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 456;
    private String mCurrentPhotoPath;
    private String photoPath = "";
    private static final String CAMERA_DIR = "/dcim/";

    // for external permission
    private final static int READ_EXTERNAL_STORAGE = 101;
    private SharedPreferences sharedPreferences;
    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        setHeader(getResources().getString(R.string.header_edit_profile));
        EventConstant.ACTIVITIES.add(ctx);
        initialize();
        setListener();
    }

    private void initialize() {
        selected = false;
        TextView button_text = (TextView) findViewById(R.id.button_text);
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
        submit = (LinearLayout) findViewById(R.id.sign_up);
        radio_male = (ImageView) findViewById(R.id.radio_male);
        radio_female = (ImageView) findViewById(R.id.radio_female);
        radio_other = (ImageView) findViewById(R.id.radio_other);
        profile_image = (ImageView) findViewById(R.id.profile_image);
        sharedPreferences = getSharedPreferences(EventConstant.PREF_NAME, Activity.MODE_PRIVATE);

        country.setOnItemSelectedListener(typeSelectedListener);
        ImageView nav = (ImageView) findViewById(R.id.nav);
        nav.setVisibility(View.GONE);
        profile_image.setVisibility(View.VISIBLE);
        button_text.setText(getResources().getString(R.string.submit));

        email.setFocusable(false);
        email.setClickable(false);

        password.setVisibility(View.GONE);
        confirm_password.setVisibility(View.GONE);

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        day = calendar.get(Calendar.DAY_OF_MONTH);

        mInflator = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        cd = new ConnectionDetector(ctx);

        getProfile();
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

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateProfileForm();
            }
        });

        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    addPermissionDialogMarshMallow();
                } else {
                    showChooserDialog();
                }
            }
        });

    }

    private void addPermissionDialogMarshMallow() {
        ArrayList<String> permissions = new ArrayList<>();
        int resultCode = 0;

        permissions.add(WRITE_EXTERNAL_STORAGE);
        resultCode = READ_EXTERNAL_STORAGE;


        //filter out the permissions we have already accepted
        permissionsToRequest = findUnAskedPermissions(permissions);
        //get the permissions we have asked for before but are not granted..
        //we will store this in a global list to access later.
        permissionsRejected = findRejectedPermissions(permissions);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (permissionsToRequest.size() > 0) {//we need to ask for permissions
                //but have we already asked for them?
                requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]), resultCode);
                //mark all these as asked..
                for (String perm : permissionsToRequest) {
                    markAsAsked(perm, sharedPreferences);
                }
            } else {
                //show the success banner
                if (permissionsRejected.size() < permissions.size()) {
                    //this means we can show success because some were already accepted.
                    //permissionSuccess.setVisibility(View.VISIBLE);
                    showChooserDialog();
                }

                if (permissionsRejected.size() > 0) {
                    //we have none to request but some previously rejected..tell the user.
                    //It may be better to show a dialog here in a prod application

                    requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]), resultCode);
                    //mark all these as asked..
                    for (String perm : permissionsToRequest) {
                        markAsAsked(perm, sharedPreferences);
                    }
                }
            }
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {
            case READ_EXTERNAL_STORAGE:
                if (hasPermission(WRITE_EXTERNAL_STORAGE)) {
                    //        permissionSuccess.setVisibility(View.VISIBLE);
                    showChooserDialog();
                } else {
                    permissionsRejected.add(WRITE_EXTERNAL_STORAGE);
                    clearMarkAsAsked(WRITE_EXTERNAL_STORAGE);
                    String message = "permission for access external storage was rejected. Please allow to run the app.";
                    makePostRequestSnack(message, permissionsRejected.size());
                }
                break;
        }
    }

    public void showChooserDialog() {
        image_picker_dialog = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar);
        image_picker_dialog.setContentView(R.layout.custom_imageupload_dialog);
        image_picker_dialog.setCancelable(true);

        // set the custom dialog components - text, image and button
        ImageView gallery = (ImageView) image_picker_dialog.findViewById(R.id.gallery_image);
        gallery.setImageResource(R.mipmap.gallery);
        ImageView camera = (ImageView) image_picker_dialog.findViewById(R.id.camera_image);
        camera.setImageResource(R.mipmap.camera);
        LinearLayout cameraLayout = (LinearLayout) image_picker_dialog.findViewById(R.id.cameraLayout);
        LinearLayout gallery_layout = (LinearLayout) image_picker_dialog.findViewById(R.id.gallery_layout);
        cross_dialog = (ImageView) image_picker_dialog.findViewById(R.id.cross_dialog);
        cross_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                image_picker_dialog.dismiss();
            }
        });

        gallery_layout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, SELECT_PHOTO);
                image_picker_dialog.dismiss();
                image_picker_dialog.hide();
            }
        });

        cameraLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // give the image a name so we can store it in the phone's
                // default location
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//				File f = null;
                try {
//					f = setUpPhotoFile();

                    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                    String imageFileName = "bmh" + timeStamp + "_";
                    File albumF = getAlbumDir();
                    File imageF = File.createTempFile(imageFileName, "bmh", albumF);


                    mCurrentPhotoPath = imageF.getAbsolutePath();
                    Uri photoURI = FileProvider.getUriForFile(ctx, getApplicationContext().getPackageName() + ".my.package.name.provider", imageF);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                } catch (IOException e) {
                    e.printStackTrace();
//					f = null;
//					mCurrentPhotoPath = null;

                }

                startActivityForResult(takePictureIntent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
                image_picker_dialog.dismiss();
                image_picker_dialog.hide();
            }
        });
        image_picker_dialog.show();
    }

    private File getAlbumDir() {
        File storageDir = null;

        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
                storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "CameraPicture");
            } else {
                storageDir = new File(Environment.getExternalStorageDirectory() + CAMERA_DIR + "CameraPicture");
            }

            if (storageDir != null) {
                if (!storageDir.mkdirs()) {
                    if (!storageDir.exists()) {
//		Log.d("CameraSample", "failed to create directory");
                        return null;
                    }
                }
            }

        } else {
//		Log.v(getString(R.string.app_name), "External storage is not mounted READ/WRITE.");
        }

        return storageDir;
    }

    public String compressImage(String imageUri, int flag) {
        String filePath;
        if (flag == 1) {
            filePath = getRealPathFromURI(imageUri);
        }

        filePath = imageUri;
        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;
        float maxHeight = 816.0f;
        float maxWidth = 612.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;

            }
        }

        ImageLoadingUtils utils = new ImageLoadingUtils(this);
        options.inSampleSize = utils.calculateInSampleSize(options, actualWidth, actualHeight);
        options.inJustDecodeBounds = false;
        options.inDither = false;
//        options.inPurgeable = true;
//        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));


        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        FileOutputStream out = null;
        String filename = getFilename();
        try {
            out = new FileOutputStream(filename);
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return filename;
    }

    public String getFilename() {
        File file = new File(Environment.getExternalStorageDirectory().getPath(), "MyFolder/Images");
        if (!file.exists()) {
            file.mkdirs();
        }
        String uriSting = (file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg");
        return uriSting;
    }

    private String getRealPathFromURI(String contentURI) {
        Uri contentUri = Uri.parse(contentURI);
        Cursor cursor = this.getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }

    }

    private void validateProfileForm() {
        String firstName = first_name.getText().toString().trim();
        String lastName = last_name.getText().toString().trim();
        String email_val = email.getText().toString().trim();
        String address_val = address.getText().toString().trim();
        String city_val = city.getText().toString().trim();
        String state_val = state.getText().toString().trim();
        String dob_val = dob.getText().toString().trim();
        String zip_val = zip_code.getText().toString();
        String mobile_val = mobile.getText().toString();

        dialog = new EventDialogs(ctx);

        if (firstName.equals("")) {
            dialog.displayCommonDialog(getResources().getString(R.string.first_name_msg));
        } else if (lastName.equals("")) {
            dialog.displayCommonDialog(getResources().getString(R.string.last_name_msg));
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
        } else if (!selected) {
            dialog.displayCommonDialog(getResources().getString(R.string.country_msg));
        } else if (zip_val.equals("")) {
            dialog.displayCommonDialog(getResources().getString(R.string.zip_code_msg));
        } else if (!zip_val.equals("") && zip_val.length() < 5) {
            dialog.displayCommonDialog(getResources().getString(R.string.zip_code_no_msg));
        } else if (dob_val.equals("")) {
            dialog.displayCommonDialog(getResources().getString(R.string.dob_msg));
        } else {
            updateProfile(firstName, lastName, email_val, mobile_val, gender, dob_val, address_val, city_val, state_val, countryId, zip_val);
            hideSoftKeyboard();
        }
    }

    private void updateProfile(String firstName, final String lastName, final String email_val, String mobile_val, String gender, String dob, String address, String city_val, String state_val, String country_val, String zip_val) {
        if (cd.isConnectingToInternet()) {
            final ProgressDialog d = EventDialogs.showLoading(ctx);
            d.setCanceledOnTouchOutside(false);
            Call<LoginPojo> call;

            FileUploadSer.FileUploadService service = ServiceGenerator.createService(FileUploadSer.FileUploadService.class);
            File file = new File(photoPath);

            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part body = null;
            if (photoPath != null && !photoPath.equals("")) {
                body = MultipartBody.Part.createFormData("profile_pic", file.getName(), requestFile);
                call = service.updateProfile(getMultiPartData("UpdateProfile"), getMultiPartData(getFromPrefs(EventConstant.USER_ID)), getMultiPartData(firstName), getMultiPartData(lastName), getMultiPartData(gender), getMultiPartData(dob), getMultiPartData(mobile_val), getMultiPartData(address), getMultiPartData(city_val),
                        getMultiPartData(state_val), getMultiPartData(country_val), getMultiPartData(zip_val), getMultiPartData(getFromPrefs(EventConstant.USER_ROLE)), body);
            } else {
                call = service.updateProfileWithoutImage(getMultiPartData("UpdateProfile"), getMultiPartData(getFromPrefs(EventConstant.USER_ID)), getMultiPartData(firstName), getMultiPartData(lastName), getMultiPartData(gender), getMultiPartData(dob), getMultiPartData(mobile_val), getMultiPartData(address), getMultiPartData(city_val),
                        getMultiPartData(state_val), getMultiPartData(country_val), getMultiPartData(zip_val), getMultiPartData(getFromPrefs(EventConstant.USER_ROLE)));
            }
            call.enqueue(new Callback<LoginPojo>() {
                @Override
                public void onResponse(Call<LoginPojo> call, Response<LoginPojo> response) {
                    if (response.body().getStatus_code().equals("1")) {
                        displayToast(getResources().getString(R.string.profile_updated));
                        saveIntoPrefs(EventConstant.IMAGE, response.body().getData().getProfile_pic());
                        saveIntoPrefs(EventConstant.NAME, response.body().getData().getFirst_name() + " " + response.body().getData().getLast_name());
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

    private RequestBody getMultiPartData(String value) {
        RequestBody multipart =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), value);

        return multipart;
    }

    private SpinnerAdapter typeSpinnerAdapter = new BaseAdapter() {

        private TextView text;

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = mInflator.inflate(R.layout.row_spinner, null);
            }
            text = (TextView) convertView.findViewById(R.id.spinnerTarget);
            if (!selected) {
                text.setText(getResources().getString(R.string.select_country));
            } else {
                text.setText(countriesArr.get(position).getName());
            }
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

    private AdapterView.OnItemSelectedListener typeSelectedListener = new AdapterView.OnItemSelectedListener() {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view,
                                   int position, long id) {
            selected = true;
            countryId = countriesArr.get(position).getId();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            selected = false;
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
            ApiInterface apiService =
                    ApiClient.getClient().create(ApiInterface.class);

            Call<CountriesPojo> call = apiService.getCountries("GetCountries");
            call.enqueue(new Callback<CountriesPojo>() {
                @Override
                public void onResponse(Call<CountriesPojo> call, Response<CountriesPojo> response) {
                    if (response.body().getStatus_code().equals("1")) {
                        countriesArr = response.body().getData();
                        int index = 0;
                        country.setAdapter(typeSpinnerAdapter);
                        for (CountriesDataPojo obj : countriesArr) {
                            index++;
                            if (obj.getName().equals(countryVal)) {
                                country.setSelection(index - 1);
                                countryId = obj.getId();
                                selected = true;
                                break;
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<CountriesPojo> call, Throwable t) {
                    // Log error here since request failed
                    System.out.println("retrofit hh failure " + t.getMessage());
                }
            });
        } else {
            dialog.displayCommonDialog(getResources().getString(R.string.no_internet_connection));
        }
    }

    private void getProfile() {
        EventDialogs dialog = new EventDialogs(this);
        if (cd.isConnectingToInternet()) {
            final ProgressDialog d = EventDialogs.showLoading(this);
            d.setCanceledOnTouchOutside(false);

            ApiInterface apiService =
                    ApiClient.getClient().create(ApiInterface.class);

            Call<LoginPojo> call = apiService.getProfile("MyProfile", getFromPrefs(EventConstant.USER_ID));
            call.enqueue(new Callback<LoginPojo>() {
                @Override
                public void onResponse(Call<LoginPojo> call, Response<LoginPojo> response) {
                    d.dismiss();
                    if (response.body().getStatus_code().equals("1")) {
                        LoginDataPojo data = response.body().getData();
                        first_name.setText(data.getFirst_name());
                        last_name.setText(data.getLast_name());
                        email.setText(data.getEmail());
                        city.setText(data.getCity());
                        address.setText(data.getAddress());
                        state.setText(data.getState());
                        zip_code.setText(data.getPostcode());
                        mobile.setText(data.getMobile_no());
                        countryVal = data.getCountry_name();
                        dobVal = data.getDob();
                        saveIntoPrefs(EventConstant.IMAGE, data.getProfile_pic());
                        setProfileImageInLayout(ctx, (int) getResources().getDimension(R.dimen.profile_image), (int) getResources().getDimension(R.dimen.profile_image), data.getProfile_pic(), profile_image);
                        if (data.getGender().equalsIgnoreCase(getResources().getString(R.string.male)))
                            male.performClick();
                        else if (data.getGender().equalsIgnoreCase(getResources().getString(R.string.female)))
                            female.performClick();
                        else
                            other.performClick();

                        String date[] = dobVal.split("-");

                        year = Integer.parseInt(date[0]);
                        month = Integer.parseInt(date[1]) - 1;
                        day = Integer.parseInt(date[2]);

                        calendar.set(year, month, day);
                        getCountries();
                        updateLabel();
                    }
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

    public void onActivityResult(final int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
        }

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_PHOTO) {

                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = this.getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String imagePath1 = cursor.getString(columnIndex);

//                File f =new File(compressImage(imagePath1, 1));
                File f = new File(compressImage(imagePath1, 1));
                Bitmap myBitmap = BitmapFactory.decodeFile(f.getAbsolutePath());

                profile_image.setImageBitmap(myBitmap);
                photoPath = f.getAbsolutePath();

            } else if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {

                File f = new File(compressImage(mCurrentPhotoPath, 0));
                Bitmap myBitmap = BitmapFactory.decodeFile(f.getAbsolutePath());

                profile_image.setImageBitmap(myBitmap);
                photoPath = f.getAbsolutePath();
            }
        }
    }
}
