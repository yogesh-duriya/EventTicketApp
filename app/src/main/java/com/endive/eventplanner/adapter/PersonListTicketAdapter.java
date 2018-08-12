package com.endive.eventplanner.adapter;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.endive.eventplanner.R;
import com.endive.eventplanner.activity.FillTicketDetailsActivity;
import com.endive.eventplanner.pojo.ColorSizeListPojo;
import com.endive.eventplanner.pojo.EventPackageDetailPojo;
import com.endive.eventplanner.pojo.PackageMerchandiseDetailPojo;
import com.endive.eventplanner.pojo.PersonTicketDetailPojo;

import java.util.ArrayList;

/**
 * Created by arpit.jain on 11/2/2017.
 */

public class PersonListTicketAdapter extends RecyclerView.Adapter<PersonListTicketAdapter.ViewHolder> {

    private FillTicketDetailsActivity ctx;
    private ArrayList<PersonTicketDetailPojo> arr;
    private EventPackageDetailPojo packageData;
    private int color_pos = 0;
    private int size_pos = 0;

    public PersonListTicketAdapter(FillTicketDetailsActivity ctx, ArrayList<PersonTicketDetailPojo> arr, EventPackageDetailPojo packageData) {
        this.ctx = ctx;
        this.arr = arr;
        this.packageData = packageData;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_layout_ticket_details, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final PersonTicketDetailPojo data = arr.get(position);
        position = position + 1;
        holder.ticket_number.setText(ctx.getResources().getString(R.string.ticket) + " " + position + ":");
        if (data.getName().equals(""))
            holder.ticket_member_name.setText("");
        else
            holder.ticket_member_name.setText(data.getName());

        if (data.getAge().equals(""))
            holder.age.setText("");
        else
            holder.age.setText(data.getAge());

        if (data.getGender().equals("1")) {
            holder.radio_male.setImageResource(R.mipmap.circle_colour);
            holder.radio_other.setImageResource(R.mipmap.circle);
            holder.radio_female.setImageResource(R.mipmap.circle);
        } else if (data.getGender().equals("2")) {
            holder.radio_male.setImageResource(R.mipmap.circle);
            holder.radio_other.setImageResource(R.mipmap.circle);
            holder.radio_female.setImageResource(R.mipmap.circle_colour);
        } else {
            holder.radio_male.setImageResource(R.mipmap.circle);
            holder.radio_other.setImageResource(R.mipmap.circle_colour);
            holder.radio_female.setImageResource(R.mipmap.circle);
        }
        holder.ticket_member_name.setHint(ctx.getResources().getString(R.string.name_of_ticket) + " " + position);

        if (packageData != null) {
            ArrayList<PackageMerchandiseDetailPojo> merchandise_detail_arr = data.getPackageData().getPackage_detail().getPackage_merchandise_detail();
            holder.merchandise_row.removeAllViews();
            for (int i = 0; i < merchandise_detail_arr.size(); i++) {
                View itemView = LayoutInflater.from(ctx).inflate(R.layout.row_layout_merchandise_fill_details, null, false);
                ImageView merchandise_image_small = (ImageView) itemView.findViewById(R.id.merchandise_image_small);
                TextView merchandise_name_small = (TextView) itemView.findViewById(R.id.merchandise_name_small);
                Spinner color_spinnner = (Spinner) itemView.findViewById(R.id.color_spinnner);
                Spinner size_spinner = (Spinner) itemView.findViewById(R.id.size_spinner);
                ctx.setCategoryImageInLayout(ctx, merchandise_detail_arr.get(i).getMerchandise_detail().getImage(), merchandise_image_small);
                merchandise_name_small.setText(merchandise_detail_arr.get(i).getMerchandise_detail().getName());

                final ArrayList<String> color_list = new ArrayList<>();
                final ArrayList<String> size_list = new ArrayList<>();

                final ArrayList<ColorSizeListPojo> color = merchandise_detail_arr.get(i).getMerchandise_detail().getMerchandise_property_detail().getColor();
                final ArrayList<ColorSizeListPojo> size = merchandise_detail_arr.get(i).getMerchandise_detail().getMerchandise_property_detail().getSize();
                color_pos = 0;
                size_pos = 0;

                for (int k = 0; k < color.size(); k++) {
                    if (color.get(k).isSelected()) {
                        color_pos = k;
                        break;
                    } else
                        color_pos = 0;
                }

                for (int j = 0; j < size.size(); j++) {
                    if (size.get(j).isSelected()) {
                        size_pos = j;
                        break;
                    } else
                        size_pos = 0;
                }

                for (int k = 0; k < color.size(); k++) {
                    color_list.add(color.get(k).getColor_name());
                }

                for (int j = 0; j < size.size(); j++) {
                    size_list.add(size.get(j).getSize());
                }

                color_spinnner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        for (int j = 0; j < color_list.size(); j++) {
                            color.get(j).setSelected(false);
                        }
                        color_pos = i;
                        color.get(i).setSelected(true);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

                size_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        for (int j = 0; j < size_list.size(); j++) {
                            size.get(j).setSelected(false);
                        }
                        size_pos = i;
                        size.get(i).setSelected(true);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

                ArrayAdapter<String> color_adapter = new ArrayAdapter<>(ctx, R.layout.spinner_item, color_list);
                color_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                color_spinnner.setAdapter(color_adapter);

                color_spinnner.setSelection(color_pos);

                ArrayAdapter<String> size_adapter = new ArrayAdapter<>(ctx, R.layout.spinner_item, size_list);
                size_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                size_spinner.setAdapter(size_adapter);

                size_spinner.setSelection(size_pos);

                holder.merchandise_row.addView(itemView);
            }
            LinearLayout layout2 = new LinearLayout(ctx);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 2);
            layout2.setOrientation(LinearLayout.HORIZONTAL);

            params.setMargins(0, 25, 0, 0);
            layout2.setLayoutParams(params);

            layout2.setBackgroundColor(ContextCompat.getColor(ctx, R.color.filter_divider));
//            View itemView = LayoutInflater.from(ctx).inflate(R.layout.divider_layout, null, false);
            holder.merchandise_row.addView(layout2);
        }

        holder.male.setTag(R.string.data, data);
        holder.female.setTag(R.string.data, data);
        holder.other.setTag(R.string.data, data);
        holder.ticket_member_name.setTag(R.string.data, data);

        holder.male.setOnClickListener(ctx);
        holder.female.setOnClickListener(ctx);
        holder.other.setOnClickListener(ctx);

        holder.ticket_member_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                arr.get(holder.getAdapterPosition()).setName(s + "");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        holder.age.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                arr.get(holder.getAdapterPosition()).setAge(s + "");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return arr.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView ticket_number;
        private EditText ticket_member_name, age;
        private ImageView radio_male, radio_female, radio_other;
        private LinearLayout male, female, merchandise_row, other;

        public ViewHolder(View itemView) {
            super(itemView);
            ticket_number = (TextView) itemView.findViewById(R.id.ticket_number);
            ticket_member_name = (EditText) itemView.findViewById(R.id.ticket_member_name);
            age = (EditText) itemView.findViewById(R.id.age);
            radio_male = (ImageView) itemView.findViewById(R.id.radio_male);
            radio_female = (ImageView) itemView.findViewById(R.id.radio_female);
            radio_other = (ImageView) itemView.findViewById(R.id.radio_other);
            male = (LinearLayout) itemView.findViewById(R.id.male);
            female = (LinearLayout) itemView.findViewById(R.id.female);
            other = (LinearLayout) itemView.findViewById(R.id.other);
            merchandise_row = (LinearLayout) itemView.findViewById(R.id.merchandise_row);
        }
    }
}