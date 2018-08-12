package com.endive.eventplanner.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.endive.eventplanner.R;
import com.endive.eventplanner.adapter.PersonListTicketAdapter;
import com.endive.eventplanner.pojo.EventPackageDetailPojo;
import com.endive.eventplanner.pojo.PackageMerchandiseDetailPojo;
import com.endive.eventplanner.pojo.PersonTicketDetailPojo;
import com.endive.eventplanner.pojo.SomethingInterestingDataPojo;
import com.endive.eventplanner.pojo.TicketDataPojo;
import com.endive.eventplanner.util.EventConstant;

import java.util.ArrayList;
import java.util.Arrays;

public class FillTicketDetailsActivity extends BaseActivity implements View.OnClickListener {

    private FillTicketDetailsActivity ctx = this;
    private RecyclerView ticket_person_list;
    private int ticketCount;
    private ArrayList<PersonTicketDetailPojo> personListArr;
    private PersonListTicketAdapter adapter;
    private SomethingInterestingDataPojo data;
    private LinearLayout proceed;
    private TicketDataPojo dataTicket;
    private EventPackageDetailPojo packageData;
    private String seats = "";
    private String from;
    private ArrayList<PackageMerchandiseDetailPojo> arr_list;
    private String venue_id;
    private ArrayList<String> seatsArr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_ticket_details);

        EventConstant.ACTIVITIES.add(ctx);

        ticketCount = getIntent().getIntExtra("ticketCount", 0);
        from = getIntent().getStringExtra("from");
        data = (SomethingInterestingDataPojo) getIntent().getSerializableExtra("event_detail");
        personListArr = (ArrayList<PersonTicketDetailPojo>) getIntent().getSerializableExtra("tickets_detail");
        dataTicket = (TicketDataPojo) getIntent().getSerializableExtra("dataTicket");
        packageData = (EventPackageDetailPojo) getIntent().getSerializableExtra("packageData");
        seats = getIntent().getStringExtra("seats");
        venue_id = getIntent().getStringExtra("venue_id");
        setHeader(getResources().getString(R.string.header_fill_details));

        initialize();
    }

    private void initialize() {
        if(seats != null){
            seatsArr = new ArrayList<>(Arrays.asList(seats.split(",")));
        }
        if(personListArr == null) {
            personListArr = new ArrayList<>();

            for (int i = 0; i < ticketCount; i++) {
                PersonTicketDetailPojo obj = new PersonTicketDetailPojo();
                if(seatsArr != null)
                    obj.setSeat(seatsArr.get(i));
                obj.setPackageData(packageData);
                personListArr.add(i, obj);
            }
        }
        proceed = (LinearLayout) findViewById(R.id.proceed);
        proceed.setOnClickListener(this);

        ticket_person_list = (RecyclerView) findViewById(R.id.ticket_person_list);
        ticket_person_list.setHasFixedSize(true);
        setRecyclerLayoutManager(ticket_person_list);

        setAdapter();

    }

    private void setAdapter() {
        adapter = new PersonListTicketAdapter(ctx, personListArr, packageData);
        ticket_person_list.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        if(from == null) {
            Intent intent = new Intent(ctx, FillTicketDetailsActivity.class);
            intent.putExtra("ticketCount", ticketCount);
            intent.putExtra("event_detail", data);
            intent.putExtra("dataTicket", dataTicket);
            intent.putExtra("seats", seats);
            intent.putExtra("venue_id",venue_id);
            intent.putExtra("from", "fill_detail");
            intent.putExtra("tickets_detail", personListArr);
            intent.putExtra("packageData", packageData);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        PersonTicketDetailPojo data;
        switch (v.getId()) {
            case R.id.male:
                data = (PersonTicketDetailPojo) v.getTag(R.string.data);
                data.setGender("1");
                adapter.notifyDataSetChanged();
                break;

            case R.id.female:
                data = (PersonTicketDetailPojo) v.getTag(R.string.data);
                data.setGender("2");
                adapter.notifyDataSetChanged();
                break;

            case R.id.other:
                data = (PersonTicketDetailPojo) v.getTag(R.string.data);
                data.setGender("3");
                adapter.notifyDataSetChanged();
                break;

            case R.id.proceed:
                hideSoftKeyboard();
                boolean goAhead = true;
                for (int i = 0; i < personListArr.size(); i++) {
                    if (personListArr.get(i).getName().toString().trim().equals("") || personListArr.get(i).getAge().toString().trim().equals("")) {
                        goAhead = false;
                        break;
                    }
                }
                if (goAhead) {
                    if(from.equals("fill_detail")) {
                        Intent buy_ticket_intent = new Intent(this, OrderSummaryActivity.class);
                        buy_ticket_intent.putExtra("event_detail", this.data);
                        buy_ticket_intent.putExtra("tickets_detail", personListArr);
                        buy_ticket_intent.putExtra("dataTicket", dataTicket);
                        buy_ticket_intent.putExtra("seats", seats);
                        buy_ticket_intent.putExtra("venue_id",venue_id);
                        buy_ticket_intent.putExtra("ticketCount", ticketCount);
                        buy_ticket_intent.putExtra("packageData", packageData);
                        startActivity(buy_ticket_intent);
                        finish();
                    }
                    else{
                        Intent intent = new Intent();
                        intent.putExtra("event_detail", this.data);
                        intent.putExtra("tickets_detail", personListArr);
                        intent.putExtra("dataTicket", dataTicket);
                        intent.putExtra("seats", seats);
                        intent.putExtra("venue_id",venue_id);
                        intent.putExtra("ticketCount", ticketCount);
                        intent.putExtra("packageData", packageData);
                        setResult(Activity.RESULT_OK, intent);
                        finish();
                    }
                } else {
                    displayToast(getResources().getString(R.string.fill_entries));
                }
                break;
        }
    }
}
