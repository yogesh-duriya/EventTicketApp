package com.endive.eventplanner.activity;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.endive.eventplanner.R;
import com.endive.eventplanner.pojo.PaymentNoncePackageDataPojo;
import com.endive.eventplanner.pojo.PaymentNoncePackageProductPojo;
import com.endive.eventplanner.pojo.PaymentNonceResultPojo;
import com.endive.eventplanner.util.EventConstant;
import com.endive.eventplanner.util.FileDownloader;
import com.endive.eventplanner.view.CircularTextView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class PlacedOrderDetailActivity extends BaseActivity {

    private PlacedOrderDetailActivity ctx = this;
    private TextView order_number, booking_date, event_name, event_type;
    private TextView total_amt, ticket_count, package_name, ticket_name;
    private TextView organizer_name, location, country, city;
    private TextView state, zip_code, grand_total, address, detail_header, seats;
    private TextView event_venue, event_date, total_ticket;
    private TextView amount, coupon, discount;
    private ImageView mail, print;
    private PaymentNonceResultPojo paymentResult;
    private LinearLayout user_list;
    private LinearLayout total_amount_layout;
    private ImageView payment_gateway;
    private String file_name;
    private String from;
    private boolean email_send = false;
    private File folder;

    // for external permission
    private final static int READ_EXTERNAL_STORAGE = 101;
    private SharedPreferences sharedPreferences;
    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_placed_order_detail);

        setHeader(getResources().getString(R.string.order_detail));

        initialize();
        setData();
        setListener();
        if (paymentResult.getPackage_data() != null && paymentResult.getPackage_data().size() > 0)
            displayPersonList();
    }

    private void initialize() {
        paymentResult = (PaymentNonceResultPojo) getIntent().getSerializableExtra("payment_response");
        from = getIntent().getStringExtra("from");
        sharedPreferences = getSharedPreferences(EventConstant.PREF_NAME, Activity.MODE_PRIVATE);
        order_number = (TextView) findViewById(R.id.order_number);
        booking_date = (TextView) findViewById(R.id.booking_date);
        event_name = (TextView) findViewById(R.id.event_name);
        event_type = (TextView) findViewById(R.id.event_type);
        total_amt = (TextView) findViewById(R.id.total_amt);
        ticket_count = (TextView) findViewById(R.id.ticket_count);
        package_name = (TextView) findViewById(R.id.package_name);
        ticket_name = (TextView) findViewById(R.id.ticket_name);
        organizer_name = (TextView) findViewById(R.id.organizer_name);
        location = (TextView) findViewById(R.id.location);
        country = (TextView) findViewById(R.id.country);
        city = (TextView) findViewById(R.id.city);
        state = (TextView) findViewById(R.id.state);
        zip_code = (TextView) findViewById(R.id.zip_code);
        address = (TextView) findViewById(R.id.address);
        grand_total = (TextView) findViewById(R.id.grand_total);
        amount = (TextView) findViewById(R.id.amount);
        coupon = (TextView) findViewById(R.id.coupon);
        discount = (TextView) findViewById(R.id.discount);
        detail_header = (TextView) findViewById(R.id.detail_header);
        seats = (TextView) findViewById(R.id.seats);
        event_venue = (TextView) findViewById(R.id.event_venue);
        event_date = (TextView) findViewById(R.id.event_date);
        total_ticket = (TextView) findViewById(R.id.total_ticket);
        user_list = (LinearLayout) findViewById(R.id.user_list);
        total_amount_layout = (LinearLayout) findViewById(R.id.total_amount_layout);
        payment_gateway = (ImageView) findViewById(R.id.payment_gateway);
        print = (ImageView) findViewById(R.id.print);
        mail = (ImageView) findViewById(R.id.mail);
    }

    private void displayPersonList() {
        user_list.removeAllViews();
        for (int i = 0; i < paymentResult.getPackage_data().size(); i++) {
            PaymentNoncePackageDataPojo data = paymentResult.getPackage_data().get(i);
            View v = LayoutInflater.from(this).inflate(R.layout.row_show_fill_details, null, false);
            TextView tv_gender, tv_user_name, tv_age;
            LinearLayout merchandise_list;
            tv_gender = (TextView) v.findViewById(R.id.tv_gender);
            tv_user_name = (TextView) v.findViewById(R.id.tv_user_name);
            tv_age = (TextView) v.findViewById(R.id.tv_age);
            merchandise_list = (LinearLayout) v.findViewById(R.id.merchandise_list_users);
            if (data != null) {
                merchandise_list.removeAllViews();
                ArrayList<PaymentNoncePackageProductPojo> arr_mer = data.getProduct_array();
                if (arr_mer != null) {
                    for (int j = 0; j < arr_mer.size(); j++) {
                        View merchandise = LayoutInflater.from(this).inflate(R.layout.row_layout_merchandise_list_order_summary, null, false);
                        TextView merchandise_name = (TextView) merchandise.findViewById(R.id.merchandise_name);
                        TextView size_view = (TextView) merchandise.findViewById(R.id.size_view);
                        CircularTextView tv_solid_color_set = (CircularTextView) merchandise.findViewById(R.id.tv_solid_color_set);
                        merchandise_name.setText(arr_mer.get(j).getName());

                        tv_solid_color_set.setSolidColor(arr_mer.get(j).getColor());
                        tv_solid_color_set.setStrokeWidth(1);
                        tv_solid_color_set.setStrokeColor();
                        size_view.setText(arr_mer.get(j).getSize());

                        merchandise_list.addView(merchandise);
                    }
                }
            } else {
                merchandise_list.setVisibility(View.GONE);
            }
            tv_user_name.setText(data.getName());
            tv_age.setText(data.getAge()+" "+getResources().getString(R.string.years));
            if (data.getGender().equals("1")) {
                tv_gender.setText(getResources().getString(R.string.male));
            } else if (data.getGender().equals("2")) {
                tv_gender.setText(getResources().getString(R.string.female));
            } else {
                tv_gender.setText(getResources().getString(R.string.other));
            }
            user_list.addView(v);
        }
    }

    private void setData() {
        order_number.setText(paymentResult.getBooking_number());
        booking_date.setText(paymentResult.getBooking_date());
        event_date.setText(paymentResult.getEvent_date());
        event_name.setText(paymentResult.getEvent_title());
        event_type.setText(paymentResult.getEvent_type());
        total_amt.setText(getResources().getString(R.string.dollar) + paymentResult.getPaid_amount());
        if (paymentResult.getNo_of_tickets().equals("1"))
            total_ticket.setText(getResources().getString(R.string.total_ticket));
        else
            total_ticket.setText(getResources().getString(R.string.total_tickets));
        ticket_count.setText(" " + paymentResult.getNo_of_tickets());
        package_name.setText(paymentResult.getPackage_name());
        ticket_name.setText(paymentResult.getTicket_name());
        organizer_name.setText(paymentResult.getOrganizer_name());
        event_venue.setText(paymentResult.getVenue_name());
        location.setText(paymentResult.getOrganizer_city());
        country.setText(paymentResult.getCountry());
        state.setText(paymentResult.getState());
        zip_code.setText(paymentResult.getZipcode());
        address.setText(paymentResult.getUser_address());
        city.setText(paymentResult.getCity());
        grand_total.setText(getResources().getString(R.string.dollar) + paymentResult.getPaid_amount());
        amount.setText(getResources().getString(R.string.dollar) + paymentResult.getTotal_amount());
        if (paymentResult.getBooking_type().equals("3")) {
            total_amount_layout.setVisibility(View.GONE);
            payment_gateway.setVisibility(View.GONE);
        }
        if (paymentResult.getCoupon_code() != null && !paymentResult.getCoupon_code().equals("")) {
            coupon.setText(paymentResult.getCoupon_code());
        } else {
            coupon.setText("NA");
        }

        discount.setText(getResources().getString(R.string.dollar) + paymentResult.getDiscount());
        seats.setText(paymentResult.getBooked_seats());
        if (paymentResult.getBooking_type().equals("1")) {
            detail_header.setText(getResources().getString(R.string.ticket_details));
            package_name.setText("NA");
        }
    }

    private void setListener() {
        print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email_send = false;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    addPermissionDialogMarshMallow();
                } else {
                    viewAndDownload();
                }
            }
        });
        mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email_send = true;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    addPermissionDialogMarshMallow();
                } else {
                    viewAndDownload();
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
                    viewAndDownload();
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
                    viewAndDownload();
                } else {
                    permissionsRejected.add(WRITE_EXTERNAL_STORAGE);
                    clearMarkAsAsked(WRITE_EXTERNAL_STORAGE);
                    String message = "permission for access external storage was rejected. Please allow to run the app.";
                    makePostRequestSnack(message, permissionsRejected.size());
                }
                break;
        }
    }

    private void viewAndDownload() {
        folder = new File(Environment.getExternalStorageDirectory() + "/ticket_pass");
        if (!folder.exists())
            folder.mkdir();
        file_name = paymentResult.getBooking_number();
        File file = new File(folder + "/" + file_name);
        if (!file.exists())
            new DownloadFile().execute(paymentResult.getPdf_url(), file_name);
        else if (!email_send)
            view();
        else
            openEmailClient();
    }

    private class DownloadFile extends AsyncTask<String, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            displayToast(getResources().getString(R.string.downloading));
        }

        @Override
        protected Void doInBackground(String... strings) {
            String fileUrl = strings[0];
            String fileName = strings[1];

            File pdfFile = new File(folder, fileName);

            try {
                pdfFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            FileDownloader.downloadFile(fileUrl, pdfFile);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (!email_send)
                view();
            else
                openEmailClient();
        }
    }

//    private void printFile() {
//        PrintManager printManager = (PrintManager) this.getSystemService(Context.PRINT_SERVICE);
//        String jobName = this.getString(R.string.app_name) + " Document";
//
//        PrintDocumentAdapter pda = new PrintDocumentAdapter() {
//
//            @Override
//            public void onWrite(PageRange[] pages, ParcelFileDescriptor destination, CancellationSignal cancellationSignal, WriteResultCallback callback) {
//                InputStream input = null;
//                OutputStream output = null;
//
//                try {
//
//                    input = new FileInputStream(file_name);
//                    output = new FileOutputStream(destination.getFileDescriptor());
//
//                    byte[] buf = new byte[1024];
//                    int bytesRead;
//
//                    while ((bytesRead = input.read(buf)) > 0) {
//                        output.write(buf, 0, bytesRead);
//                    }
//
//                    callback.onWriteFinished(new PageRange[]{PageRange.ALL_PAGES});
//
//                } catch (FileNotFoundException ee) {
//                    //Catch exception
//                } catch (Exception e) {
//                    //Catch exception
//                } finally {
//                    try {
//                        input.close();
//                        output.close();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//
//            @Override
//            public void onLayout(PrintAttributes oldAttributes, PrintAttributes newAttributes, CancellationSignal cancellationSignal, LayoutResultCallback callback, Bundle extras) {
//
//                if (cancellationSignal.isCanceled()) {
//                    callback.onLayoutCancelled();
//                    return;
//                }
//
//
//                PrintDocumentInfo pdi = new PrintDocumentInfo.Builder(file_name).setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT).build();
//
//                callback.onLayoutFinished(pdi, true);
//            }
//        };
//
//        printManager.print(jobName, pda, null);
//    }

    private void openEmailClient() {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"endiveqc@gmail.com"});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Your Tickets for " + paymentResult.getEvent_title() + " Event");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Hi " + getFromPrefs(EventConstant.NAME) + "!!\n\nPlease find attached tickets for your event.\nEnjoy the event.");
        File root = Environment.getExternalStorageDirectory();
        String pathToMyAttachedFile = "ticket_pass/" + file_name;
        File file = new File(root, pathToMyAttachedFile);
        if (!file.exists() || !file.canRead()) {
            return;
        }
        Uri uri = Uri.fromFile(file);
        emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
        final PackageManager pm = getPackageManager();
        final List<ResolveInfo> matches = pm.queryIntentActivities(emailIntent, 0);
        ResolveInfo best = null;
        for (final ResolveInfo info : matches)
            if (info.activityInfo.packageName.endsWith(".gm") || info.activityInfo.name.toLowerCase().contains("gmail"))
                best = info;
        if (best != null)
            emailIntent.setClassName(best.activityInfo.packageName, best.activityInfo.name);
        startActivity(emailIntent);
    }

    private void view() {
        File pdfFile = new File(Environment.getExternalStorageDirectory() + "/ticket_pass/" + file_name);  // -> filename = maven.pdf
//        Uri path = Uri.fromFile(pdfFile);
        Uri photoURI = FileProvider.getUriForFile(ctx, getApplicationContext().getPackageName() + ".my.package.name.provider", pdfFile);
        Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
        pdfIntent.setDataAndType(photoURI, "application/pdf");
        pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        pdfIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        try {
            startActivity(pdfIntent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(ctx, getResources().getString(R.string.no_app), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        if (from == null) {
            Intent intent = new Intent(ctx, HomeActivity.class);
            startActivity(intent);
            finish();
        } else {
            super.onBackPressed();
        }
    }
}
