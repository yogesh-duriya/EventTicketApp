package com.endive.eventplanner.util;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.endive.eventplanner.R;
import com.endive.eventplanner.activity.HomeActivity;

/**
 * Created by arpit.jain on 10/6/2017.
 */

public class EventDialogs {

    private Activity ctx;

    public static ProgressDialog showLoading(Activity activity) {
        ProgressDialog mProgressDialog = new ProgressDialog(activity);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage(activity.getResources().getString(R.string.loading));
        if (!activity.isFinishing() && !mProgressDialog.isShowing())
            mProgressDialog.show();
        return mProgressDialog;
    }

    public EventDialogs(Activity ctx) {
        this.ctx = ctx;
    }

    public void displayCommonDialog(String msg) {
        LinearLayout btn_yes_exit_LL;
        final Dialog dialog = new Dialog(ctx, android.R.style.Theme_Translucent_NoTitleBar);
        dialog.setContentView(R.layout.custom_dialog_withheader);

        TextView msg_textView = (TextView) dialog.findViewById(R.id.text_exit);

        msg_textView.setText(msg);
        btn_yes_exit_LL = (LinearLayout) dialog.findViewById(R.id.btn_yes_exit_LL);

        ImageView dialog_header_cross = (ImageView) dialog.findViewById(R.id.dialog_header_cross);
        dialog_header_cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btn_yes_exit_LL.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        if (ctx != null && !ctx.isFinishing())
            dialog.show();
    }

    public void displayCommonDialogFinish(String msg) {
        LinearLayout btn_yes_exit_LL;
        final Dialog dialog = new Dialog(ctx, android.R.style.Theme_Translucent_NoTitleBar);
        dialog.setContentView(R.layout.custom_dialog_withheader);

        TextView msg_textView = (TextView) dialog.findViewById(R.id.text_exit);

        msg_textView.setText(msg);
        btn_yes_exit_LL = (LinearLayout) dialog.findViewById(R.id.btn_yes_exit_LL);

        ImageView dialog_header_cross = (ImageView) dialog.findViewById(R.id.dialog_header_cross);
        dialog_header_cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btn_yes_exit_LL.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                ctx.finish();
            }
        });

        if (ctx != null && !ctx.isFinishing())
            dialog.show();
    }

    public void displayCommonDialogWithCancel(String msg) {
        LinearLayout btn_yes_exit_LL;
        LinearLayout btn_no_exit_LL;
        TextView btn_yes_exit;
        TextView btn_no_exit;
        final Dialog dialog = new Dialog(ctx, android.R.style.Theme_Translucent_NoTitleBar);
        dialog.setContentView(R.layout.custom_dialog_withheader);

        TextView msg_textView = (TextView) dialog.findViewById(R.id.text_exit);

        msg_textView.setText(msg);
        btn_yes_exit_LL = (LinearLayout) dialog.findViewById(R.id.btn_yes_exit_LL);
        btn_no_exit_LL = (LinearLayout) dialog.findViewById(R.id.btn_no_exit_LL);
        btn_yes_exit = (TextView) dialog.findViewById(R.id.btn_yes_exit);
        btn_no_exit = (TextView) dialog.findViewById(R.id.btn_no_exit);
        btn_no_exit_LL.setVisibility(View.VISIBLE);

        btn_yes_exit.setText(ctx.getResources().getString(R.string.yes));
        btn_no_exit.setText(ctx.getResources().getString(R.string.no));

        ImageView dialog_header_cross = (ImageView) dialog.findViewById(R.id.dialog_header_cross);
        dialog_header_cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btn_yes_exit_LL.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
                for (int i = 0; i < EventConstant.ACTIVITIES.size(); i++) {
                    if (EventConstant.ACTIVITIES.get(i) != null)
                        EventConstant.ACTIVITIES.get(i).finish();
                }
                Intent intent = new Intent(ctx, HomeActivity.class);
                ctx.startActivity(intent);
                ctx.finish();
            }
        });
        btn_no_exit_LL.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        if (ctx != null && !ctx.isFinishing())
            dialog.show();
    }

}
