package com.endive.eventplanner.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.endive.eventplanner.R;
import com.endive.eventplanner.adapter.CustomAdapter;
import com.endive.eventplanner.adapter.NothingSelectedSpinnerAdapter;
import com.endive.eventplanner.adapter.PackageCustomAdapterListView;
import com.endive.eventplanner.adapter.TicketCountAdapter;
import com.endive.eventplanner.pojo.EventPackageDetailPojo;
import com.endive.eventplanner.pojo.SomethingInterestingDataPojo;
import com.endive.eventplanner.pojo.TicketCountPojo;
import com.endive.eventplanner.pojo.TicketDataPojo;
import com.endive.eventplanner.pojo.VenueDataPojo;
import com.endive.eventplanner.util.EventConstant;
import com.endive.eventplanner.util.EventDialogs;
import com.endive.eventplanner.util.ViewAnimationUtils;

import java.util.ArrayList;

public class BuyTicketActivity extends BaseActivity implements View.OnClickListener {

    private BuyTicketActivity ctx = this;
    private LinearLayout ticket_layout, ticket_option;
    private LinearLayout package_layout, package_option;
    private ImageView ticket_down, package_down;
    private ImageView no_of_person;
    private EventDialogs dialogErr;

    public SomethingInterestingDataPojo data;

    private ListView ticket_list;
    private ArrayList<TicketDataPojo> ticketArr;

    private ListView rv_package_list;
    private ArrayList<EventPackageDetailPojo> packageArr;
    private LinearLayout package_divider;

    private ArrayList<VenueDataPojo> venueArr;

    private boolean is_ticket = false;
    private int ticketCount = 0;
    private TicketCountAdapter countAdapter;
    private ArrayList<TicketCountPojo> countArr;

    private LayoutInflater mInflator;
    private Spinner venue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_ticket);

        setHeader(getResources().getString(R.string.buy_ticket));
        EventConstant.ACTIVITIES.add(ctx);

        initialize();
        setListener();
    }

    private void initialize() {
        ticket_layout = (LinearLayout) findViewById(R.id.ticket_layout);
        ticket_option = (LinearLayout) findViewById(R.id.ticket_option);
        package_layout = (LinearLayout) findViewById(R.id.package_layout);
        package_option = (LinearLayout) findViewById(R.id.package_option);
        package_divider = (LinearLayout) findViewById(R.id.package_divider);

        ticket_down = (ImageView) findViewById(R.id.ticket_down);
        package_down = (ImageView) findViewById(R.id.package_down);

        mInflator = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

        ticketArr = new ArrayList<>();
        ticket_list = (ListView) findViewById(R.id.ticket_list);

        packageArr = new ArrayList<>();
        rv_package_list = (ListView) findViewById(R.id.rv_package_list);

        Intent intent = getIntent();

        data = (SomethingInterestingDataPojo) getIntent().getSerializableExtra("data");
        packageArr = (ArrayList<EventPackageDetailPojo>) intent.getSerializableExtra("packageArr");
        ticketArr = (ArrayList<TicketDataPojo>) intent.getSerializableExtra("ticketArr");
        venueArr = (ArrayList<VenueDataPojo>) intent.getSerializableExtra("venueArr");

        if (packageArr.size() == 0) {
            package_layout.setVisibility(View.GONE);
            package_divider.setVisibility(View.GONE);
        } else
            setPackageAdapter();

        if (ticketArr.size() == 0)
            ticket_layout.setVisibility(View.GONE);
        else
            setAdapter();

        countArr = new ArrayList<>();
        for (int i = 10; i >= 1; i--) {
            TicketCountPojo obj = new TicketCountPojo();
            obj.setCount("" + i);
            obj.setSelected(false);
            countArr.add(obj);
        }
        dialogErr = new EventDialogs(ctx);
        countAdapter = new TicketCountAdapter(ctx, countArr, "buyTicket");
    }

    private void setListener() {
        ticket_layout.setOnClickListener(this);
        package_layout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ticket_layout:
                ticketLayoutClick();
                break;

            case R.id.package_layout:
                packageLayoutClick();
                break;

            case R.id.buy_ticket:
                TicketDataPojo dataTicket = (TicketDataPojo) v.getTag(R.string.data);
                is_ticket = true;
                displayPurchaseTicketDialog(dataTicket, null);
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

            case R.id.tv_buy_now:
                is_ticket = false;
                EventPackageDetailPojo package_list = (EventPackageDetailPojo) v.getTag(R.string.packageList);
                TicketDataPojo dataTicketDetail = null;
                displayPurchaseTicketDialog(dataTicketDetail, package_list);
                break;

        }
    }

    private void ticketLayoutClick() {
        if (ticket_option.getVisibility() == View.GONE) {
            ViewAnimationUtils.expand(ticket_option);
            setArrow(ticket_down, true);
        } else {
            ViewAnimationUtils.collapse(ticket_option);
            setArrow(ticket_down, false);
        }
    }

    private void packageLayoutClick() {
        if (package_option.getVisibility() == View.GONE) {
            ViewAnimationUtils.expand(package_option);
            setArrow(package_down, true);
        } else {
            ViewAnimationUtils.collapse(package_option);
            setArrow(package_down, false);
        }
    }

    private void setArrow(ImageView image, boolean check) {
        if (check)
            image.setImageResource(R.mipmap.upperarrow);
        else
            image.setImageResource(R.mipmap.downarrow_grey);
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

    private void setAdapter() {
        CustomAdapter adapter = new CustomAdapter(ticketArr, ctx);
        ticket_list.setAdapter(adapter);
        setListViewHeightBasedOnItems(ticket_list, "ticket");
    }

    private void setPackageAdapter() {
        PackageCustomAdapterListView adapter = new PackageCustomAdapterListView(packageArr, ctx);
        rv_package_list.setAdapter(adapter);
        setListViewHeightBasedOnItems(rv_package_list, "package");
    }

    public boolean setListViewHeightBasedOnItems(ListView listView, String from) {

        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter != null) {

            int numberOfItems = listAdapter.getCount();

            // Get total height of all items.
            int totalItemsHeight = 0;
            for (int itemPos = 0; itemPos < numberOfItems; itemPos++) {
                View item = listAdapter.getView(itemPos, null, listView);
                item.measure(0, 0);
                totalItemsHeight += item.getMeasuredHeight();
                if (from.equals("package") && packageArr.get(itemPos).getPackage_detail().getPackage_merchandise_detail().size() == 1) {
                    totalItemsHeight += getResources().getInteger(R.integer.sub_from_package);
                }
            }
            // Get total height of all item dividers.
            int totalDividersHeight = listView.getDividerHeight() *
                    (numberOfItems - 1);
            // Set list height.
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            if (totalDividersHeight > 0)
                params.height = totalItemsHeight + totalDividersHeight;
            else
                params.height = totalItemsHeight;

            if (from.equals("ticket"))
                params.height += numberOfItems * getResources().getInteger(R.integer.add_in_ticket);
            else {
//                displayToast(""+getResources().getInteger(R.integer.sub_from_package));
                params.height -= numberOfItems * getResources().getInteger(R.integer.sub_from_package);
            }
            listView.setLayoutParams(params);
            listView.requestLayout();

            return true;

        } else {
            return false;
        }

    }
}
