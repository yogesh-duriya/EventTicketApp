package com.endive.eventplanner.interfaces;

import android.support.annotation.NonNull;

/**
 * Created by arpit.jain on 10/28/2017.
 */

public interface UpdateImageIntercace {
    void onSuccess(@NonNull String value);

    void onError(@NonNull Throwable throwable);
}
