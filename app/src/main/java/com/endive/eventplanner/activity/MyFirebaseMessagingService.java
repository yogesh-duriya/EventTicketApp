/**
 * Copyright 2016 Google Inc. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.endive.eventplanner.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;

import com.endive.eventplanner.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    private String image;
    private PendingIntent pi;

    private Bitmap bitmap;
    private Context ctx;
    private String message, title;

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]
        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }
        ctx = getApplicationContext();

        message = remoteMessage.getData().get("message");
        title = remoteMessage.getData().get("title");

        image = remoteMessage.getData().get("image");

        /**
         * In some cases it may be useful to show a notification indicating to the user
         * that a message was received.
         */
//        if (title != null && message!= null && title.length() > 0 && message.length() > 0 ) {
        sendNotification(message);
//        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }

    @Override
    public void handleIntent(Intent intent) {
        message = intent.getStringExtra("message");
        title = intent.getStringExtra("title");
        ctx = getApplicationContext();
        sendNotification(message);
    }

    // [END receive_message]

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param message FCM message body received.
     */

    private void sendNotification(String message) {
        Intent intent = new Intent(this, SplashActivity.class);


        // set intent so it does not start a new activity
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(getApplicationContext(), new Random().nextInt(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
        pi = resultPendingIntent;

        if (image != null && image.length() > 0)
            new GetNotificationTask().execute();
        else
            sendBigTextStyleNotification();
    }

    public void sendBigTextStyleNotification() {

        NotificationManager notificationManager = (NotificationManager) ctx
                .getSystemService(ctx.NOTIFICATION_SERVICE);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(ctx);
        Notification notification = mBuilder.setSmallIcon(R.mipmap.logo).setTicker(title).setWhen(0).setColor(ContextCompat.getColor(ctx, R.color.button_color))
                .setAutoCancel(true).setContentTitle(title)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message)).setContentIntent(pi)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setLargeIcon(BitmapFactory.decodeResource(ctx.getResources(), R.mipmap.ic_launcher))
                .setContentText(message).build();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int smallIconViewId = getResources().getIdentifier("right_icon", "id", android.R.class.getPackage().getName());

            if (smallIconViewId != 0) {
                if (notification.contentIntent != null)
                    notification.contentView.setViewVisibility(smallIconViewId, View.INVISIBLE);

                if (notification.headsUpContentView != null)
                    notification.headsUpContentView.setViewVisibility(smallIconViewId, View.INVISIBLE);

                if (notification.bigContentView != null)
                    notification.bigContentView.setViewVisibility(smallIconViewId, View.INVISIBLE);
            }
        }

        notificationManager.notify(new Random().nextInt(), notification);

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @SuppressLint("NewApi")
    public void sendBigPictureStyleNotification() {
        NotificationManager notificationManager = (NotificationManager) ctx
                .getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new
                NotificationCompat.Builder(ctx);
        Notification notification =
                mBuilder.setSmallIcon(R.mipmap.logo).setTicker(title).setWhen(0)
                        .setAutoCancel(true).setContentTitle(title).setColor(ContextCompat.getColor(ctx, R.color.button_color))
                        .setStyle(new
                                NotificationCompat.BigPictureStyle(mBuilder).bigPicture(bitmap).setSummaryText(message))
                        .setContentIntent(pi).setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                        .setLargeIcon(BitmapFactory.decodeResource(ctx.getResources(),
                                R.mipmap.ic_launcher)).build();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int smallIconViewId = getResources().getIdentifier("right_icon", "id", android.R.class.getPackage().getName());

            if (smallIconViewId != 0) {
                if (notification.contentIntent != null)
                    notification.contentView.setViewVisibility(smallIconViewId, View.INVISIBLE);

                if (notification.headsUpContentView != null)
                    notification.headsUpContentView.setViewVisibility(smallIconViewId, View.INVISIBLE);

                if (notification.bigContentView != null)
                    notification.bigContentView.setViewVisibility(smallIconViewId, View.INVISIBLE);
            }
        }
        notificationManager.notify(new Random().nextInt(), notification);
    }

    private class GetNotificationTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            bitmap = getBitmapFromURL(image);
            return null;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            sendBigPictureStyleNotification();
        }
    }

    public Bitmap getBitmapFromURL(String strURL) {
        try {
            URL url = new URL(strURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            // sendBigPictureStyleNotification();
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
