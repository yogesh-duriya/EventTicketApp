package com.endive.eventplanner.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Arpit on 15-Feb-16.
 */
public class ConnectionDetector {
    private Context _context;

    public ConnectionDetector(Context context){
        this._context = context;
    }

    public boolean isConnectingToInternet(){
//        ConnectivityManager connectivity = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
//        if (connectivity != null)
//        {
//            NetworkInfo[] info = connectivity.getAllNetworkInfo();
//            if (info != null)
//                for (int i = 0; i < info.length; i++)
//                    if (info[i].getState() == NetworkInfo.State.CONNECTED)
//                    {
//                        return true;
//                    }
//
//        }
//        return false;


        ConnectivityManager cm = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) { // connected to the internet
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                // connected to wifi
                return true;
//                Toast.makeText(_context, activeNetwork.getTypeName(), Toast.LENGTH_SHORT).show();
            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                // connected to the mobile provider's data plan
                return true;
//                Toast.makeText(_context, activeNetwork.getTypeName(), Toast.LENGTH_SHORT).show();
            }
        } else {
            return false;
            // not connected to the internet
        }
        return false;
    }
}
