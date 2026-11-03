package com.android.cast.dlna.dmc.control;

import androidx.annotation.NonNull;

/**
 * Generic callback for a DLNA service action invocation, always delivered on the main thread.
 */
public interface ServiceActionCallback<T> {

    void onSuccess(T result);

    void onFailure(@NonNull String errorMsg);
}
